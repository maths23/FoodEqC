package com.ecp_project.carriere_eung.foodeqc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.ecp_project.carriere_eung.foodeqc.Entity.ComposedItem;
import com.ecp_project.carriere_eung.foodeqc.Entity.Ingredient;
import com.ecp_project.carriere_eung.foodeqc.Entity.Item;
import com.ecp_project.carriere_eung.foodeqc.Entity.ItemType;
import com.ecp_project.carriere_eung.foodeqc.Entity.ProcessingCost;
import com.ecp_project.carriere_eung.foodeqc.MainActivity;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthieu on 25/05/2016.
 * Contains all methods to use the SQLite local database.
 * Every item can be identified by its name : no duplicate allowed
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    //Database name and tables
    private static final String DATABASE_NAME = "FoodEqC Database";
    private static final String DATABASE_TABLE_ITEM = "items";
    private static final String DATABASE_TABLE_CITEM = "composedItems";
    private static final String DATABASE_TABLE_INGREDIENT_RELATION = "ingredients";
    private static final int DATABASE_VERSION = 1;

    //Rows name for item table
    public static final String KEY_ITEMS_ROWID = "_id";
    public static final String KEY_ITEMS_NAME = "name";
    public static final String KEY_ITEMS_CO2_EQUIVALENT = "co2_equivalent";
    public static final String KEY_ITEMS_TYPE = "type";
    public static final String KEY_ITEM_PROCESS_COST = "pcost";

    //Rows name for composedItem table
    public static final String KEY_CITEMS_ROWID = "_id";
    public static final String KEY_CITEMS_NAME = "name";
    public static final String KEY_CITEMS_CO2_EQUIVALENT = "co2_equivalent";
    public static final String KEY_CITEMS_TYPE = "type";

    //Rows name for ingredient table
    public static final String KEY_INGREDIENT_ROWID = "_id";
    public static final String KEY_INGREDIENT_ITEM_ID = "item_id";
    public static final String KEY_INGREDIENT_CITEM_NAME = "citem_name";
    public static final String KEY_INGREDIENT_PROPORTION = "proportion";


    private static final String TAG = "DBHandler";
    private Context context;


    //SQL command to create item table
    private static final String DATABASE_CREATE_ITEM =
            "create table items (_id integer primary key autoincrement, "
                    + "name text not null, co2_equivalent real not null, "
                    + "type text not null, " + "pcost real default 1);";

    //SQL command to create Composed items table
    public static final String DATABASE_CREATE_CITEM =
            "create table composedItems (_id integer primary key autoincrement, "
                    + "name text not null, co2_equivalent real not null, "
                    + "type text not null);";

    //SQL command to create ingredient items table
    public static final String DATABASE_CREATE_INGREDIENT =
            "create table ingredients (_id integer primary key autoincrement, "
                    + "item_id integer not null, citem_name text not null, "
                    + "proportion real not null);";


    public DatabaseHandler(Context contextArg) {
        super(contextArg, DATABASE_NAME, null, DATABASE_VERSION);
        context = contextArg;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DATABASE_CREATE_ITEM);
        db.execSQL(DATABASE_CREATE_INGREDIENT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ITEM);

        // Create tables again
        onCreate(db);
    }

    public void restaureBaseDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ITEM);

        // Create tables again
        onCreate(db);
    }

    // ------------------------------- "Items" Table Methods --------------------------------- //


    /**
     * Adding new item
     *
     * @param item
     * @return true si l'ajout est réussi, faux sinon
     * DANGER : crash dès qu'on met un "'" dans le nom ^^
     */
    public boolean addItem(Item item) {

        boolean createSuccessful = false;
        boolean createRelationSuccessful = true;

        if (!checksIfItemExists(item)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_ITEMS_NAME, item.getName());
            values.put(KEY_ITEMS_CO2_EQUIVALENT, item.getCo2Equivalent());
            values.put(KEY_ITEMS_TYPE, item.getType().toString());


            //create item-composedItem relationships in ingredient table
            //both conditions are supposed to be actually exactely the same
            if (item.getType() != ItemType.base && item instanceof ComposedItem) {
                ComposedItem citem = (ComposedItem) item;
                values.put(KEY_ITEM_PROCESS_COST, citem.getCost().getMalusFactor());
                String citemName = citem.getName();
                for (Ingredient ingredient : citem.getIngredients()) {
                    ContentValues valuesIngredient = new ContentValues();
                    int itemId = ingredient.getItem().getId();
                    valuesIngredient.put(KEY_INGREDIENT_ITEM_ID, itemId);
                    valuesIngredient.put(KEY_INGREDIENT_CITEM_NAME, citemName);
                    valuesIngredient.put(KEY_INGREDIENT_PROPORTION, ingredient.getProportion());


                    boolean success = db.insert(DATABASE_TABLE_INGREDIENT_RELATION, null, valuesIngredient) > 0;
                    if (success) {
                        Log.e(TAG, ingredient.getName() + " has been successfully added to " + item.getName() + " list if ingredient");
                    } else {
                        createRelationSuccessful = false;
                    }
                }
            }
            createSuccessful = db.insert(DATABASE_TABLE_ITEM, null, values) > 0;
            db.close();
            if (createSuccessful) {
                Log.e(TAG, item.getName() + " created.");
            }
        } else {
            //à remettre dès que prb résolu !
            //Toast.makeText(context,R.string.item_alreaydy_on_db,Toast.LENGTH_LONG).show();
        }

        return createSuccessful && createRelationSuccessful;
    }


    /**
     * @return return all items contained in the table "items" of the database
     */
    public List<Item> getAllItems() {
        List<Item> itemList = new ArrayList<Item>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE_ITEM + " ORDER BY " + KEY_ITEMS_NAME ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(3).equals(ItemType.base.toString())) {
                    Item item = getItem(cursor.getInt(0));
                    // Adding contact to list
                    itemList.add(item);
                } else {
                    ComposedItem citem = (ComposedItem) getItem(cursor.getInt(0));
                    itemList.add(citem);
                }

            } while (cursor.moveToNext());
        }
        // return contact list
        db.close();
        return itemList;
    }

    public Item getItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DATABASE_TABLE_ITEM, new String[]{KEY_ITEMS_ROWID, KEY_ITEMS_NAME,
                        KEY_ITEMS_CO2_EQUIVALENT, KEY_ITEMS_TYPE}, KEY_ITEMS_ROWID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Log.e("DBHandler",""+cursor.getCount());
        String itemName = cursor.getString(1);

        if (cursor.getString(3).equals(ItemType.base.toString())) {
            return new Item(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                    cursor.getDouble(2), ItemType.toItemType(cursor.getString(3)));
        } else {
            ArrayList<Ingredient> ingredients = new ArrayList<>();
            List<Integer> relationId = getIngredientsIdFromName(itemName);
            for (int value : relationId) {
                ingredients.add(getIngredientFromId(value));
            }
            if (cursor.getString(1).equals("thon aux carottes")){
                Log.e("naming", "c'est aussi ça");
            }
            return new ComposedItem(Integer.parseInt(cursor.getString(0)),cursor.getString(1), ItemType.toItemType(cursor.getString(3)), ingredients,
                    ProcessingCost.toProcessingCost(cursor.getDouble(2)));
        }


    }

    public List<Item> getAllItems(ItemType type){
        List<Item> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery;
        switch (type){
            case base:
                 selectQuery = "SELECT * FROM " + DATABASE_TABLE_ITEM + " WHERE "
                        + KEY_ITEMS_TYPE + " = '" + ItemType.base.toString() + "'" + " ORDER BY " + KEY_ITEMS_NAME;
                // looping through all rows and adding to list
                Cursor cursor = db.rawQuery(selectQuery,null);
                if (cursor.moveToFirst()) {
                    do {
                        Item item = getItem(cursor.getInt(0));
                        // Adding item to list
                        list.add(item);
                    } while (cursor.moveToNext());
                }
                // return contact list
                db.close();
                break;
            case local:
               selectQuery = "SELECT * FROM " + DATABASE_TABLE_ITEM + " WHERE "
                        + KEY_ITEMS_TYPE + " = '" + ItemType.local.toString() + "'" + " ORDER BY " + KEY_ITEMS_NAME;
                // looping through all rows and adding to list
                cursor = db.rawQuery(selectQuery,null);
                if (cursor.moveToFirst()) {
                    do {
                        ComposedItem citem = (ComposedItem) getItem(cursor.getInt(0));
                        list.add(citem);
                    } while (cursor.moveToNext());
                }
                // return contact list
                db.close();
                break;
            case imported:
                selectQuery = "SELECT * FROM " + DATABASE_TABLE_ITEM + " WHERE "
                        + KEY_ITEMS_TYPE + " = '" + ItemType.imported.toString() + "'";
                // looping through all rows and adding to list
                cursor = db.rawQuery(selectQuery,null);
                if (cursor.moveToFirst()) {
                    do {
                        ComposedItem citem = (ComposedItem) getItem(cursor.getInt(0));
                        list.add(citem);
                    } while (cursor.moveToNext());
                }
                // return contact list
                db.close();
                break;


        }
        return list;
    }

    /**
     * Updating single item
     * Requieres an item with an id from the database !
     * Do not convert an item to composedItem or the oppositve
     *
     * @param item
     * @return
     * @throws InvalidParameterException
     */
    public int updateItem(Item item) throws InvalidParameterException {
        Boolean UpdateItemSuccess = true;
        Boolean updateRelationSuccess = true;
        if (item.getId() <= -1) {
            throw new InvalidParameterException("updateItem must be used on a contact instanciated from the database");
        }
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEMS_NAME, item.getName());
        values.put(KEY_ITEMS_CO2_EQUIVALENT, item.getCo2Equivalent());
        values.put(KEY_ITEMS_TYPE, item.getType().toString());

        if(item instanceof ComposedItem){
            ComposedItem citem = (ComposedItem) item;
            values.put(KEY_ITEM_PROCESS_COST, citem.getCost().getMalusFactor());
            String citemName = citem.getName();

            //updating ingredients relationship

            //checking if any local item has been removed from the ingredients list
            List<Integer> newBaseItemIds = new ArrayList<>();
            for (Ingredient ingredient : citem.getIngredients()){
                newBaseItemIds.add(ingredient.getItem().getId());
            }
            List<Integer> formerIngredientIds = getIngredientsIdOfFromID(citem.getId());
            for (int id:formerIngredientIds) {
                if (!newBaseItemIds.contains(getIngredientFromId(id).getItem().getId())){
                    deleteIngredient(getIngredientFromId(id));
                }
            }

            //adding new ingredients or updating already existing ones.
            for (Ingredient ingredient : citem.getIngredients()) {
                ContentValues valuesIngredient = new ContentValues();
                int itemId = ingredient.getItem().getId();
                valuesIngredient.put(KEY_INGREDIENT_ITEM_ID, itemId);
                valuesIngredient.put(KEY_INGREDIENT_CITEM_NAME, citemName);
                valuesIngredient.put(KEY_INGREDIENT_PROPORTION, ingredient.getProportion());
                if (checkIfRelationExists(ingredient.getItem().getId(), citem.getId())) {
                    db.update(DATABASE_TABLE_INGREDIENT_RELATION, valuesIngredient, KEY_INGREDIENT_ROWID + " = ?",
                            new String[]{String.valueOf(ingredient.getId())});
                } else {
                    boolean success = db.insert(DATABASE_TABLE_INGREDIENT_RELATION, null, valuesIngredient) > 0;
                    if (success) {
                        Log.e(TAG, ingredient.getName() + " has been successfully added to " + item.getName() + " list if ingredient");
                    }
                }
            }
        }

        // updating row
        return db.update(DATABASE_TABLE_ITEM, values, KEY_ITEMS_ROWID + " = ?",
                new String[]{String.valueOf(item.getId())});
    }

    // Deleting single contact
    public void deleteItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        int id = item.getId();
        if (item instanceof ComposedItem){
            List<Integer> toBeDeleted = getIngredientsIdOfFromID(id);
            for (int idToBeDeleted:toBeDeleted){
                deleteIngredients(idToBeDeleted);
            }
        } else {
            List<Integer> toBeDeleted = getIngredientIdUsingItemId(id);
            for (int idToBeDeleted:toBeDeleted){
                deleteIngredients(idToBeDeleted);
            }
        }
        db.delete(DATABASE_TABLE_ITEM, KEY_ITEMS_ROWID + " = ?",
                new String[]{String.valueOf(item.getId())});

        db.close();
    }

    public void deleteAllItem(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE_ITEM, KEY_ITEMS_ROWID + " = ?",
                new String[]{null});
    }

    /**
     * @param item
     * @return true if the item is in the database
     */
    public boolean checksIfItemExists(Item item) {
        return checksIfItemNameExists(item.getName());
    }

    public boolean checksIfItemNameExists(String name) {
        boolean recordExists = false;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_ITEMS_ROWID + " FROM " + DATABASE_TABLE_ITEM + " WHERE " + KEY_ITEMS_NAME + " = '" + name + "'", null);

        if (cursor != null) {

            if (cursor.getCount() > 0) {
                recordExists = true;
            }
        }

        cursor.close();
        db.close();
        return recordExists;
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


    /**
     * @param name
     * @return the id of the unique item whose name matches parameter.If there is none, -2 will be returned.
     * must be used with a name which is in the database
     */
    public int getItemIdFromCompleteName(String name) {

        int returnValue = -2;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_ITEMS_ROWID + " FROM " + DATABASE_TABLE_ITEM + " WHERE " + KEY_ITEMS_NAME + " = '" + name + "'", null);

        if (cursor != null) {

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                returnValue = Integer.parseInt(cursor.getString(0));
            }
        }

        cursor.close();
        db.close();
        return returnValue;
    }

    //must be used with a name which is in the database
    public Item getItem(String name) {
        return getItem(getItemIdFromCompleteName(name));
    }

    public List<Item> queryItemFromName(String name) {

        SQLiteDatabase db = this.getWritableDatabase();
        List<Item> items = new ArrayList<Item>();

        // select query
        String sql = "";
        sql += "SELECT * FROM " + DATABASE_TABLE_ITEM;
        sql += " WHERE " + KEY_ITEMS_NAME + " LIKE '%" + name + "%'";
        sql += " ORDER BY " + KEY_ITEMS_NAME + " DESC";
        sql += " LIMIT 0,5";
        // execute the query
        Cursor cursor = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                // int productId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(fieldProductId)));

                Item item = new Item(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                        cursor.getDouble(2), ItemType.toItemType(cursor.getString(3)));
                // add to list
                items.add(item);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return items;
    }

    /**
     * @param searchTerm
     * @return a list of items whose name matches searchTerm
     * used for the autocompletion feature
     */
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

    /**
     * @param citem
     * @return the list of ingredients composing the composedItem.
     */
    public List<Ingredient> getIngredientsOfComposedItem(ComposedItem citem) {
        List<Ingredient> ingredientsList = new ArrayList<>();
        int citemId = getItemIdFromCompleteName(citem.getName());
        List<Integer> ingredientsIds = getIngredientsIdOfFromID(citemId);
        for (int id : ingredientsIds) {
            ingredientsList.add(getIngredientFromId(id));
        }
        return ingredientsList;
    }

    public String getPath(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.getPath();
    }

    // ------------------------------- "ingredient" Table Methods --------------------------------- //

    /**
     * add an ingredient-relation between an item and a composedItem
     *
     * @param item_id
     * @param citem_id
     * @param proportion : % of total mass
     * @return true if the relation was added, or if it already exist
     */
    public boolean addIngredientRelation(int item_id, int citem_id, double proportion) {
        boolean success = true;
        if (!checkIfRelationExists(item_id, citem_id)) {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_INGREDIENT_ITEM_ID, item_id);
            values.put(KEY_INGREDIENT_CITEM_NAME, citem_id);
            values.put(KEY_INGREDIENT_PROPORTION, proportion);

            success = db.insert(DATABASE_TABLE_INGREDIENT_RELATION, null, values) > 0;


        } else {
            //à remettre dès que prb résolu !
            //Toast.makeText(context,R.string.item_alreaydy_on_db,Toast.LENGTH_LONG).show();
        }
        return success;

    }

    /**
     * @param citem_id
     * @return the list of ingredients'Id for a composed item
     */
    public List<Integer> getIngredientsIdOfFromID(int citem_id) {
        List<Integer> idList = new ArrayList<>();
        String selectQuery = "SELECT " + KEY_INGREDIENT_ROWID + " FROM " + DATABASE_TABLE_INGREDIENT_RELATION + " WHERE "
                + KEY_INGREDIENT_CITEM_NAME + " = '" + getItem(citem_id).getName() + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                idList.add(cursor.getInt(0));

            } while (cursor.moveToNext());
        }
        // return contact list
        cursor.close();
        db.close();
        return idList;

    }

    /**
     *
     * @param citem_name
     * @return the list of the ids of the ingredients containing the composedItem
     */
    public List<Integer> getIngredientsIdFromName(String citem_name) {
        List<Integer> idList = new ArrayList<>();
        String selectQuery = "SELECT " + KEY_INGREDIENT_ROWID + " FROM " + DATABASE_TABLE_INGREDIENT_RELATION + " WHERE "
                + KEY_INGREDIENT_CITEM_NAME + " = '" + citem_name + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                idList.add(cursor.getInt(0));

            } while (cursor.moveToNext());
        }
        // return contact list
        cursor.close();
        db.close();
        return idList;

    }

    /**
     *
     * @param item_id
     * @return the list of ids of the ingredients containing the(simple) item which matches the item_id
     */
    public List<Integer> getIngredientIdUsingItemId(int item_id){
        List<Integer> idList = new ArrayList<>();
        String selectQuery = "SELECT " + KEY_INGREDIENT_ROWID + " FROM " + DATABASE_TABLE_INGREDIENT_RELATION + " WHERE "
                + KEY_INGREDIENT_ITEM_ID + " = '" + item_id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                idList.add(cursor.getInt(0));

            } while (cursor.moveToNext());
        }
        // return contact list
        cursor.close();
        db.close();
        return idList;
    }

    /**
     * @param id
     * @return the ingredient within the database that matches the id
     */
    public Ingredient getIngredientFromId(int id) {
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE_INGREDIENT_RELATION + " WHERE "
                + KEY_INGREDIENT_ROWID + " = '" + id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();
        return new Ingredient(cursor.getInt(0), getItem(cursor.getInt(1)), cursor.getString(2),
                cursor.getDouble(3));
    }


    public void deleteIngredient(Ingredient ing){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE_INGREDIENT_RELATION, KEY_INGREDIENT_ROWID + " = ?",
                new String[]{String.valueOf(ing.getId())});
        db.close();
    }

    public void deleteIngredients(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE_INGREDIENT_RELATION, KEY_INGREDIENT_ROWID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
    /**
     * check if an ingredient-relation between an item and a composedItem exists
     *
     * @param item_id
     * @param citem_id
     * @return
     */
    public boolean checkIfRelationExists(int item_id, int citem_id) {
        boolean recordExists = false;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_INGREDIENT_ROWID + " FROM " + DATABASE_TABLE_INGREDIENT_RELATION +
                        " WHERE " + KEY_INGREDIENT_ITEM_ID + " = '" + item_id + "'" + " AND " + KEY_INGREDIENT_CITEM_NAME +
                " = '" + getItem(citem_id).getName() + "'", null);

        if (cursor != null) {

            if (cursor.getCount() > 0) {
                recordExists = true;
            }
        }

        cursor.close();
        db.close();
        return recordExists;
    }
}

    // ------------------------------- "composedItems" Table Methods --------------------------------- //

    /**
     *Adding new item
     * @param
     * @return true si l'ajout est réussi, faux sinon
     * DANGER : crash dès qu'on met un "'" dans le nom ^^
     */
   /* public boolean addComposedItem(ComposedItem item){

        boolean createSuccessful = false;

        if (!checksIfItemExists(item)){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_CITEMS_NAME,item.getName());
            values.put(KEY_CITEMS_CO2_EQUIVALENT,item.getCo2Equivalent());
            values.put(KEY_CITEMS_TYPE,item.getType().toString());

            createSuccessful = db.insert(DATABASE_TABLE_CITEM, null, values) > 0;
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

    /**
     *
     * @return return all composedItems contained in the table "items" of the database
     */
