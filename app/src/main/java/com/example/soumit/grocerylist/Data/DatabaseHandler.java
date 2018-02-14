package com.example.soumit.grocerylist.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.soumit.grocerylist.Model.Grocery;
import com.example.soumit.grocerylist.Util.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Soumit on 2/13/2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHandler";

    private Context context;

    public DatabaseHandler(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GROCERY_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "(" + Constants.KEY_ID + " INTEGER PRIMARY KEY," +
                Constants.KEY_GROCERY_ITEM + " TEXT," + Constants.KEY_QTY_NUMBER + " TEXT," + Constants.KEY_DATE_NAME + " LONG);";

        db.execSQL(CREATE_GROCERY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }

    /**
     * CRUD Operation
     */

    public void addGrocery(Grocery grocery){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.KEY_GROCERY_ITEM, grocery.getName());
        contentValues.put(Constants.KEY_QTY_NUMBER, grocery.getQuantity());
        contentValues.put(Constants.KEY_DATE_NAME, System.currentTimeMillis());

        //row insertion
        db.insert(Constants.TABLE_NAME, null, contentValues);
        db.close();

        Log.d(TAG, "addGrocery: Saved to db");
    }

    /**
     * get single grocery item
     * @param id
     * @return grocery
     */
    public Grocery getGrocery(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{
                Constants.KEY_ID, Constants.KEY_GROCERY_ITEM, Constants.KEY_QTY_NUMBER, Constants.KEY_DATE_NAME},
                Constants.KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        if(cursor!=null)
            cursor.moveToFirst();

        Grocery grocery = new Grocery();
        grocery.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
        grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
        grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));

        //converting timeStamp
        DateFormat dateFormat = DateFormat.getDateInstance();
        String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());

        grocery.setDateItemAdded(formatedDate);

        return grocery;
    }

    /**
     * get All Groceries
     * @return groceryList
     */
    public List<Grocery> getAllGroceries(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Grocery> groceryList = new ArrayList<>();
        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{
                Constants.KEY_ID, Constants.KEY_GROCERY_ITEM, Constants.KEY_QTY_NUMBER,
                Constants.KEY_DATE_NAME},
                null, null, null, null, Constants.KEY_DATE_NAME + " DESC");

        if(cursor.moveToFirst()){
            do {
                Grocery grocery = new Grocery();
                grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
                grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));

                //converting timeStamp
                DateFormat dateFormat = DateFormat.getDateInstance();
                String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());

                grocery.setDateItemAdded(formatedDate);

                groceryList.add(grocery);

            }while (cursor.moveToNext());
        }

        return groceryList;
    }

    /**
     * update single grocery item
     * @param grocery
     * @return updated row no
     */
    public int updateGrocery(Grocery grocery){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_GROCERY_ITEM, grocery.getName());
        values.put(Constants.KEY_QTY_NUMBER, grocery.getQuantity());
        values.put(Constants.KEY_DATE_NAME, System.currentTimeMillis());

        //update row
        return db.update(Constants.TABLE_NAME, values,
                Constants.KEY_ID + "=?", new String[]{String.valueOf(grocery.getId())});
    }

    /**
     * delete single grocery item
     * @param id
     */
    public void deleteGrocery(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Constants.TABLE_NAME,
                Constants.KEY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    /**
     * grocery item counter
     * @return count
     */
    public int getGroceriesCount(){
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }

}




























