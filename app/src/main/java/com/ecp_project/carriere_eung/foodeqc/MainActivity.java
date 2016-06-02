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

    //putting data from ADEME in the database
    //Equivalent are in gramme of C02 per 100g
    //DANGER : crash dès qu'on met un "'" dans le nom ^^
    public void publishItemsInDatabase() {
        Log.d("insert","insertion commencée ...");
        db =new DatabaseHandler(getApplication());
        if (db.getItemsCount() == 0){
            db.addItem(new Item(getString(R.string.wheat),100*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.potatoes),20*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.tomatoes),90*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.tomatoes_greenhouse),734*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.letuce),77*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.letuce_greenhouse),2970*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.cucomber),6*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.cucomber_greenhouses),587*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.grapes),19*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.flour),125*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.bread),125*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.sugar),200*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.alcohol_pure),400*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.wine),400*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.calf_meat),15900*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.cow_milk_whole),329*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.beef_meat),7330*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.yoghurt),660*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.cheese),2000*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.butter),2700*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.pork_meat),1410*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.chicken_meat_battery),770*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.egg),870*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.mutton_meat),6330*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.milk_lamb),8470*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.sea_fish_france),520*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.remote_fish),1000*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.shrinks),2528*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.turkey_meat),800*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.duck_meat),990*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.free_range_chicken),1330*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.guinea_fowl_meat),1630*44/12/10,ItemType.base));

            db.addItem(new Item(getString(R.string.bread),137*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.pasta),383*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.rice),120*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.semoule),120*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.viennoiserie),809*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.biscuits),684*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.patisserie),965*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.charcuterie),1410*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.vegetables_no_potatoes),122*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.dried_pulse),64*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.fruits),122*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.pizza),829*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.sandwitch),1015*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.soup),238*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.industrial_meals),1849*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.stewed_fruits),32*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.water_bottled),6*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.fruit_juice),64*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.coffee),200*44/12/10,ItemType.base));
            db.addItem(new Item(getString(R.string.tea),100*44/12/10,ItemType.base));



        }






        Log.d("insert","insertion finie ...");
    }


}
