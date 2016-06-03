package com.ecp_project.carriere_eung.foodeqc.Activity;

import android.content.Intent;
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

import com.ecp_project.carriere_eung.foodeqc.DatabaseHandler;
import com.ecp_project.carriere_eung.foodeqc.DisplayItemComplete;
import com.ecp_project.carriere_eung.foodeqc.Entity.Item;
import com.ecp_project.carriere_eung.foodeqc.Entity.ItemType;
import com.ecp_project.carriere_eung.foodeqc.R;

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
    final String TAG_ID = "id";
    ArrayList<HashMap<String,String>> itemList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_existing_item);

        //Preparing the listView displaying the existing items
        lvItem = (ListView)findViewById(R.id.listViewExistingItem);
        itemList = harvestItems();
        db =new DatabaseHandler(getApplication());
        Toast.makeText(getApplication(),"number of items : "+ db.getItemsCount(),Toast.LENGTH_LONG).show();



        adapter = new SimpleAdapter(
                DisplayExistingItemActivity.this, itemList, R.layout.display_item, new String[]{TAG_IIEM, TAG_EQUIVALENT},
                new int[]{R.id.textViewDisplayItemName,R.id.textViewDisplayItemEquivalent});
        lvItem.setAdapter(adapter);
        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("DisplayEI, on click","test");
                Log.e("DisplayEI, on click",""+itemList.get(position).get(TAG_ID));
                Intent intent = new Intent(getApplicationContext(),DisplayItemComplete.class);
                int item_id = db.getItem(Integer.parseInt(itemList.get(position).get(TAG_ID))).getId();
                intent.putExtra("item_id",item_id);
                startActivity(intent);
            }
        });


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

    /**
     *
     * @return all items from the database, so that they can be displayed
     */
    public ArrayList<HashMap<String,String>> harvestItems(){
        DatabaseHandler db =new DatabaseHandler(getApplication());
        ArrayList<HashMap<String,String>> itemList = new ArrayList<HashMap<String,String>>();
        HashMap<String,String> baseItemMap = new HashMap<>();
        baseItemMap.put(TAG_IIEM,getString(R.string.item_name));
        baseItemMap.put(TAG_EQUIVALENT,getString(R.string.CO2_equivalent));
        for(Item item:db.getAllItems()){
            HashMap<String, String> itemMap = new HashMap<String, String>();
            itemMap.put(TAG_IIEM, item.getName());
            itemMap.put(TAG_EQUIVALENT, String.valueOf(item.getCo2Equivalent()));
            Log.e("DisplayEI, harvest",""+item.getId());
            itemMap.put(TAG_ID,String.valueOf(item.getId()));
            itemList.add(itemMap);
        }
        db.close();
        return  itemList;
    }

    /**
     *
     * @param type
     * @return all items of a given type from the database, so that they can be displayed
     */
    public ArrayList<HashMap<String,String>> harvestItems(ItemType type){
        DatabaseHandler db =new DatabaseHandler(getApplication());
        HashMap<String,String> baseItemMap = new HashMap<>();
        baseItemMap.put(TAG_IIEM,getString(R.string.item_name));
        baseItemMap.put(TAG_EQUIVALENT,getString(R.string.CO2_equivalent));
        ArrayList<HashMap<String,String>> itemList = new ArrayList<HashMap<String,String>>();
        for(Item item:db.getAllItems(type)){
            HashMap<String, String> itemMap = new HashMap<String, String>();
            itemMap.put(TAG_IIEM, item.getName());
            itemMap.put(TAG_EQUIVALENT, String.valueOf(item.getCo2Equivalent()));
            itemMap.put(TAG_ID,String.valueOf(item.getId()));
            itemList.add(itemMap);
        }
        db.close();
        return  itemList;
    }
}
