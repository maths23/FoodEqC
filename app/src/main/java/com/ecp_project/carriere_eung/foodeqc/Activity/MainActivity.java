package com.ecp_project.carriere_eung.foodeqc.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ecp_project.carriere_eung.foodeqc.AuxiliaryMethods.MenuHandler;
import com.ecp_project.carriere_eung.foodeqc.DatabaseHandler;
import com.ecp_project.carriere_eung.foodeqc.Entity.Item;
import com.ecp_project.carriere_eung.foodeqc.Entity.ItemContainer;
import com.ecp_project.carriere_eung.foodeqc.Entity.ItemType;
import com.ecp_project.carriere_eung.foodeqc.Entity.Repas;
import com.ecp_project.carriere_eung.foodeqc.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button manageItem;
    Button createRepas;
    Button showMeals;
    Button statistics;
    DatabaseHandler db;


    TextView textViewTodayEmission;
    ProgressBar progressBarTodayEmission;
    int average_emission_per_day;
    int maxProgressBarTodayEmission;
    int today_emission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        publishItemsInDatabase();



        createRepas = (Button)findViewById(R.id.buttonCreateRepasMain);
        createRepas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddNewRepasTypeActivity.class);
                startActivity(intent);
            }
        });

        showMeals = (Button)findViewById(R.id.buttonShowMeals);
        showMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ShowAllMealsActivity.class);
                startActivity(intent);
            }
        });

        manageItem = (Button)findViewById(R.id.buttonManageItems);
        manageItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ManageItemActivity.class);
                startActivity(intent);
            }
        });

        statistics = (Button)findViewById(R.id.buttonStatistics);
        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),StatisticsActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        String message = intent.getStringExtra(AddNewRepasActivity.EXTRA_MESSAGE);
        if (message != null) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

        }


        progressBarTodayEmission = (ProgressBar)findViewById(R.id.progressBarTodayEmission);
        average_emission_per_day = Integer.parseInt(getString(R.string.average_emission_per_day));
        maxProgressBarTodayEmission = (average_emission_per_day*4)/3;
        progressBarTodayEmission.setMax(maxProgressBarTodayEmission);

        today_emission = (int)db.getTodayCO2Equivalent();

        textViewTodayEmission = (TextView)findViewById(R.id.textViewTodayEmission);
        textViewTodayEmission.setText(String.valueOf(today_emission) + " g EqC");


        if (today_emission <= maxProgressBarTodayEmission*6/10) {
            progressBarTodayEmission.setProgress(today_emission);
            textViewTodayEmission.setTextColor(getResources().getColor(R.color.progressBarGreen));
        }
        else if (today_emission <= maxProgressBarTodayEmission*8/10) {
            progressBarTodayEmission.setProgress(today_emission);
            textViewTodayEmission.setTextColor(getResources().getColor(R.color.progressBarOrange));
        }
        else {
            progressBarTodayEmission.setProgress(maxProgressBarTodayEmission);
            textViewTodayEmission.setTextColor(getResources().getColor(R.color.progressBarRed));

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

    private double getEmissionPerDayAverage() {
        List<Repas> repasList = db.getAllRepas();
        int number_of_day = 0;
        double sumCO2Equivalent = 0;
        GregorianCalendar latestDate = new GregorianCalendar();
        latestDate.clear(Calendar.HOUR_OF_DAY);
        latestDate.clear(Calendar.MINUTE);
        latestDate.clear(Calendar.SECOND);
        latestDate.clear(Calendar.MILLISECOND);

        latestDate.set(Calendar.HOUR_OF_DAY, 0);
        latestDate.set(Calendar.MINUTE,0);
        latestDate.set(Calendar.SECOND,0);
        latestDate.set(Calendar.MILLISECOND,0);

        long time = latestDate.getTimeInMillis();

        for (Repas repas:repasList
                ) {
            if (repas.getDate().getTimeInMillis() < time) {
                time -= 1000*60*60*24;
                number_of_day +=1;
            }
            sumCO2Equivalent += repas.getCo2Equivalent();
        }
        return sumCO2Equivalent/number_of_day;
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
