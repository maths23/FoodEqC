package com.ecp_project.carriere_eung.foodeqc.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ecp_project.carriere_eung.foodeqc.R;

/**
 * Created by eung on 08/06/16.
 */
public class ManageItemActivity extends AppCompatActivity {

    Button showItems;
    Button createItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_items_activity);


        showItems = (Button)findViewById(R.id.buttonSeeItems);
        showItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),DisplayExistingItemActivity.class);
                startActivity(intent);
            }
        });



        createItem = (Button)findViewById(R.id.buttonCreateItemMain);
        createItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddNewItemTypeActivity.class);
                startActivity(intent);

            }
        });

    }
}
