package com.ecp_project.carriere_eung.foodeqc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ecp_project.carriere_eung.foodeqc.Entity.Item;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Matthieu on 25/05/2016.
 */
public class DisplayExistingItemActivity extends AppCompatActivity {
    DatabaseHandler db;
    ListView lvItem;
    ListAdapter adapter;
    final String TAG_IIEM = "item";
    final String TAG_EQUIVALENT = "equivalent";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_existing_item);
        lvItem = (ListView)findViewById(R.id.listViewExistingItem);
        ArrayList<HashMap<String,String>> itemList = new ArrayList<HashMap<String,String>>();
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
    }
}
