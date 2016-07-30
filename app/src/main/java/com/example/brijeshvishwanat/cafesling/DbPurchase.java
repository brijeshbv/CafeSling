package com.example.brijeshvishwanat.cafesling;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Gautam.Gare on 29-Jul-16.
 */
public class DbPurchase  extends SQLiteOpenHelper {

    public DbPurchase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    SQLiteDatabase db;



    //GRG
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "slingcafe.db";
    private static final String TABLE_NAME = "purchasetable";
    private static final String Serial_NO = "slno";
    private static final String Voucher_NO = "voucherno";
    private static final String SQL_DateTime = "datentime";
    private static final String Paid_Amount = "amountpaid";
    private static final String Cash_in_Hand = "cashinhand";
    private static final String Remark = "remark";

    private static final String TABLE_CREATE = "CREATE TABLE "+TABLE_NAME+" ("+Serial_NO
            +" integer primary key, "+Voucher_NO+" text, "+SQL_DateTime+" DATETIME DEFAULT CURRENT_TIMESTAMP, "
            +Paid_Amount+" integer, "+Cash_in_Hand+" integer, "+Remark+" text )";

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

    public void newPurchase(HashMap<String,String> queryValues) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        final int purchaseAmount = Integer.parseInt(queryValues.get("purchaseAmount"));
        String voucherNo = queryValues.get("voucherNumber");
        String remarks = queryValues.get("remarks");
        int newCashInHand = Integer.parseInt(queryValues.get("cashInHand")) - purchaseAmount;

        values.put(Voucher_NO,voucherNo);
        values.put(Paid_Amount, purchaseAmount);
        values.put(Cash_in_Hand,newCashInHand);
        values.put(Remark,remarks);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<HashMap<String,String>> getAllPurchases(){
        ArrayList<HashMap<String,String>> purchaseArrayList = new ArrayList<HashMap<String, String>>();
        String selectQuery ="SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor1 = db.rawQuery(selectQuery,null);
        if(cursor1.moveToFirst()){
            do{
                HashMap<String,String> purchase = new HashMap<String, String>();
                purchase.put(Serial_NO,cursor1.getString(0));
                purchase.put(Voucher_NO,cursor1.getString(1));
                purchase.put(SQL_DateTime,cursor1.getString(2));
                purchase.put(Paid_Amount,cursor1.getString(3));
                purchase.put(Cash_in_Hand,cursor1.getString(4));
                purchase.put(Remark,cursor1.getString(5));
                purchaseArrayList.add(purchase);
            }while (cursor1.moveToNext());

        }

        return purchaseArrayList;
    }

}
