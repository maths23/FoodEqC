package com.ecp_project.carriere_eung.foodeqc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ecp_project.carriere_eung.foodeqc.Entity.ComposedItem;
import com.ecp_project.carriere_eung.foodeqc.Entity.Ingredient;
import com.ecp_project.carriere_eung.foodeqc.Entity.Item;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Matthieu on 02/06/2016.
 */
public class DisplayItemComplete extends AppCompatActivity {
    DatabaseHandler db;
    TextView textItemName;
    TextView textTypeValue;
    TextView textEquivalentValue;
    ListView lvIngredients;
    ListAdapter adapter;
    ArrayList<HashMap<String,String>> ingredientList = new ArrayList<HashMap<String,String>>();
    final String TAG_INGREDIENT = "ingredient";
    final String TAG_PROPORTION = "proportion";
    final String TAG_EQUIVALENT = "equivalent";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_item_complete);

        textItemName = (TextView)findViewById(R.id.textViewItemName);
        textTypeValue = (TextView)findViewById(R.id.textViewTypeValue);
        textEquivalentValue = (TextView)findViewById(R.id.textViewEquivalentValue);
        lvIngredients = (ListView)findViewById(R.id.listViewIngredient);


        int item_id = (int) getIntent().getExtras().get("item_id");
        db = new DatabaseHandler(getApplication());
        Item item = db.getItem(item_id);

        textItemName.setText(item.getName());
        textTypeValue.setText(item.getType().toString());
        textEquivalentValue.setText(String.valueOf(item.getCo2Equivalent()));
        if(item instanceof ComposedItem){
            for(Ingredient ing:((ComposedItem) item).getIngredients()){
                HashMap<String, String> ingredient = new HashMap<String, String>();
                ingredient.put(TAG_INGREDIENT, ing.getName());
                ingredient.put(TAG_PROPORTION, String.valueOf(ing.getProportion()));
                ingredient.put(TAG_EQUIVALENT,String.valueOf(ing.getProportion()*ing.getItem().getCo2Equivalent()/100));
                ingredientList.add(ingredient);
            }
        }

        adapter = new SimpleAdapter(
                DisplayItemComplete.this, ingredientList, R.layout.display_ingredient_complete, new String[]{TAG_INGREDIENT,TAG_PROPORTION,TAG_EQUIVALENT},
                new int[]{R.id.textViewIngredientNameDisplay,R.id.textViewIngredientProportionDisplay,R.id.textViewIngredientEquivalentDisplay});
        lvIngredients.setAdapter(adapter);

    }
}
