package com.ecp_project.carriere_eung.foodeqc.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ecp_project.carriere_eung.foodeqc.Adapter.RepasAdapter;
import com.ecp_project.carriere_eung.foodeqc.AuxiliaryMethods.MenuHandler;
import com.ecp_project.carriere_eung.foodeqc.DatabaseHandler;
import com.ecp_project.carriere_eung.foodeqc.Entity.Repas;
import com.ecp_project.carriere_eung.foodeqc.Entity.RepasType;
import com.ecp_project.carriere_eung.foodeqc.Exception.ItemNotFoundException;
import com.ecp_project.carriere_eung.foodeqc.R;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by eung on 02/06/16.
 * Activité qui montre tous les repas
 */
public class ShowAllMealsActivity extends AppCompatActivity {

    public static final String EXTRA_REPAS = "extra_repas";
    ListView lvMeals;

    RepasAdapter adapter;
    List<Repas> listMeals;
    Repas temporaryStorageMeal;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_all_meals);

        db = new DatabaseHandler(getApplication());

        listMeals = db.getAllRepas();
        lvMeals = (ListView)findViewById(R.id.listViewMeals);

        initializeAdapter();
        lvMeals.setAdapter(adapter);

        lvMeals.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                temporaryStorageMeal = listMeals.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowAllMealsActivity.this);
                builder.setMessage(getString(R.string.alertDialogDeleteRepas) + temporaryStorageMeal.getId());
                builder.setCancelable(true);
                builder.setPositiveButton(R.string.delete, new OkOnClickListener());
                builder.setNegativeButton(R.string.cancel, new CancelOnClickListener());
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });

        lvMeals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),AddNewRepasActivity.class);
                intent.putExtra(EXTRA_REPAS, listMeals.get(position).getId());
                startActivity(intent);
            }
        });

    }

    private void initialiseDatabase() {
        db.addRepas(new Repas(new GregorianCalendar(), RepasType.dinner));
        db.addRepas(new Repas(new GregorianCalendar(), RepasType.lunch));

    }


    public void initializeAdapter() {
        adapter = new RepasAdapter(ShowAllMealsActivity.this, listMeals);
    }

    private final class CancelOnClickListener implements
            DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    private final class OkOnClickListener implements
            DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            db.deleteRepas(temporaryStorageMeal);
            listMeals = db.getAllRepas();
            initializeAdapter();
            lvMeals.setAdapter(adapter);
            Toast.makeText(getApplication(), getString(R.string.meal)+" "+getString(R.string.deleted) + temporaryStorageMeal.getId(),Toast.LENGTH_LONG).show();

        }
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
