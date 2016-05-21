package com.ecp_project.carriere_eung.foodeqc;

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
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Matthieu on 19/05/2016.
 * allows the user to create a new item, made from different ingredients.
 * Auto-completion from the known items list will be featured
 */
public class AddNewItemActivity extends AppCompatActivity {

    EditText itemNameText;
    EditText ingredientText;
    ListView lvIngredients;
    Button addIngredient;
    Button createItem;
    ArrayList<HashMap<String,String>> ingredientList = new ArrayList<HashMap<String,String>>();
    ListAdapter adapter;
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
                HashMap<String, String> ingredient = new HashMap<String, String>();
                ingredient.put("ingredient", ingredientName);
                ingredient.put("proportion", "0");
                ingredientList.add(ingredient);
                lvIngredients.setAdapter(adapter);
            }
        });



        lvIngredients = (ListView) findViewById(R.id.listViewIngredients);
        lvIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplication());
                builder.setTitle(R.string.set_proportion_title);
                builder.setView(R.layout.set_proportion);
                builder.setNegativeButton(R.string.delete_ingredient, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNeutralButton(R.string.confirm_proportion, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


            }

            ;
        });
    }
}
