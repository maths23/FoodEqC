package com.ecp_project.carriere_eung.foodeqc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ecp_project.carriere_eung.foodeqc.Entity.RepasType;

/**
 * Created by eung on 21/05/16.
 */
public class AddNewRepasTypeActivity extends AppCompatActivity {

    Button breakfast;
    Button lunch;
    Button dinner;
    Button snack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_repas_type);

        breakfast = (Button)findViewById(R.id.button_break_fast);
        lunch = (Button)findViewById(R.id.button_lunch);
        dinner = (Button)findViewById(R.id.button_dinner);
        snack = (Button)findViewById(R.id.button_snack);

        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddNewRepasActivity.class);
                intent.putExtra("RepasType", RepasType.breakfast);
                startActivity(intent);
            }
        });

        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddNewRepasActivity.class);
                intent.putExtra("RepasType", RepasType.lunch);
                startActivity(intent);
            }
        });

        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddNewRepasActivity.class);
                intent.putExtra("RepasType", RepasType.dinner);
                startActivity(intent);
            }
        });

        snack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddNewRepasActivity.class);
                intent.putExtra("RepasType", RepasType.snack);
                startActivity(intent);
            }
        });
    }
}
