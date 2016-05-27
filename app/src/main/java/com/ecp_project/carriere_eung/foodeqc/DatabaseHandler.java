package com.ecp_project.carriere_eung.foodeqc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.ecp_project.carriere_eung.foodeqc.Entity.Item;
import com.ecp_project.carriere_eung.foodeqc.Entity.ItemType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthieu on 25/05/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    //Database name and tables
    private static final String DATABASE_NAME = "FoodEqC Database";
    private static final String DATABASE_TABLE_ITEM = "items";
    private static final int DATABASE_VERSION = 1;

    //Rows name for item table
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_CO2_EQUIVALENT = "co2_equivalent";
    public static final String KEY_TYPE = "type";

    private static final String TAG = "DBAdapter";
    private Context context;


    //SQL command to create item table
    private static final String DATABASE_CREATE_ITEM =
            "create table items (_id integer primary key autoincrement, "
                    + "name text not null, co2_equivalent text not null, "
                    + "type text not null);";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ITEM);

        // Create tables again
        onCreate(db);
    }

    //Adding new item
    public boolean addItem(Item item){

        boolean createSuccessful = false;

        if (!checksIfExist(item)){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_NAME,item.getName());
            //je vais rentrer un double alors que la table c'est un string, quid de ce qui va se passer ?
            values.put(KEY_CO2_EQUIVALENT,String.valueOf(item.getCo2Equivalent()));
            values.put(KEY_TYPE,item.getType().toString());

            createSuccessful = db.insert(DATABASE_NAME, null, values) > 0;
        } else {
            Toast.makeText(context,R.string.item_alreaydy_on_db,Toast.LENGTH_LONG).show();
        }
        return createSuccessful;
    }

    //item type à modifier
    public List<Item> getAllContact(){
        List<Item> itemList = new ArrayList<Item>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE_ITEM;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(cursor.getString(1),Double.parseDouble(cursor.getString(2)), ItemType.local);
                // Adding contact to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        // return contact list
        return itemList;
    }

    public boolean checksIfExist(Item item){
        boolean recordExists = false;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_ROWID + " FROM " + DATABASE_TABLE_ITEM + " WHERE " + KEY_NAME + " = '" + item.getName() + "'", null);

        if(cursor!=null) {

            if(cursor.getCount()>0) {
                recordExists = true;
            }
        }

        cursor.close();
        db.close();
        return recordExists;
    }

    public SQLiteDatabase openWDb(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db;
    }
}
