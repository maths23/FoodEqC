package com.ecp_project.carriere_eung.foodeqc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.ecp_project.carriere_eung.foodeqc.Entity.Item;
import com.ecp_project.carriere_eung.foodeqc.Entity.ItemType;

import java.security.InvalidParameterException;
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

    public DatabaseHandler(Context contextArg) {
        super(contextArg, DATABASE_NAME, null, DATABASE_VERSION);
        context = contextArg;
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
    //DANGER : crash dès qu'on met un "'" dans le nom ^^
    public boolean addItem(Item item){

        boolean createSuccessful = false;

        if (!checksIfExist(item)){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_NAME,item.getName());
            values.put(KEY_CO2_EQUIVALENT,String.valueOf(item.getCo2Equivalent()));
            values.put(KEY_TYPE,item.getType().toString());

            createSuccessful = db.insert(DATABASE_TABLE_ITEM, null, values) > 0;
            db.close();
            if(createSuccessful){
                Log.e(TAG, item.getName() + " created.");
            }
        } else {
            //à remettre dès que prb résolu !
            //Toast.makeText(context,R.string.item_alreaydy_on_db,Toast.LENGTH_LONG).show();
        }

        return createSuccessful;
    }

    //return all items contained in the table "items" of the database
    public List<Item> getAllItems(){
        List<Item> itemList = new ArrayList<Item>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE_ITEM;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(Integer.parseInt(cursor.getString(0)),cursor.getString(1),Double.parseDouble(cursor.getString(2)), ItemType.toItemType(cursor.getString(3)));
                // Adding contact to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        // return contact list
        db.close();
        return itemList;
    }

    public boolean checksIfExist(Item item){
        return checksIfNameExist(item.getName());
    }

    public boolean checksIfNameExist(String name){
        boolean recordExists = false;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_ROWID + " FROM " + DATABASE_TABLE_ITEM + " WHERE " + KEY_NAME + " = '" + name + "'", null);

        if(cursor!=null) {

            if(cursor.getCount()>0) {
                recordExists = true;
            }
        }

        cursor.close();
        db.close();
        return recordExists;
    }

    public Item getItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DATABASE_TABLE_ITEM, new String[] {KEY_ROWID, KEY_NAME,
                        KEY_CO2_EQUIVALENT, KEY_TYPE }, KEY_ROWID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        // return contact
        return new Item(Integer.parseInt(cursor.getString(0)),cursor.getString(1),
                Double.parseDouble(cursor.getString(2)),ItemType.toItemType(cursor.getString(3)));
    }

    // Getting items Count
    public int getItemsCount() {

        String countQuery = "SELECT  * FROM " + DATABASE_TABLE_ITEM;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;

    }

    // Updating single contact
    //Requieres a contact with an id from the database !
    public int updateItem(Item item) throws InvalidParameterException {

        if (item.getId()<=-1){
            throw new InvalidParameterException("updateContact must be used on a contact instanciated from the database");
        }
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, item.getName());
        values.put(KEY_CO2_EQUIVALENT, String.valueOf(item.getCo2Equivalent()));
        values.put(KEY_TYPE,item.getType().toString());

        // updating row
        return db.update(DATABASE_TABLE_ITEM, values, KEY_ROWID + " = ?",
                new String[] { String.valueOf(item.getId()) });
    }

    // Deleting single contact
    public void deleteItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE_ITEM, KEY_ROWID + " = ?",
                new String[] { String.valueOf(item.getId()) });
        db.close();
    }
     //must be used with a name which is in the database
    public int getIdFromCompleteName(String name){

        int returnValue = -2;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_ROWID + " FROM " + DATABASE_TABLE_ITEM + " WHERE " + KEY_NAME + " = '" + name + "'", null);

        if(cursor!=null) {

            if(cursor.getCount()>0) {
                cursor.moveToFirst();
               returnValue = Integer.parseInt(cursor.getString(0));
            }
        }

        cursor.close();
        db.close();
        return returnValue;
    }

    //must be used with a name which is in the database
    public Item getItem(String name){
        return getItem(getIdFromCompleteName(name));
    }

    public List<Item> queryItemFromName(String name){

        SQLiteDatabase db = this.getWritableDatabase();
        List<Item> items = new ArrayList<Item>();

        // select query
        String sql = "";
        sql += "SELECT * FROM " + DATABASE_TABLE_ITEM;
        sql += " WHERE " + KEY_NAME + " LIKE '%" + name + "%'";
        sql += " ORDER BY " + KEY_NAME + " DESC";
        sql += " LIMIT 0,5";
        // execute the query
        Cursor cursor = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                // int productId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(fieldProductId)));

                Item item = new Item(Integer.parseInt(cursor.getString(0)),cursor.getString(1),
                        Double.parseDouble(cursor.getString(2)),ItemType.toItemType(cursor.getString(3)));
                // add to list
                items.add(item);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return items;
    }

    public String[] getItemsFromDb(String searchTerm) {

        SQLiteDatabase db = this.getWritableDatabase();
        // add items on the array dynamically
        List<Item> items = this.queryItemFromName(searchTerm);
        int rowCount = items.size();

        String[] item = new String[rowCount];
        int x = 0;

        for (Item record : items) {

            item[x] = record.getName();
            x++;
        }
        db.close();
        return item;
    }



}
