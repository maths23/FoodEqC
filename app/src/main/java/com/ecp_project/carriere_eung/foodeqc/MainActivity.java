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
        db = new DatabaseHandler(this);
        db.addItem(new Item("viande de boeuf",150,ItemType.base));
        db.addItem(new Item("viande de porc",100,ItemType.base));
        db.addItem(new Item("viande de poulet",50,ItemType.base));
        db.addItem(new Item("viande d agneau",120,ItemType.base));
        db.addItem(new Item("viande d homme",666,ItemType.base));
        db.addItem(new Item("viande de veau",110,ItemType.base));
        db.addItem(new Item("carotte",15,ItemType.base));
        db.addItem(new Item("pommes",20,ItemType.base));
        if (db.addItem(new Item("thon",45,ItemType.base))){
            Toast.makeText(getApplication(),"sucess",Toast.LENGTH_LONG);
            Log.d("insert","ok");
        } else {
            Toast.makeText(getApplication(),"failure",Toast.LENGTH_LONG);
            Log.d("insert","pas ok");
        };
        Log.d("insert","insertion finie ...");
    }


}
