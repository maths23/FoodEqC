package com.ecp_project.carriere_eung.foodeqc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button createItem;
    Button createRepas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }
}
