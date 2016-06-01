package com.ecp_project.carriere_eung.foodeqc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.ecp_project.carriere_eung.foodeqc.Entity.Item;
import com.ecp_project.carriere_eung.foodeqc.Entity.ItemType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Matthieu on 25/05/2016.
 */
public class DisplayExistingItemActivity extends AppCompatActivity {
    DatabaseHandler db;
    ListView lvItem;
    ListAdapter adapter;
    Spinner spinner;
    ArrayAdapter<String> spinnerAdapter;
    final String TAG_IIEM = "item";
    final String TAG_EQUIVALENT = "equivalent";
    ArrayList<HashMap<String,String>> itemList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_existing_item);
        lvItem = (ListView)findViewById(R.id.listViewExistingItem);
        itemList = new ArrayList<HashMap<String,String>>();
        db =new DatabaseHandler(getApplication());

        for(Item item:db.getAllItems()){
            HashMap<String, String> itemMap = new HashMap<String, String>();
            itemMap.put(TAG_IIEM, item.getName());
            itemMap.put(TAG_EQUIVALENT, String.valueOf(item.getCo2Equivalent()));
            itemList.add(itemMap);
        }
        Toast.makeText(getApplication(),"number of items : "+ db.getItemsCount(),Toast.LENGTH_LONG).show();


        adapter = new SimpleAdapter(
                DisplayExistingItemActivity.this, itemList, R.layout.display_item, new String[]{TAG_IIEM, TAG_EQUIVALENT},
                new int[]{R.id.textViewDisplayItemName, R.id.textViewDisplayItemEquivalent});
        lvItem.setAdapter(adapter);

        spinner = (Spinner)findViewById(R.id.spinnerChooseItemType);
        final ArrayList<String> types = new ArrayList<>();
        String[]types_bis = getResources().getStringArray(R.array.item_type);
        for(String type:types_bis){
            types.add(type);
        }
        spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,types);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                   itemList = harvestItems();
                else {
                    Log.e("DisplayActivity",""+types.get(position));
                    itemList = harvestItems(ItemType.toItemType(types.get(position).toLowerCase()));
                }
                adapter = new SimpleAdapter(
                        DisplayExistingItemActivity.this, itemList, R.layout.display_item, new String[]{TAG_IIEM, TAG_EQUIVALENT},
                        new int[]{R.id.textViewDisplayItemName, R.id.textViewDisplayItemEquivalent});
                lvItem.setAdapter(adapter);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


    }


    public ArrayList<HashMap<String,String>> harvestItems(){
        DatabaseHandler db =new DatabaseHandler(getApplication());
        ArrayList<HashMap<String,String>> itemList = new ArrayList<HashMap<String,String>>();
        for(Item item:db.getAllItems()){
            HashMap<String, String> itemMap = new HashMap<String, String>();
            itemMap.put(TAG_IIEM, item.getName());
            itemMap.put(TAG_EQUIVALENT, String.valueOf(item.getCo2Equivalent()));
            itemList.add(itemMap);
        }
        return  itemList;
    }

    public ArrayList<HashMap<String,String>> harvestItems(ItemType type){
        DatabaseHandler db =new DatabaseHandler(getApplication());
        ArrayList<HashMap<String,String>> itemList = new ArrayList<HashMap<String,String>>();
        for(Item item:db.getAllItems(type)){
            HashMap<String, String> itemMap = new HashMap<String, String>();
            itemMap.put(TAG_IIEM, item.getName());
            itemMap.put(TAG_EQUIVALENT, String.valueOf(item.getCo2Equivalent()));
            itemList.add(itemMap);
        }
        return  itemList;
    }
}
