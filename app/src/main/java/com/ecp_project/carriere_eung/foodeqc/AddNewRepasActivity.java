package com.ecp_project.carriere_eung.foodeqc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ecp_project.carriere_eung.foodeqc.Entity.RepasType;

/**
 * Created by eung on 21/05/16.
 * Allow the user to had a meal.
 */
public class AddNewRepasActivity extends AppCompatActivity {
    RepasType repasType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_repas);

        repasType = (RepasType)getIntent().getExtras().get("RepasType");


    }
}
