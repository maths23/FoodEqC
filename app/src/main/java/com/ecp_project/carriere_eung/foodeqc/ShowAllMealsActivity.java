package com.ecp_project.carriere_eung.foodeqc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.ecp_project.carriere_eung.foodeqc.Adapter.RepasAdapter;
import com.ecp_project.carriere_eung.foodeqc.Entity.Repas;
import com.ecp_project.carriere_eung.foodeqc.Entity.RepasType;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by eung on 02/06/16.
 * Activité qui montre tous les repas
 */
public class ShowAllMealsActivity extends AppCompatActivity {

    ListView lvMeals;

    RepasAdapter adapter;
    List<Repas> listMeals;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_all_meals);

        db = new DatabaseHandler(getApplication());

        initialiseDatabase();

        listMeals = db.getAllRepas();
        lvMeals = (ListView)findViewById(R.id.listViewMeals);

        initializeAdapter();
        lvMeals.setAdapter(adapter);

    }

    private void initialiseDatabase() {
        db.addRepas(new Repas(new GregorianCalendar(), RepasType.dinner));
        db.addRepas(new Repas(new GregorianCalendar(), RepasType.lunch));
    }


    public void initializeAdapter() {
        adapter = new RepasAdapter(ShowAllMealsActivity.this, listMeals);
    }
}
