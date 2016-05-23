package com.ecp_project.carriere_eung.foodeqc;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ecp_project.carriere_eung.foodeqc.AuxiliaryMethods.AddNewItemAuxiliary;
import com.ecp_project.carriere_eung.foodeqc.Widget.CustomNumberPicker;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Matthieu on 19/05/2016.
 * allows the user to create a new item, made from different ingredients.
 * Auto-completion from the known items list will be featured
 * [NOTE] Pensez à ajouter la posibilité de rentrer directement la proportion avec une syntaxe du style "nom /proportion"
 * mais attention aux doublons + utiliser un clean string, pour que "test", et "test " soit considéré comme la même chose.
 */
public class AddNewItemActivity extends AppCompatActivity implements  SetProportionDialog.SetProportionDialogListener {

    EditText itemNameText;
    EditText ingredientText;
    ListView lvIngredients;
    Button addIngredient;
    Button createItem;
    ArrayList<HashMap<String,String>> ingredientList = new ArrayList<HashMap<String,String>>();
    ListAdapter adapter;
    final String TAG_INGREDIENT = "ingredient";
    final String TAG_PROPORTION = "proportion";

    //those two fiels are used to store the name and the position of the item of the ListView
    //on which onItemClick is called
    String temporayStorageIngredientName;
    int temporaryStoragePosition;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_item);

        itemNameText = (EditText) findViewById(R.id.editTextAddItemName);
        ingredientText = (EditText) findViewById(R.id.editTextAddIngredient);
        addIngredient = (Button) findViewById(R.id.buttonAddIngredient);
        createItem = (Button) findViewById(R.id.buttonCreateItem);
        lvIngredients = (ListView) findViewById(R.id.listViewIngredients);

        adapter = new SimpleAdapter(
                AddNewItemActivity.this, ingredientList, R.layout.display_ingredient, new String[]{"ingredient", "proportion"},
                new int[]{R.id.textViewIngredientNameDisplay, R.id.textViewIngredientProportionDisplay});
        lvIngredients.setAdapter(adapter);

        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingredientName = ingredientText.getText().toString();
                if (AddNewItemAuxiliary.listContainsMap(ingredientList,ingredientName)){
                    Toast.makeText(getApplication(),R.string.ingredient_alreay_exist,Toast.LENGTH_LONG).show();
                } else{
                    HashMap<String, String> ingredient = new HashMap<String, String>();
                    ingredient.put(TAG_INGREDIENT, ingredientName);
                    ingredient.put(TAG_PROPORTION, "0");
                    ingredientList.add(ingredient);
                    lvIngredients.setAdapter(adapter);

                }
                ingredientText.setText("");

            }
        });



        lvIngredients = (ListView) findViewById(R.id.listViewIngredients);
        lvIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lvIngredients.setAdapter(adapter);
                Toast.makeText(AddNewItemActivity.this, "j'ai cliqué sur l'item " + position, Toast.LENGTH_SHORT).show();
                temporayStorageIngredientName = ingredientList.get(position).get(TAG_INGREDIENT);
                temporaryStoragePosition = position;
                SetProportionDialog aDialog = new SetProportionDialog();
                FragmentManager manager = getFragmentManager();
                aDialog.show(getFragmentManager(),"SetProportionDialog");
            }

            ;
        });


    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        ingredientList.remove(temporaryStoragePosition);
        lvIngredients.setAdapter(adapter);
    }

    @Override
    public void onDialogNeutralClick(DialogFragment dialog, int proportion) {
        Toast.makeText(getApplication(),""+proportion,Toast.LENGTH_LONG).show();
        HashMap<String, String> ingredient = new HashMap<String, String>();
        ingredient.put(TAG_INGREDIENT, temporayStorageIngredientName);
        ingredient.put(TAG_PROPORTION, ""+proportion);
        ingredientList.set(temporaryStoragePosition,ingredient);
        lvIngredients.setAdapter(adapter);
    }
}
