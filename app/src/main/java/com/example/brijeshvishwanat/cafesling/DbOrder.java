package com.example.brijeshvishwanat.cafesling;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Brijesh.Vishwanat on 7/27/2016.
 */
public class DbOrder  extends SQLiteOpenHelper {
    public DbOrder(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    SQLiteDatabase db;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "slingcafe.db";
    private static final String TABLE_NAME = "ordertable";
    private static final String COLUMN_NO = "no";
    private static final String COLUMN_ID = "empid";
    private static final String COLUMN_UNIQUE = "uniqueno";
    private static final String COLUMN_SQLDT = "sqldt";
    private static final String COLUMN_MEAL = "mealtype";
    private static final String COLUMN_COST = "cost";
    private static final String COLUMN_PAID = "paid";
    private static final String COLUMN_BALANCE = "balance";
    private static final String COLUMN_STATUS = "status";




    private static final String TABLE_CREATE = "CREATE TABLE "+ TABLE_NAME+"(no integer primary key, "
            +COLUMN_UNIQUE+" BIGINT,"+COLUMN_SQLDT+" DATETIME DEFAULT CURRENT_TIMESTAMP, "+
            COLUMN_ID+" text,"+COLUMN_MEAL +" text,"+COLUMN_COST+" integer,"
            +COLUMN_PAID+" text,"+COLUMN_BALANCE+" integer, " + COLUMN_STATUS+" text DEFAULT 'pending')" ;


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

    public void insertOrder(HashMap<String,String> queryValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //String query= "SELECT * FROM SlingEmployee";
        //Cursor cursor0 = new db.rawQuery();

        // values.put(COLUMN_NO, 10);
        values.put(COLUMN_ID, queryValues.get(COLUMN_ID));
        values.put(COLUMN_UNIQUE, queryValues.get(COLUMN_UNIQUE));


        values.put(COLUMN_MEAL, queryValues.get(COLUMN_MEAL));
      values.put(COLUMN_COST, Integer.valueOf(queryValues.get(COLUMN_COST)));
        values.put(COLUMN_PAID, queryValues.get(COLUMN_PAID));
        values.put(COLUMN_BALANCE, Integer.valueOf(queryValues.get(COLUMN_BALANCE)));
        values.put(COLUMN_STATUS, queryValues.get(COLUMN_STATUS));
        db.insert(TABLE_NAME, null, values);
        db.close();

    }


    //not yet complete
    public ArrayList<HashMap<String,String>> getAllTransactions(String idToSearch) {
        ArrayList<HashMap<String,String>> transactionArrayMap =new ArrayList<HashMap<String,String>>();

        SQLiteDatabase db = this.getReadableDatabase();
        String balanceReturned="";
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE empid='" + idToSearch + "'";
        Cursor cursor1 = db.rawQuery(query, null);

        if (cursor1.moveToFirst()) {
            do {
                HashMap<String,String> employeeMap = new HashMap<String, String>();
                employeeMap.put(COLUMN_NO,cursor1.getString(0));
                employeeMap.put(COLUMN_ID,cursor1.getString(1));
                employeeMap.put(COLUMN_UNIQUE,cursor1.getString(2));
                employeeMap.put(COLUMN_SQLDT,cursor1.getString(3));
                employeeMap.put(COLUMN_MEAL,cursor1.getString(4));
                employeeMap.put(COLUMN_COST,cursor1.getString(5));
                employeeMap.put(COLUMN_PAID,cursor1.getString(6));
                employeeMap.put(COLUMN_BALANCE,cursor1.getString(7));
                employeeMap.put(COLUMN_STATUS,cursor1.getString(8));

transactionArrayMap.add(employeeMap);

            } while (cursor1.moveToNext());


        }
        cursor1.close();
        db.close();
        return transactionArrayMap;
    }


    public HashMap<String,String> getBalance( String ID) {

        HashMap<String,String> employeeMap = new HashMap<String, String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String balanceReturned="";
        String query = "SELECT SUM("+COLUMN_BALANCE+") ,"+ COLUMN_ID+" FROM "+TABLE_NAME+" WHERE "+COLUMN_ID+" is "+ID+" GROUP BY "+COLUMN_ID;

        Cursor cursor1a = db.rawQuery(query, null);

        if (cursor1a.moveToFirst()) {
            do {

                employeeMap.put(COLUMN_BALANCE,cursor1a.getString(0));
                employeeMap.put(COLUMN_ID,cursor1a.getString(1));


            } while (cursor1a.moveToNext());


        }
        cursor1a.close();
        db.close();
        return employeeMap;
    }

    public  void balanceUpdate(String amountPaid, String empId){
        int balanceFromTop = 0;
        String statusvar = "paid";



        int amountPaidInt = Integer.valueOf(amountPaid);
        SQLiteDatabase db = this.getWritableDatabase();

        while (amountPaidInt != 0) {

            String query = "SELECT "+COLUMN_BALANCE+" FROM "+TABLE_NAME+" WHERE "+COLUMN_NO+" is (SELECT MIN("+COLUMN_NO+") FROM "
                    +TABLE_NAME+" WHERE "+COLUMN_ID+" is '"+empId+"' AND "
                    + COLUMN_STATUS+" is 'pending') ";
            Cursor cursor1 = db.rawQuery(query, null);


            if (cursor1.moveToFirst()) {
                do {
                   balanceFromTop = cursor1.getInt(0);
                } while (cursor1.moveToNext());
            }
            cursor1.close();


            if (balanceFromTop <= amountPaidInt){

                amountPaidInt = amountPaidInt - balanceFromTop;
                balanceFromTop = 0;
                        statusvar ="paid";
                String query0 ="UPDATE "+TABLE_NAME+
                        " SET "+COLUMN_BALANCE+" = "+balanceFromTop+","+ COLUMN_STATUS+ " = '"+ statusvar +
                        "' WHERE "+COLUMN_NO+" is (SELECT MIN("+COLUMN_NO+") FROM "
                        +TABLE_NAME+" WHERE "+COLUMN_ID+" is '"+empId+"' AND "
                        + COLUMN_STATUS+" is 'pending') ";
                db.execSQL(query0);
              //  Cursor cursor2 = db.rawQuery(query0, null);
              //  cursor2.close();
            }
            else if (balanceFromTop > amountPaidInt) {
                balanceFromTop = balanceFromTop - amountPaidInt;
                amountPaidInt = 0;
                statusvar = "pending";
                String query0 ="UPDATE "+TABLE_NAME+
                        " SET "+COLUMN_BALANCE+" = "+balanceFromTop+","+ COLUMN_STATUS+ " = '"+ statusvar +
                        "' WHERE "+COLUMN_NO+" is (SELECT MIN("+COLUMN_NO+") FROM "
                        +TABLE_NAME+" WHERE "+COLUMN_ID+" is '"+empId+"' AND "
                        + COLUMN_STATUS+" is 'pending') ";
               db.execSQL(query0);
              //  Cursor cursor3 = db.rawQuery(query0, null);
           // cursor3.close();
            }


        }
        db.close();


    }


    public HashMap<String,String> getBalanceForDate( String ID, String date) {


        HashMap<String,String> employeeMap = new HashMap<String, String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String minDate = "";
        String queryForDate = " SELECT " + "strftime('%Y-%m'," + COLUMN_SQLDT + ") " + " FROM " + TABLE_NAME
                + " WHERE " + COLUMN_ID + " is " + ID
                + " AND " + COLUMN_NO + " IS (SELECT MIN(" + COLUMN_NO + ") FROM "
                + TABLE_NAME + " WHERE " + COLUMN_ID + " is '" + ID + "')";
        Cursor cursorDate = db.rawQuery(queryForDate, null);
        if (cursorDate.moveToFirst()) {
            do {

                minDate = cursorDate.getString(0);


            } while (cursorDate.moveToNext());


        }
        cursorDate.close();

        String balanceReturned="";
        String query = "SELECT SUM("+COLUMN_BALANCE+") ,"+ COLUMN_ID+"  FROM "+TABLE_NAME
                +" WHERE "+COLUMN_ID+" is "+ID+" AND strftime('%Y-%m',"+ COLUMN_SQLDT+") "
                + "BETWEEN \"" + minDate + "\" AND \"" + date + "\""
                +" GROUP BY "+COLUMN_ID;

        Cursor cursor1ab = db.rawQuery(query, null);

        if (cursor1ab.moveToFirst()) {
            do {

                employeeMap.put(COLUMN_BALANCE, cursor1ab.getString(0));
                employeeMap.put(COLUMN_ID, cursor1ab.getString(1));


            } while (cursor1ab.moveToNext());


        }
        cursor1ab.close();
        db.close();
        return employeeMap;
    }


}