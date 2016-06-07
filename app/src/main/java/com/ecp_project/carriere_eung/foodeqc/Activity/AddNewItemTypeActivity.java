package com.ecp_project.carriere_eung.foodeqc.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ecp_project.carriere_eung.foodeqc.AuxiliaryMethods.MenuHandler;
import com.ecp_project.carriere_eung.foodeqc.R;

/**
 * Created by Matthieu on 05/06/2016.
 */
public class AddNewItemTypeActivity extends AppCompatActivity {

    Button buttonKnown;
    Button buttonUnknown;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_item_type);

        buttonKnown = (Button)findViewById(R.id.buttonCreateItemTypeKnown);
        buttonUnknown = (Button)findViewById(R.id.buttonCreateItemTypeUnknown);

        buttonKnown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddNewItemKnownActivity.class);
                startActivity(intent);
            }
        });
        buttonUnknown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddNewItemActivity.class);
                startActivity(intent);
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
