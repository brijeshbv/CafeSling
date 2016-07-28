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
public class DbReceipt extends SQLiteOpenHelper {

    public DbReceipt(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    SQLiteDatabase db;



    //GRG
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "slingcafe.db";
    private static final String TABLE_NAME = "receipttable";
    private static final String Serial_NO = "slno";
    private static final String RECEIPT_NO = "receiptno";
    private static final String Employee_ID = "empid";
    private static final String SQL_DateTime = "datentime";
    private static final String Paid_Amount = "amountpaid";
    private static final String BALANCE = "balance";


    private static final String TABLE_CREATE = "CREATE TABLE "+TABLE_NAME+" ("+Serial_NO
            +" integer primary key, "+RECEIPT_NO+" bigint, "+Employee_ID+" integer, "+SQL_DateTime+" DATETIME DEFAULT CURRENT_TIMESTAMP, "+Paid_Amount+" integer, "+BALANCE+" integer)";

    //GRG


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

    public void newReceipt(HashMap<String,String> queryValues) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        final int amountPaid = Integer.parseInt(queryValues.get("amountPaid"));
        long receiptNo = System.currentTimeMillis();  //Receipt no is time in milliseconds;
        int newBalance = Integer.parseInt(queryValues.get("balance")) - amountPaid;

        values.put(RECEIPT_NO,receiptNo);
        values.put(Employee_ID, queryValues.get("empid"));
        values.put(Paid_Amount,amountPaid);
        values.put(BALANCE,newBalance);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<HashMap<String,String>> getAllReceipts(){
        ArrayList<HashMap<String,String>> receiptArrayList = new ArrayList<HashMap<String, String>>();
        String selectQuery ="SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor1 = db.rawQuery(selectQuery,null);
        if(cursor1.moveToFirst()){
            do{
                HashMap<String,String> receipt = new HashMap<String, String>();
                receipt.put(Serial_NO,cursor1.getString(0));
                receipt.put(RECEIPT_NO,cursor1.getString(1));
                receipt.put(Employee_ID,cursor1.getString(2));
                receipt.put(SQL_DateTime,cursor1.getString(3));
                receipt.put(Paid_Amount,cursor1.getString(4));
                receipt.put(BALANCE,cursor1.getString(5));
                receiptArrayList.add(receipt);
            }while (cursor1.moveToNext());

        }

        return receiptArrayList;
    }

}
