package com.ecp_project.carriere_eung.foodeqc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecp_project.carriere_eung.foodeqc.Adapter.ItemRepasAdapter;
import com.ecp_project.carriere_eung.foodeqc.Entity.Item;
import com.ecp_project.carriere_eung.foodeqc.Entity.ItemRepas;
import com.ecp_project.carriere_eung.foodeqc.Entity.ItemType;
import com.ecp_project.carriere_eung.foodeqc.Entity.Repas;
import com.ecp_project.carriere_eung.foodeqc.Entity.RepasType;
import com.ecp_project.carriere_eung.foodeqc.Exception.ItemNotFoundException;

import java.util.GregorianCalendar;

/**
 * Created by eung on 21/05/16.
 * Allow the user to had a meal.
 */
public class AddNewRepasActivity extends AppCompatActivity {
    private static final int DIALOG_ALERT_DELETE = 100;
    private ItemRepas temporaryStorageItemRepas;

    final GregorianCalendar c = new GregorianCalendar();
    ItemRepasAdapter adapter;

    RepasType repasType;
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
        initializeAdapter();
        listViewRepasItem.setAdapter(adapter);

        listViewRepasItem.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                temporaryStorageItemRepas = repas.getElements().get(position);
                createAlertDialogDeleteItem();
                return true;
            }
        });

        listViewRepasItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                temporaryStorageItemRepas = repas.getElements().get(position);
                AlertDialog.Builder alert = new AlertDialog.Builder(AddNewRepasActivity.this);
                alert.setTitle(R.string.set_weight_title); //Set Alert dialog title here

                // Set an EditText view to get user input
                final EditText input = new EditText(AddNewRepasActivity.this);
                input.setText(String.valueOf(temporaryStorageItemRepas.getPoids()));
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(input);

                alert.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //You will get as string input data in this variable.
                        // here we convert the input to a string and show in a toast.
                        int weight = Integer.parseInt(input.getEditableText().toString());
                        temporaryStorageItemRepas.setPoids(weight);
                        listViewRepasItem.setAdapter(adapter);
                        Toast.makeText(AddNewRepasActivity.this,temporaryStorageItemRepas.getItem().getName()+getString(R.string.item_repas_weigth_set)+weight,Toast.LENGTH_LONG).show();
                    }
                });

                alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });





    }

    public void initializeAdapter() {
        adapter = new ItemRepasAdapter(AddNewRepasActivity.this,repas.getElements());

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

        ItemRepas itemRepas1 = new ItemRepas(repas,item1,100);
        ItemRepas itemRepas2 = new ItemRepas(repas,item2,100);
        ItemRepas itemRepas3 = new ItemRepas(repas,item3,100);
        ItemRepas itemRepas4 = new ItemRepas(repas,item4,100);
        ItemRepas itemRepas5 = new ItemRepas(repas,item5,100);
        ItemRepas itemRepas6 = new ItemRepas(repas,item6,100);
        ItemRepas itemRepas7 = new ItemRepas(repas,item7,100);
        ItemRepas itemRepas8 = new ItemRepas(repas,item8,100);
        ItemRepas itemRepas9 = new ItemRepas(repas,item9,100);
        ItemRepas itemRepas10 = new ItemRepas(repas,item10,100);
        ItemRepas itemRepas11 = new ItemRepas(repas,item11,100);

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


    public void createAlertDialogDeleteItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alertDialogDeleteItemRepas);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.delete_item_repas, new OkOnClickListener());
        builder.setNegativeButton(R.string.cancel_delete_item_repas, new CancelOnClickListener());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_ALERT_DELETE:

        }
        return super.onCreateDialog(id);
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
            try {
                repas.removeElement(temporaryStorageItemRepas);
                listViewRepasItem.setAdapter(adapter);
                Toast.makeText(getApplication(), temporaryStorageItemRepas.getItem().getName()+getString(R.string.item_repas_deleted),Toast.LENGTH_LONG).show();
            } catch (ItemNotFoundException e) {
                Toast.makeText(getApplication(), getString(R.string.item_not_found_error),Toast.LENGTH_LONG).show();
            }
        }
    }
}

