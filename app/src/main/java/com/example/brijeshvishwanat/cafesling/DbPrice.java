package com.example.brijeshvishwanat.cafesling;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Brijesh.Vishwanat on 7/26/2016.
 */
public class DbPrice extends SQLiteOpenHelper {
    public DbPrice(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    SQLiteDatabase db;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "slingcafe.db";
    private static final String TABLE_NAME = "pricetable";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_MEAL = "mealtype";

    private static final String TABLE_CREATE = "CREATE TABLE pricetable(no integer primary key, " +
            "mealtype text,price text )";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);

        this.db = sqLiteDatabase;

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(query);
        onCreate(sqLiteDatabase);

    }
    public void insertPrice(HashMap<String,String> queryValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEAL, queryValues.get(COLUMN_MEAL));
        values.put(COLUMN_PRICE, queryValues.get(COLUMN_PRICE));
        db.insert(TABLE_NAME, null, values);
        db.close();

    }

    public int updatePrice(HashMap<String,String> queryValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //String query= "SELECT * FROM SlingEmployee";
        //Cursor cursor0 = new db.rawQuery();

        //values.put(COLUMN_NO, 1);
        values.put(COLUMN_MEAL, queryValues.get(COLUMN_MEAL));
        values.put(COLUMN_PRICE, queryValues.get(COLUMN_PRICE));
        return db.update(TABLE_NAME , values,COLUMN_MEAL+"=?" ,new String[]{queryValues.get(COLUMN_MEAL)});

    }
    public HashMap<String,String> getPrice(String mealtype){

        SQLiteDatabase db = this.getReadableDatabase();
  HashMap<String,String> price = new HashMap<String, String>();
        String query = "SELECT * FROM "+ TABLE_NAME +" WHERE "+COLUMN_MEAL+"= "+mealtype;
        Cursor cursor1 = db.rawQuery(query, null);

        if (cursor1.moveToFirst()) {
            do {

                price.put(COLUMN_PRICE,cursor1.getString(2));
            } while (cursor1.moveToNext());


        }
        return price;
    }

    public ArrayList<HashMap<String,String>> getAllMeals(){
        ArrayList<HashMap<String,String>> employeeArrayList = new ArrayList<HashMap<String, String>>();
        String selectQuery ="SELECT * FROM "+TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor1 = db.rawQuery(selectQuery,null);
        if(cursor1.moveToFirst()){
            do{
                HashMap<String,String> mealMap = new HashMap<String, String>();
                mealMap.put(COLUMN_MEAL,cursor1.getString(1));
                mealMap.put(COLUMN_PRICE,cursor1.getString(2));
                employeeArrayList.add(mealMap);
            }while (cursor1.moveToNext());

        }

        return employeeArrayList;
    }
}
