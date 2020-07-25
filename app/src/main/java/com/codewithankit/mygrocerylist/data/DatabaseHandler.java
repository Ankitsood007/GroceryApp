package com.codewithankit.mygrocerylist.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.Nullable;

import com.codewithankit.mygrocerylist.Model.Grocery;
import com.codewithankit.mygrocerylist.Util.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private Context ctx;
    public DatabaseHandler(Context context) {
        super(context, Constants.DB_NAME, null , Constants.DB_VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GROCERY_TABLE = "CREATE TABLE " +
                Constants.TABLE_NAME + "(" +
                Constants.KEY_ID + " INTEGER PRIMARY KEY," +
                Constants.KEY_GROCERY_ITEM + " TEXT," +
                Constants.KEY_QTY_NO + " TEXT," +
                Constants.KEY_DATE_NO + " LONG);";

        db.execSQL(CREATE_GROCERY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }

    /*
    CRUD goes here -> create , read , update , delete.
     */

    //1.
    //add a grocery item.
    public void addGrocery(Grocery g){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_GROCERY_ITEM, g.getName());
        values.put(Constants.KEY_QTY_NO, g.getQuantity());
        values.put(Constants.KEY_DATE_NO, java.lang.System.currentTimeMillis());

        //Insert the row
        db.insert(Constants.TABLE_NAME, null, values);

        Log.d("Saved!!", "Saved to DB");

    }

    //2.
    //get a grocery item.
    public Grocery getGrocery(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[] {Constants.KEY_ID,
                        Constants.KEY_GROCERY_ITEM, Constants.KEY_QTY_NO, Constants.KEY_DATE_NO},
                Constants.KEY_ID + "=?",
                new String[] {String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();


        Grocery grocery = new Grocery();
        assert cursor != null;
        grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
        grocery.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
        grocery.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY_NO)));

        //convert timestamp to something readable
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formatedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NO)))
                .getTime());
        grocery.setDateItemAdded(formatedDate);
        return grocery;
    }

    //3.
    //get all groceries.
    public List<Grocery> getAllGroceries(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Grocery> gl = new ArrayList<>();

        Cursor cursor = db.query(Constants.TABLE_NAME,new String[]{Constants.KEY_ID,Constants.KEY_GROCERY_ITEM,Constants.KEY_QTY_NO,Constants.KEY_DATE_NO},
                null , null , null , null , Constants.KEY_DATE_NO + " DESC");

        if(cursor.moveToFirst()){
            do {
                Grocery g = new Grocery();
                g.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                g.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
                g.setQuantity(cursor.getString(cursor.getColumnIndex(Constants.KEY_QTY_NO)));

                //convert timestamp to something readable
                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NO))).getTime());
                g.setDateItemAdded(formedDate);
                gl.add(g);
            }while(cursor.moveToNext());
        }
        return gl;
    }

    //4.
    //update the grocery
    public int updateGrocery(Grocery g){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_GROCERY_ITEM, g.getName());
        values.put(Constants.KEY_QTY_NO, g.getQuantity());
        values.put(Constants.KEY_DATE_NO, java.lang.System.currentTimeMillis());//get system time

        //update row
        return db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + "=?", new String[] { String.valueOf(g.getId())} );

    }

    //5.
    //delete grocery
    public void deleteGrocery(int id){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + "=?", new String[] { String.valueOf(id)});
        db.close();
    }

    //6.
    //get the count of groceries which are currently present in the database.
    public int getGroceryCount(){
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }
}
