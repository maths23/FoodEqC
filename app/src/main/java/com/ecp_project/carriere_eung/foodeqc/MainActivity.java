package com.ecp_project.carriere_eung.foodeqc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ecp_project.carriere_eung.foodeqc.Entity.Item;
import com.ecp_project.carriere_eung.foodeqc.Entity.ItemType;

public class MainActivity extends AppCompatActivity {
    Button createItem;
    Button createRepas;
    Button showItems;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        publishItemsInDatabase();


        createItem = (Button)findViewById(R.id.buttonCreateItemMain);
        createItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddNewItemActivity.class);
                startActivity(intent);

            }
        });
        createRepas = (Button)findViewById(R.id.buttonCreateRepasMain);
        createRepas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddNewRepasTypeActivity.class);
                startActivity(intent);
            }
        });

        showItems = (Button)findViewById(R.id.buttonSeeItems);
        showItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),DisplayExistingItemActivity.class);
                startActivity(intent);
            }
        });
    }

    //put fake Data in db
    //DANGER : crash dès qu'on met un "'" dans le nom ^^
    private void publishItemsInDatabase() {
        Log.d("insert","insertion commencée ...");
        db.addItem(new Item(getString(R.string.wheat),100,ItemType.base));
        db.addItem(new Item(getString(R.string.potatoes),20,ItemType.base));
        db.addItem(new Item(getString(R.string.tomatoes),90,ItemType.base));
        db.addItem(new Item(getString(R.string.tomatoes_greenhouse),734,ItemType.base));
        db.addItem(new Item(getString(R.string.letuce),77,ItemType.base));
        db.addItem(new Item(getString(R.string.letuce_greenhouse),2970,ItemType.base));
        db.addItem(new Item(getString(R.string.cucomber),6,ItemType.base));
        db.addItem(new Item(getString(R.string.cucomber_greenhouses),587,ItemType.base));
        db.addItem(new Item(getString(R.string.grapes),19,ItemType.base));
        db.addItem(new Item(getString(R.string.flour),125,ItemType.base));
        db.addItem(new Item(getString(R.string.bread),125,ItemType.base));
        db.addItem(new Item(getString(R.string.sugar),200,ItemType.base));
        db.addItem(new Item(getString(R.string.alcohol_pure),400,ItemType.base));
        db.addItem(new Item(getString(R.string.wine),400,ItemType.base));
        db.addItem(new Item(getString(R.string.calf_meat),15900,ItemType.base));
        db.addItem(new Item(getString(R.string.cow_milk_whole),329,ItemType.base));
        db.addItem(new Item(getString(R.string.beef_meat),7330,ItemType.base));
        db.addItem(new Item(getString(R.string.yoghurt),660,ItemType.base));
        db.addItem(new Item(getString(R.string.cheese),2000,ItemType.base));
        db.addItem(new Item(getString(R.string.butter),2700,ItemType.base));
        db.addItem(new Item(getString(R.string.pork_meat),1410,ItemType.base));
        db.addItem(new Item(getString(R.string.chicken_meat_battery),770,ItemType.base));
        db.addItem(new Item(getString(R.string.egg),870,ItemType.base));
        db.addItem(new Item(getString(R.string.mutton_meat),6330,ItemType.base));
        db.addItem(new Item(getString(R.string.milk_lamb),8470,ItemType.base));
        db.addItem(new Item(getString(R.string.sea_fish_france),520,ItemType.base));
        db.addItem(new Item(getString(R.string.remote_fish),1000,ItemType.base));
        db.addItem(new Item(getString(R.string.shrinks),2528,ItemType.base));
        db.addItem(new Item(getString(R.string.turkey_meat),800,ItemType.base));
        db.addItem(new Item(getString(R.string.duck_meat),990,ItemType.base));
        db.addItem(new Item(getString(R.string.free_range_chicken),1330,ItemType.base));
        db.addItem(new Item(getString(R.string.guinea_fowl_meat),1630,ItemType.base));





        Log.d("insert","insertion finie ...");
    }


}
