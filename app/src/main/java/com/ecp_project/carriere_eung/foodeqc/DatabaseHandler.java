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
import com.ecp_project.carriere_eung.foodeqc.Entity.ItemRepas;
import com.ecp_project.carriere_eung.foodeqc.Entity.ItemType;
import com.ecp_project.carriere_eung.foodeqc.Entity.ProcessingCost;

import com.ecp_project.carriere_eung.foodeqc.Entity.Repas;
import com.ecp_project.carriere_eung.foodeqc.Entity.RepasType;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
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
    private static final String DATABASE_TABLE_REPAS = "repas";
    private static final String DATABASE_TABLE_ITEM_REPAS = "item_repas";
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

    //Rows name for repas table
    public static final String KEY_REPAS_ROWID = "_id";
    public static final String KEY_REPAS_DATE = "date";
    public static final String KEY_REPAS_REPASTYPE = "repas_type";
    public static final String KEY_REPAS_CO2_EQUIVALENT = "co2_equivalent";

    //Rows name for item_repas table
    public static final String KEY_ITEM_REPAS_ROWID = "_id";
    public static final String KEY_ITEM_REPAS_REPAS_ID = "repas_id";
    public static final String KEY_ITEM_REPAS_ITEM_ID = "item_id";
    public static final String KEY_ITEM_REPAS_POIDS = "poids";



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

    //SQL command to create repas table
    public static final String DATABASE_CREATE_REPAS =
            "create table repas (_id integer primary key autoincrement, "
                    + KEY_REPAS_DATE+ " integer not null, "
                    + KEY_REPAS_REPASTYPE+ " text not null, "
                    + KEY_REPAS_CO2_EQUIVALENT+ " real not null);";

    public static final String DATABASE_CREATE_ITEM_REPAS =
            "create table item_repas (_id integer primary key autoincrement, "
                    + KEY_ITEM_REPAS_REPAS_ID+ " integer not null, "
                    + KEY_ITEM_REPAS_ITEM_ID+ " integer not null, "
                    + KEY_ITEM_REPAS_POIDS+ " integer not null);";


    public DatabaseHandler(Context contextArg) {
        super(contextArg, DATABASE_NAME, null, DATABASE_VERSION);
        context = contextArg;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DATABASE_CREATE_ITEM);
        db.execSQL(DATABASE_CREATE_INGREDIENT);
        db.execSQL(DATABASE_CREATE_REPAS);
        db.execSQL(DATABASE_CREATE_ITEM_REPAS);

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

            if (createSuccessful) {
                Log.e(TAG, item.getName() + " created.");
            }
        } else {

            Toast.makeText(context,R.string.item_alreaydy_on_db,Toast.LENGTH_LONG).show();
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

        return itemList;
    }

    /**
     *
     * @param id
     * @return the item/composedItem matching the id if there is one, null if there is none.
     */
    public Item getItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DATABASE_TABLE_ITEM, new String[]{KEY_ITEMS_ROWID, KEY_ITEMS_NAME,
                        KEY_ITEMS_CO2_EQUIVALENT, KEY_ITEMS_TYPE}, KEY_ITEMS_ROWID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null){
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

                return new ComposedItem(Integer.parseInt(cursor.getString(0)),cursor.getString(1), ItemType.toItemType(cursor.getString(3)), ingredients,
                        ProcessingCost.toProcessingCost(cursor.getDouble(2)));
            }
        } else {
            return null;
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
     * @return int
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

    /**
     * Delete an item, base or composed.
     * Warning : all corresponding itemItem (Ingredient) and ItemRepas relations are deleted
     * BUT composedItem and repas which contains the deleted item will NOT be deleted
     * This may lead to 1 or even 0 ingredients composedItems
     * or 0 item Repas
     * @param item
     */
    public void deleteItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        int id = item.getId();

        //deleting all related ingredients
        if (item instanceof ComposedItem){
            List<Integer> toBeDeleted = getIngredientsIdOfFromID(id);
            for (int idToBeDeleted:toBeDeleted){
                deleteIngredients(idToBeDeleted);
            }
        } else {
            List<Integer> toBeDeleted = getIngredientIdUsingBaseItemId(id);
            for (int idToBeDeleted:toBeDeleted){
                deleteIngredients(idToBeDeleted);
            }
        }
        db.delete(DATABASE_TABLE_ITEM, KEY_ITEMS_ROWID + " = ?",
                new String[]{String.valueOf(item.getId())});

        //deleting all related ItemRepas related
        List<Integer> itemRepasToBedeleted = getItemRepasIdFromItemId(id);
        for (int idToBeDeleted:itemRepasToBedeleted){
            deleteItemRepas(idToBeDeleted);
        }

    }

    public void deleteItem(int item_id){
        deleteItem(getItem(item_id));
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

        return returnValue;
    }

    //must be used with a name which is in the database
    public Item getItem(String name) {
        return getItem(getItemIdFromCompleteName(name));
    }

    /**
     * Used for autocompleteFeatures
     * All to get only base Item (to create composed items for instance)
     * or all items (to create a meal)
     * @param name
     * @param baseOnly
     * @return the list of items whose name matches the one prompted by the user (name parameter)
     * If baseOnly is true, only Base item will be returned
     */
    public List<Item> queryItemFromName(String name,Boolean baseOnly) {

        SQLiteDatabase db = this.getWritableDatabase();
        List<Item> items = new ArrayList<Item>();

        // select query
        String sql = "";
        sql += "SELECT * FROM " + DATABASE_TABLE_ITEM;
        sql += " WHERE " + KEY_ITEMS_NAME + " LIKE '%" + name + "%'";
        if (baseOnly){
            sql += " AND " + KEY_ITEMS_TYPE + " = '" + ItemType.base.toString() +"'";
        }
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
        //db.close();

        return items;
    }

    /**
     * @param searchTerm
     * @return a list of items whose name matches searchTerm
     * used for the autocompletion feature
     */
    public String[] getItemsFromDb(String searchTerm, Boolean baseOnly) {

        SQLiteDatabase db = this.getWritableDatabase();
        // add items on the array dynamically
        List<Item> items = this.queryItemFromName(searchTerm, baseOnly);
        int rowCount = items.size();

        String[] item = new String[rowCount];
        int x = 0;

        for (Item record : items) {

            item[x] = record.getName();
            x++;
        }
        //db.close();
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
        //db.close();
        return idList;

    }

    /**
     *
     * @param citem_name
     * @return the list of the ids of the ingredients composing the composedItem
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
        //db.close();
        return idList;

    }

    /**
     *
     * @param item_id
     * @return the list of ids of the ingredients containing the (simple) item which matches the item_id
     */
    public List<Integer> getIngredientIdUsingBaseItemId(int item_id){
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
        //db.close();
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
        //db.close();
    }


    public void deleteIngredients(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE_INGREDIENT_RELATION, KEY_INGREDIENT_ROWID + " = ?",
                new String[]{String.valueOf(id)});
        //db.close();
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
        //db.close();
        return recordExists;
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
            //db.close();
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


    // ------------------------------- "repas" Table Methods --------------------------------- //



    /**
     * Get the last id of repas table
     *
     * DANGER : crash dès qu'on met un "'" dans le nom ^^
     */
    public int lastIdRepas() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "";
        sql += "SELECT " + KEY_REPAS_ROWID;
        sql+= " FROM " + DATABASE_TABLE_REPAS;
        sql += " ORDER BY " + KEY_REPAS_ROWID + " DESC";
        sql+= " limit 1";

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        int result = cursor.getInt(0);
        //db.close();
        return result;
    }

    /**
     * Adding new repas
     *
     * @param repas
     * @return true si l'ajout est réussi, faux sinon
     * DANGER : crash dès qu'on met un "'" dans le nom ^^
     */
    public boolean addRepas(Repas repas) {
        boolean createSuccessful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_REPAS_DATE, repas.getDate().getTimeInMillis());
        values.put(KEY_REPAS_REPASTYPE, repas.getRepasType().toString());
        values.put(KEY_REPAS_CO2_EQUIVALENT, repas.getCo2Equivalent());
        createSuccessful = db.insert(DATABASE_TABLE_REPAS, null, values) > 0;

        for (ItemRepas itemRepas:repas.getElements()
                ) {
            addItemRepas(itemRepas, lastIdRepas());
        }
        //db.close();
        return createSuccessful;
    }

    /**
     * @return return all repas contained in the table "repas" of the database
     */
    public List<Repas> getAllRepas() {
        List<Repas> repasList = new ArrayList<Repas>();
        // Select All Query
        String sql = "";
        sql += "SELECT * FROM " + DATABASE_TABLE_REPAS;
        sql += " ORDER BY " + KEY_REPAS_DATE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Repas repas = getRepas(cursor.getInt(0));
                // Adding contact to list
                repasList.add(repas);
            } while (cursor.moveToNext());
        }
        // return contact list
        //db.close();
        return repasList;
    }

    public Repas getRepas(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DATABASE_TABLE_REPAS, new String[]{KEY_REPAS_ROWID,
                        KEY_REPAS_DATE, KEY_REPAS_REPASTYPE,
                        KEY_REPAS_CO2_EQUIVALENT}, KEY_REPAS_ROWID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        //On charge la liste d'item
        ArrayList<ItemRepas> elements = getItemRepasListFromRepasId(id);

        //On charge la date
        GregorianCalendar date = new GregorianCalendar();
        date.setTimeInMillis(cursor.getLong(1));

        Repas repas = new Repas(cursor.getInt(0),date, RepasType.stringToRepasType(cursor.getString(2)),elements,cursor.getDouble(3));
        //db.close();
        return repas;
    }

    public List<Repas> getAllRepasByRepasType(RepasType type){
        List<Repas> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery;

        selectQuery = "SELECT * FROM " + DATABASE_TABLE_REPAS + " WHERE "
                + KEY_REPAS_REPASTYPE + " = '" + type.toString() + "'";
        // looping through all rows and adding to list
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            do {
                Repas repas = getRepas(cursor.getInt(0));
                // Adding item to list
                list.add(repas);
            } while (cursor.moveToNext());
        }
        // return contact list
        //db.close();

        return list;
    }

    /**
     * Updating single repas
     * Requieres an item with an id from the database !
     * Do not convert an item to composedItem or the oppositve
     *
     * @param repas
     * @return int
     * @throws InvalidParameterException
     */

    public int updateRepas(Repas repas) throws InvalidParameterException {
        Boolean UpdateItemSuccess = true;
        Boolean updateRelationSuccess = true;
        if (repas.getId() <= -1) {
            throw new InvalidParameterException("updateItem must be used on a contact instanciated from the database");
        }
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_REPAS_DATE, repas.getDate().getTimeInMillis());
        values.put(KEY_REPAS_CO2_EQUIVALENT, repas.getCo2Equivalent());
        values.put(KEY_REPAS_REPASTYPE, repas.getRepasType().toString());


        //updating ingredients relationship

        //checking if any local item has been removed from the ingredients list
        List<Integer> baseItemRepasId = new ArrayList<>();
        List<ItemRepas> newItemRepasList = new ArrayList<>();
        for (ItemRepas itemRepas : repas.getElements()){
            if (itemRepas.getId() <0) {
                newItemRepasList.add(itemRepas);
            }
            else {
                baseItemRepasId.add(itemRepas.getId());
            }
        }
        //Suppression des item repas qui ne sont plus présent dans la liste de repas
        List<Integer> formerItemRepasIds = getItemRepasListIdFromRepasId(repas.getId());
        Log.e(TAG, "base "+baseItemRepasId.toString());
        Log.e(TAG, "new "+ newItemRepasList.toString());
        Log.e(TAG, "former " + formerItemRepasIds.toString());
        for (int id:formerItemRepasIds) {
            if (!baseItemRepasId.contains(id)){
                Log.e(TAG, id + " has been deleted ");

                deleteItemRepas(id);

            }
        }

        //adding new ingredients or updating already existing ones.


        for (ItemRepas itemRepas : repas.getElements()) {
            ContentValues valuesItemRepas = new ContentValues();
            int itemId = itemRepas.getItem().getId();
            valuesItemRepas.put(KEY_ITEM_REPAS_ITEM_ID, itemId);
            valuesItemRepas.put(KEY_ITEM_REPAS_REPAS_ID, repas.getId());
            valuesItemRepas.put(KEY_ITEM_REPAS_POIDS, itemRepas.getPoids());
            if (checkIfItemRepasExists(repas.getId(),itemRepas.getItem().getId())) {
                db.update(DATABASE_TABLE_ITEM_REPAS, valuesItemRepas, KEY_INGREDIENT_ROWID + " = ?",
                        new String[]{String.valueOf(itemRepas.getId())});
            } else {
                boolean success = db.insert(DATABASE_TABLE_ITEM_REPAS, null, valuesItemRepas) > 0;
                if (success) {
                    Log.e(TAG, itemRepas.getItem().getName() + " has been successfully added to meal");
                }
            }
        }

        // updating row
        return db.update(DATABASE_TABLE_REPAS, values, KEY_ITEMS_ROWID + " = ?",
                new String[]{String.valueOf(repas.getId())});
    }


    // Deleting single contact
    public void deleteRepas(Repas repas) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE_REPAS, KEY_REPAS_ROWID + " = ?",
                new String[]{String.valueOf(repas.getId())});
        for (ItemRepas itemRepas:repas.getElements()
                ) {
            db.delete(DATABASE_TABLE_ITEM_REPAS,KEY_ITEM_REPAS_ROWID + " = ?",
                    new String[]{String.valueOf(repas.getId())});
        }
        //db.close();
    }

    // Getting items Count
    public int getRepasCount() {

        String countQuery = "SELECT  * FROM " + DATABASE_TABLE_REPAS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;

    }

    // ------------------------------- "item_repas" Table Methods --------------------------------- //

    /**
     * add an item_repas-relation between an item and a repas
     *
     * @param item_id
     * @param repas_id
     * @param poids : weigth of the item in the meal
     * @return true if the relation was added, or if it already exist
     */
    public boolean addItemRepas(int item_id, int repas_id, double poids) {
        boolean success = true;
        if (!checkIfItemRepasExists(repas_id,item_id)) {


            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_REPAS_ITEM_ID, item_id);
            values.put(KEY_ITEM_REPAS_REPAS_ID, repas_id);
            values.put(KEY_ITEM_REPAS_POIDS, poids);

            success = db.insert(DATABASE_TABLE_ITEM_REPAS, null, values) > 0;
        }
        else {
            Toast.makeText(context,R.string.item_repas_already_on_db,Toast.LENGTH_LONG).show();
        }
        return success;

    }

    public boolean addItemRepas(ItemRepas itemRepas, int repas_id) {
        boolean success = true;
        int item_id = itemRepas.getItem().getId();
        int poids = itemRepas.getPoids();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_REPAS_ITEM_ID, item_id);
        values.put(KEY_ITEM_REPAS_REPAS_ID, repas_id);
        values.put(KEY_ITEM_REPAS_POIDS, poids);

        success = db.insert(DATABASE_TABLE_ITEM_REPAS, null, values) > 0;
        return success;

    }

    /**
     * @param repas_id
     * @return the list of item repas id from a repas id
     */
    public List<Integer> getItemRepasListIdFromRepasId(int repas_id) {
        List<Integer> idList = new ArrayList<>();
        String selectQuery = "SELECT " + KEY_ITEM_REPAS_ROWID + " FROM " + DATABASE_TABLE_ITEM_REPAS + " WHERE "
                + KEY_ITEM_REPAS_REPAS_ID + " = '" + repas_id + "'";

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
        return idList;

    }

    /**
     * @param repas_id
     * @return the list of item repas id from a repas id
     */
    public ArrayList<ItemRepas> getItemRepasListFromRepasId(int repas_id) {
        ArrayList<ItemRepas> idList = new ArrayList<>();
        String selectQuery = "SELECT " + KEY_ITEM_REPAS_ROWID + " FROM " + DATABASE_TABLE_ITEM_REPAS + " WHERE "
                + KEY_ITEM_REPAS_REPAS_ID + " = '" + repas_id + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                idList.add(getItemRepasFromId(cursor.getInt(0)));

            } while (cursor.moveToNext());
        }
        // return contact list
        cursor.close();
        //db.close();
        return idList;

    }

    /**
     * @param item_repas_id
     * @return the item_repas within the database that matches the id
     */
    public ItemRepas getItemRepasFromId(int item_repas_id) {
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE_ITEM_REPAS + " WHERE "
                + KEY_ITEM_REPAS_ROWID + " = '" + item_repas_id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();
        return new ItemRepas(cursor.getInt(0),getItem(cursor.getInt(2)), cursor.getInt(3));
    }

    /**
     * Used to delete corresponding item_repas objects when deleting an item.
     * @param item_id
     * @return the list of the IDs of the item_repas objects that contains the item whose Id matches item_id
     */
    public List<Integer>getItemRepasIdFromItemId(int item_id){
        List<Integer> idList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE_ITEM_REPAS + " WHERE "
                + KEY_ITEM_REPAS_ITEM_ID + " = '" + item_id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                idList.add(cursor.getInt(0));

            } while (cursor.moveToNext());
        }
        return idList;
    }


    public void deleteItemRepas(int item_repas_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE_ITEM_REPAS, KEY_ITEM_REPAS_ROWID + " = ?",
                new String[]{String.valueOf(item_repas_id)});
    }
    /**
     * check if an ingredient-relation between an item and a composedItem exists
     *
     * @param repas_id
     * @param item_id
     * @return
     */
    public boolean checkIfItemRepasExists(int repas_id, int item_id) {
        boolean recordExists = false;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_ITEM_REPAS_ROWID + " FROM " + DATABASE_TABLE_ITEM_REPAS +
                " WHERE " + KEY_ITEM_REPAS_ITEM_ID + " = '" + item_id + "'" + " AND " + KEY_ITEM_REPAS_REPAS_ID +
                " = '" + repas_id + "'", null);

        if (cursor != null) {

            if (cursor.getCount() > 0) {
                recordExists = true;
            }
        }

        cursor.close();
        return recordExists;
    }



}


