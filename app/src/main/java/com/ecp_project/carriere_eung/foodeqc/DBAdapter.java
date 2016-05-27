package com.ecp_project.carriere_eung.foodeqc;

/**
 * Created by Matthieu on 24/05/2016.

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_CO2_EQUIVALENT = "co2_equivalent";
    public static final String KEY_TYPE = "type";
    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "FoodEqC Database";
    private static final String DATABASE_TABLE_ITEM = "items";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
            "create table items (_id integer primary key autoincrement, "
                    + "name text not null, co2_equivalent text not null, "
                    + "type text not null);";

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    private final Context context;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    //---opens the database---
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close() {
        DBHelper.close();
    }

    //---insert a title into the database---
    public long insertTitle(String name, String co2Equivalent, String type) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_CO2_EQUIVALENT, co2Equivalent);
        initialValues.put(KEY_TYPE, type);
        return db.insert(DATABASE_TABLE_ITEM, null, initialValues);
    }

    //---deletes a particular title---
    public boolean deleteTitle(long rowId) {
        return db.delete(DATABASE_TABLE_ITEM, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---retrieves all the titles---
    public Cursor getAllTitles() {
        return db.query(DATABASE_TABLE_ITEM, new String[]{
                        KEY_ROWID,
                        KEY_NAME,
                        KEY_CO2_EQUIVALENT,
                        KEY_TYPE},
                null,
                null,
                null,
                null,
                null);
    }

    //---retrieves a particular title---
    public Cursor getTitle(long rowId) throws SQLException {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE_ITEM, new String[]{
                                KEY_ROWID,
                                KEY_NAME,
                                KEY_CO2_EQUIVALENT,
                                KEY_TYPE
                        },
                        KEY_ROWID + "=" + rowId,
                        null,
                        null,
                        null,
                        null,
                        null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a title---
    public boolean updateItem(long rowId, String isbn,
                              String title, String publisher) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, isbn);
        args.put(KEY_CO2_EQUIVALENT, title);
        args.put(KEY_TYPE, publisher);
        return db.update(DATABASE_TABLE_ITEM, args,
                KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean checksIfExist(String itemName){
        boolean itemExists = false;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion
                    + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS titles");
            onCreate(db);
        }
    }
}
 */