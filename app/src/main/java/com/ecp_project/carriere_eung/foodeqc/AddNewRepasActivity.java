package com.ecp_project.carriere_eung.foodeqc;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ecp_project.carriere_eung.foodeqc.Adapter.ItemRepasAdapter;
import com.ecp_project.carriere_eung.foodeqc.Entity.Item;
import com.ecp_project.carriere_eung.foodeqc.Entity.ItemRepas;
import com.ecp_project.carriere_eung.foodeqc.Entity.ItemType;
import com.ecp_project.carriere_eung.foodeqc.Entity.Repas;
import com.ecp_project.carriere_eung.foodeqc.Entity.RepasType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by eung on 21/05/16.
 * Allow the user to had a meal.
 */
public class AddNewRepasActivity extends AppCompatActivity {
    RepasType repasType;


    final GregorianCalendar c = new GregorianCalendar();
    Repas repas = new Repas(c,repasType);

    TextView tvCurrentDate;
    TextView tvRepasType;
    EditText editTextAddRepasType;
    EditText editTextAddRepasTypeWeight;
    ListView listViewRepasItem;
    Button btnAddRepasItem;
    Button btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_repas);


        genereItemRepasList();
        repasType = (RepasType)getIntent().getExtras().get("RepasType");

        tvCurrentDate = (TextView)findViewById(R.id.tvCurrentDate);
        tvCurrentDate.setText(new StringBuilder().append("Le ").append(c.get(GregorianCalendar.DAY_OF_MONTH)).append("/").append(c.get(GregorianCalendar.MONTH) + 1).append("/").append(c.get(GregorianCalendar.YEAR)).append(" à ").append(c.get(GregorianCalendar.HOUR) + c.get(GregorianCalendar.AM_PM)).append(":").append(c.get(GregorianCalendar.MINUTE)));

        tvRepasType = (TextView)findViewById(R.id.tvRepasType);
        tvRepasType.setText(stringRepasType(repasType));

        listViewRepasItem = (ListView)findViewById(R.id.listViewRepasItem);
        ItemRepasAdapter adapter = new ItemRepasAdapter(AddNewRepasActivity.this,repas.getElements());
        listViewRepasItem.setAdapter(adapter);




    }

    public String stringRepasType(RepasType repasType) {
        String result = "";
        switch (repasType) {
            case breakfast:
                result = this.getString(R.string.break_fast);
                break;
            case lunch:
                result = this.getString(R.string.lunch);
                break;
            case dinner:
                result = this.getString(R.string.dinner);
                break;
            case snack:
                result = this.getString(R.string.snack);
                break;
        }
        return result;
    }

    public void genereItemRepasList() {
        Item item1 = new Item("Item 1",10, ItemType.base);
        Item item2 = new Item("Item 2",10, ItemType.base);
        Item item3 = new Item("Item 3",10, ItemType.base);
        Item item4 = new Item("Item 4",10, ItemType.base);
        Item item5 = new Item("Item 5",10, ItemType.base);
        Item item6 = new Item("Item 6",10, ItemType.base);
        Item item7 = new Item("Item 7",10, ItemType.base);
        Item item8 = new Item("Item 8",10, ItemType.base);
        Item item9 = new Item("Item 9",10, ItemType.base);
        Item item10 = new Item("Item 10",10, ItemType.base);
        Item item11 = new Item("Item 11",10, ItemType.base);

        ItemRepas itemRepas1 = new ItemRepas(item1,100);
        ItemRepas itemRepas2 = new ItemRepas(item2,100);
        ItemRepas itemRepas3 = new ItemRepas(item3,100);
        ItemRepas itemRepas4 = new ItemRepas(item4,100);
        ItemRepas itemRepas5 = new ItemRepas(item5,100);
        ItemRepas itemRepas6 = new ItemRepas(item6,100);
        ItemRepas itemRepas7 = new ItemRepas(item7,100);
        ItemRepas itemRepas8 = new ItemRepas(item8,100);
        ItemRepas itemRepas9 = new ItemRepas(item9,100);
        ItemRepas itemRepas10 = new ItemRepas(item10,100);
        ItemRepas itemRepas11 = new ItemRepas(item11,100);

        repas.addElement(itemRepas1);
        repas.addElement(itemRepas2);
        repas.addElement(itemRepas3);
        repas.addElement(itemRepas4);
        repas.addElement(itemRepas5);
        repas.addElement(itemRepas6);
        repas.addElement(itemRepas7);
        repas.addElement(itemRepas8);
        repas.addElement(itemRepas9);
        repas.addElement(itemRepas10);
        repas.addElement(itemRepas11);
    }


}
