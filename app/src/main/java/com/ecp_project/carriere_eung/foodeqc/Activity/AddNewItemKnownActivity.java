package com.ecp_project.carriere_eung.foodeqc.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ecp_project.carriere_eung.foodeqc.AuxiliaryMethods.MenuHandler;
import com.ecp_project.carriere_eung.foodeqc.DatabaseHandler;
import com.ecp_project.carriere_eung.foodeqc.Entity.Item;
import com.ecp_project.carriere_eung.foodeqc.Entity.ItemType;
import com.ecp_project.carriere_eung.foodeqc.R;

/**
 * This activity lets the user input an item whose CO2 equivalent is known.
 * Created by Matthieu on 05/06/2016.
 */
public class AddNewItemKnownActivity extends AppCompatActivity {

    EditText itemNameText;
    EditText itemEquivalentText;
    Button createItemButton;
    DatabaseHandler db;

    //Used to keep in memory the name that should be returned to the calling activity, if there is one
    //Not totally sure it is needed ...
    private String temporayStorageItemName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_item_known_equivalent);



        db =new DatabaseHandler(getApplication());
        itemNameText = (EditText)findViewById(R.id.editTextAddItemNameKnown);
        itemEquivalentText = (EditText)findViewById(R.id.editTextKnownEquivalent);
        createItemButton = (Button)findViewById(R.id.buttonCreateItemKnownEquivalent);

        temporayStorageItemName = "";
        //used when the activities is called from addNewRepas activity
        Bundle extras = getIntent().getExtras();
        Boolean isThereExtras = !(extras == null);
        if(isThereExtras){
            if(extras.containsKey("item_name")){
                itemNameText.setText((String) extras.get("item_name"));
                temporayStorageItemName = (String) extras.get("item_name");
            }
        }

        createItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = itemNameText.getText().toString();
                double itemEquivalent = Double.parseDouble(itemEquivalentText.getText().toString());
                if(itemName.equals("")){
                    Toast.makeText(getApplicationContext(), R.string.no_name_entered, Toast.LENGTH_LONG).show();
                } else if (itemEquivalentText.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), R.string.no_equivalent_entered, Toast.LENGTH_LONG).show();
                } else {
                    Item toBeAdded = new Item(itemName,itemEquivalent, ItemType.base);
                    boolean succes = db.addItem(toBeAdded);
                    Log.e("Create ",""+succes);
                    if(succes) {
                        Toast.makeText(getApplication(),"New item created : " + toBeAdded.toString(getApplicationContext()), Toast.LENGTH_LONG).show();
                    }
                    itemNameText.setText("");
                    itemEquivalentText.setText("");
                }
                finish();
            }
        });
    }

    //menu handling
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_test, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return MenuHandler.HandleMenuEvents(item,getApplicationContext(),this);
    }
}
