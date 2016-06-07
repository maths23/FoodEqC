package com.ecp_project.carriere_eung.foodeqc.Activity;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ecp_project.carriere_eung.foodeqc.AuxiliaryMethods.AddNewItemAuxiliary;
import com.ecp_project.carriere_eung.foodeqc.AuxiliaryMethods.MenuHandler;
import com.ecp_project.carriere_eung.foodeqc.CustomAutoCompleteTextChangedListener;
import com.ecp_project.carriere_eung.foodeqc.DatabaseHandler;
import com.ecp_project.carriere_eung.foodeqc.Entity.ComposedItem;
import com.ecp_project.carriere_eung.foodeqc.Entity.Ingredient;
import com.ecp_project.carriere_eung.foodeqc.Entity.ItemType;
import com.ecp_project.carriere_eung.foodeqc.R;
import com.ecp_project.carriere_eung.foodeqc.SetProportionDialog;
import com.ecp_project.carriere_eung.foodeqc.Widget.CustomAutoCompleteView;
import com.ecp_project.carriere_eung.foodeqc.Activity.AddNewRepasActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Matthieu on 19/05/2016.
 * allows the user to create a new item, made from different ingredients.
 * Auto-completion from the known items list will be featured
 * [NOTE] Pensez à ajouter la posibilité de rentrer directement la proportion avec une syntaxe du style "nom /proportion"
 * mais attention aux doublons + utiliser un clean string, pour que "test", et "test " soit considéré comme la même chose.
 */
public class AddNewItemActivity extends AppCompatActivity  {

    public DatabaseHandler db;


    //creating new item
    EditText itemNameText;
    Button createItem;

    //inputting and showing ingredients list
    EditText ingredientText;
    ListView lvIngredients;
    Button addIngredient;
    ArrayList<HashMap<String,String>> ingredientList = new ArrayList<HashMap<String,String>>();
    ListAdapter adapter;
    final String TAG_INGREDIENT = "ingredient";
    final String TAG_PROPORTION = "proportion";

    //just before creating the new item record, base item proportion will expressed as a %
    private int total_proportion;

    //autocompletion feature
    public CustomAutoCompleteView myAutoComplete;
    public ArrayAdapter<String> myAdapter;
    public String[] item = new String[] {"Please search..."};


