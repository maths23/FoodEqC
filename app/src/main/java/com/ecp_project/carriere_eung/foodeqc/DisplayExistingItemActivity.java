package com.ecp_project.carriere_eung.foodeqc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_item);
        lvItem = (ListView)findViewById(R.id.listViewExistingItem);
        ArrayList<HashMap<String,String>> itemList = new ArrayList<HashMap<String,String>>();
        db =new DatabaseHandler(getApplication());

        for(Item item:db.getAllContact()){

        }

        adapter = new SimpleAdapter(
                DisplayExistingItemActivity.this, itemList, R.layout.display_ingredient, new String[]{"ingredient", "proportion"},
                new int[]{R.id.textViewDisplayItemName, R.id.textViewDisplayItemEquivalent});
        lvItem.setAdapter(adapter);
    }
}