    //Used to keep in memory the name that should be returned to the calling activity, if there is one
    //Not totally sure it is needed ...
    private String temporayStorageItemName;


    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_item);



        db =new DatabaseHandler(getApplication());
        itemNameText = (EditText) findViewById(R.id.editTextAddItemName);

        temporayStorageItemName = "";
        //used when the activities is called from addNewRepas activity
        Bundle extras = getIntent().getExtras();
        Boolean isThereExtras = !(extras == null);
        if(isThereExtras){
            if(extras.containsKey("item_name")){
                itemNameText.setText((String) extras.get("item_name"));
                temporayStorageItemName = (String) extras.get("item_name");
            }
        }


        ingredientText = (EditText) findViewById(R.id.autocompleteAddIngredient);
        addIngredient = (Button) findViewById(R.id.buttonAddIngredient);
        createItem = (Button) findViewById(R.id.buttonCreateItem);
        lvIngredients = (ListView) findViewById(R.id.listViewIngredients);
        myAutoComplete = (CustomAutoCompleteView)findViewById(R.id.autocompleteAddIngredient);
        myAutoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this));
        myAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,item);
        myAutoComplete.setAdapter(myAdapter);

        adapter = new SimpleAdapter(
                AddNewItemActivity.this, ingredientList, R.layout.display_ingredient, new String[]{"ingredient", "proportion"},
                new int[]{R.id.textViewIngredientNameDisplay, R.id.textViewIngredientProportionDisplay});
        lvIngredients.setAdapter(adapter);


        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingredientName = ingredientText.getText().toString();
                if (AddNewItemAuxiliary.listContainsMap(ingredientList,ingredientName)){
                    Toast.makeText(getApplication(),R.string.ingredient_alreay_on_the_list,Toast.LENGTH_LONG).show();
                    ingredientText.setText("");
                } else if (!db.checksIfItemNameExists(ingredientName)) {
                    Toast.makeText(getApplication(),R.string.item_does_not_exist,Toast.LENGTH_LONG).show();
                } else{
                    if (db.getItem(ingredientName) instanceof ComposedItem ){
                        Toast.makeText(getApplication(),R.string.no_composed_item_can_be_input,Toast.LENGTH_LONG).show();
                    } else{
                        HashMap<String, String> ingredient = new HashMap<String, String>();
                        ingredient.put(TAG_INGREDIENT, ingredientName);
                        ingredient.put(TAG_PROPORTION, "0");
                        ingredientList.add(ingredient);
                        lvIngredients.setAdapter(adapter);
                        ingredientText.setText("");
                    }


                }


            }
        });



        lvIngredients = (ListView) findViewById(R.id.listViewIngredients);

        //launch the fragment that allow to put in the proportion
        lvIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alert = new AlertDialog.Builder(AddNewItemActivity.this);
                alert.setTitle(R.string.set_proportion_title); //Set Alert dialog title here

                // Set an EditText view to get user input
                final EditText input = new EditText(AddNewItemActivity.this);
                final String currentValue = ingredientList.get(position).get(TAG_PROPORTION);
                input.setHint(String.valueOf(currentValue));
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(input);

                alert.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        total_proportion -= Integer.parseInt(currentValue);
                        String newValue = input.getText().toString();
                        ingredientList.get(position).put(TAG_PROPORTION,newValue);
                        total_proportion += Integer.parseInt(newValue);
                        lvIngredients.setAdapter(adapter);
                    }
                });
                alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
                /*
                Toast.makeText(AddNewItemActivity.this, "j'ai cliqué sur l'item " + position, Toast.LENGTH_SHORT).show();
                temporayStorageIngredientName = ingredientList.get(position).get(TAG_INGREDIENT);
                temporaryStoragePosition = position;
                SetProportionDialog aDialog = new SetProportionDialog();
                FragmentManager manager = getFragmentManager();
                aDialog.show(getFragmentManager(),"SetProportionDialog");
                */
            }

            ;
        });

       lvIngredients.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               createAlertDialogDeleteItem(position);
               return true;
           }
       });

        createItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
                String itemName = itemNameText.getText().toString();
                Log.d("proportion check","total proportion is : " + total_proportion);
                for(HashMap map:ingredientList){
                    ingredients.add(new Ingredient(db.getItem((String) map.get(TAG_INGREDIENT)),itemName,Double.parseDouble((String)map.get(TAG_PROPORTION))*100/total_proportion));
                    Log.d("proportion check",db.getItem((String) map.get(TAG_INGREDIENT)).getName() + " proportion : "+ Double.parseDouble((String)map.get(TAG_PROPORTION))*100/total_proportion );
                }
                if(itemName.equals("")){
                    Toast.makeText(getApplicationContext(), R.string.no_name_entered, Toast.LENGTH_LONG).show();
                } else if (ingredients.size()<=1){
                    Toast.makeText(getApplicationContext(), R.string.not_enough_ingredients, Toast.LENGTH_LONG).show();
                } else if (total_proportion == 0){
                    Toast.makeText(getApplicationContext(), R.string.no_proportion_entered, Toast.LENGTH_LONG).show();
                }
                else{
                    ComposedItem toBeAdded = new ComposedItem(itemName,ItemType.local,ingredients);
                    boolean succes = db.addItem(toBeAdded);
                    if(succes) {
                        Toast.makeText(getApplication(),"New item created : " + toBeAdded.toString(getApplicationContext()), Toast.LENGTH_LONG).show();
                    }
                    finish();
                }


            }
        });


    }

    //menu handling
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_test, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return MenuHandler.HandleMenuEvents(item,getApplicationContext(),this);
    }


    public void createAlertDialogDeleteItem(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alertDialogDeleteItemRepas);
        builder.setCancelable(true);

        builder.setPositiveButton(R.string.delete_item_repas, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //deducing the proportion of the deleted item from the total
                int toBeDeduced = Integer.parseInt(ingredientList.get(position).get(TAG_PROPORTION));
                total_proportion -= toBeDeduced;
                ingredientList.remove(position);
                lvIngredients.setAdapter(adapter);
            }
        });
        builder.setNegativeButton(R.string.cancel_delete_item_repas, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void finish(View view) {
        Intent intent = new Intent();
        String returnValue = temporayStorageItemName;
        intent.putExtra("returnValue",returnValue);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
