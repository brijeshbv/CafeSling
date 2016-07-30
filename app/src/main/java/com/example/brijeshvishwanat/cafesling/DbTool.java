package com.example.brijeshvishwanat.cafesling;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Brijesh.Vishwanat on 7/22/2016.
 */
public class DbTool extends SQLiteOpenHelper {

    public DbTool(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    SQLiteDatabase db;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "slingcafe.db";
    private static final String TABLE_NAME = "slingemployee";
    private static final String COLUMN_NO = "no";
    private static final String COLUMN_ID = "empid";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_BALANCE = "balance";

//FOR MEALTYPE TABLE CREATION
    private static final String TABLE_NAME1 = "pricetable";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_MEAL = "mealtype";

    // FOR ORDER TABLE CREATION
    private static final String TABLE_NAME2 = "ordertable";
    private static final String COLUMN_UNIQUE = "uniqueno";
    private static final String COLUMN_SQLDT = "sqldt";
    private static final String COLUMN_BREAK = "breakfast";
    private static final String COLUMN_LUNCH = "lunch";
    private static final String COLUMN_COST = "cost";
    private static final String COLUMN_PAID = "paid";
    private static final String COLUMN_STATUS = "status";

    //FOR RECEIPT GENERATION


    private static final String TABLE_NAME3 = "receipttable";
    private static final String Serial_NO = "slno";
    private static final String RECEIPT_NO = "receiptno";
    private static final String Employee_ID = "empid";
    private static final String SQL_DateTime = "datentime";
    private static final String Paid_Amount = "amountpaid";
    private static final String BALANCE = "balance";

    ///purchase variables
    private static final String TABLE_NAME4 = "purchasetable";
    private static final String Voucher_NO = "voucherno";
    private static final String Cash_in_Hand = "cashinhand";
    private static final String Remark = "remark";

    private static final String TABLE_CREATE4 = "CREATE TABLE "+TABLE_NAME4+" ("+Serial_NO
            +" integer primary key, "+Voucher_NO+" text, "+SQL_DateTime+" DATETIME DEFAULT CURRENT_TIMESTAMP, "
            +Paid_Amount+" integer, "+Cash_in_Hand+" integer, "+Remark+" text )";

    //GRG

    private static final String TABLE_CREATE = "CREATE TABLE slingemployee (no integer primary key, " +
            "empid text, name text, email text, balance text)";

    private static final String TABLE_CREATE1 = "CREATE TABLE pricetable(no integer primary key, " +
            "mealtype text,price text )";


    private static final String TABLE_CREATE2 = "CREATE TABLE "+ TABLE_NAME2+"(no integer primary key, "
            +COLUMN_UNIQUE+" BIGINT,"+COLUMN_SQLDT+" DATETIME DEFAULT CURRENT_TIMESTAMP, "+
            COLUMN_ID+" text,"+COLUMN_MEAL +" text,"+COLUMN_COST+" integer,"
            +COLUMN_PAID+" text,"+COLUMN_BALANCE+" integer," + COLUMN_STATUS+" text)" ;



    private static final String TABLE_CREATE3 = "CREATE TABLE "+TABLE_NAME3+" ("+Serial_NO
            +" integer primary key, "+RECEIPT_NO+" bigint, "+Employee_ID+" integer, "+SQL_DateTime+" DATETIME DEFAULT CURRENT_TIMESTAMP, "+Paid_Amount+" integer, "+BALANCE+" integer)";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);
        sqLiteDatabase.execSQL(TABLE_CREATE1);
        sqLiteDatabase.execSQL(TABLE_CREATE2);
        sqLiteDatabase.execSQL(TABLE_CREATE3);
        sqLiteDatabase.execSQL(TABLE_CREATE4);
        this.db = sqLiteDatabase;

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        String querym = "DROP TABLE IF EXISTS " + TABLE_NAME1;
        String queryo = "DROP TABLE IF EXISTS " + TABLE_NAME2;
        String queryp = "DROP TABLE IF EXISTS " + TABLE_NAME3;
        String queryr = "DROP TABLE IF EXISTS " + TABLE_NAME4;
        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.execSQL(querym);
        sqLiteDatabase.execSQL(queryo);
        sqLiteDatabase.execSQL(queryp);
        sqLiteDatabase.execSQL(queryr);
        onCreate(sqLiteDatabase);

    }

    public void insertEmpData(HashMap<String,String> queryValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //String query= "SELECT * FROM SlingEmployee";
        //Cursor cursor0 = new db.rawQuery();

       // values.put(COLUMN_NO, 10);
        values.put(COLUMN_ID, queryValues.get(COLUMN_ID));
        values.put(COLUMN_NAME, queryValues.get(COLUMN_NAME));

        values.put(COLUMN_EMAIL, queryValues.get(COLUMN_EMAIL));
        values.put(COLUMN_BALANCE, queryValues.get(COLUMN_BALANCE));
        db.insert(TABLE_NAME, null, values);
        db.close();

    }

    public int updateContact(HashMap<String,String> queryValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //String query= "SELECT * FROM SlingEmployee";
        //Cursor cursor0 = new db.rawQuery();

        //values.put(COLUMN_NO, 1);
        values.put(COLUMN_ID, queryValues.get(COLUMN_ID));
        values.put(COLUMN_NAME, queryValues.get(COLUMN_NAME));

        values.put(COLUMN_EMAIL, queryValues.get(COLUMN_EMAIL));
        values.put(COLUMN_BALANCE, queryValues.get(COLUMN_BALANCE));
        return db.update(TABLE_NAME , values,COLUMN_ID +" =?",new String[]{queryValues.get(COLUMN_ID)});

    }
    public void deleteContact(String id){

        SQLiteDatabase db = this.getWritableDatabase();
        String deleteQuery ="DELETE FROM "+TABLE_NAME+" WHERE "+COLUMN_ID+"='"+ id+"'";
      db.execSQL(deleteQuery);
    }

    public HashMap<String,String> searchEmpDetails(String idToSearch) {

        HashMap<String,String> employeeMap = new HashMap<String, String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String balanceReturned="";
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE empid='" + idToSearch + "'";
        Cursor cursor1 = db.rawQuery(query, null);

        if (cursor1.moveToFirst()) {
            do {

                employeeMap.put(COLUMN_NO,cursor1.getString(0));
                employeeMap.put(COLUMN_ID,cursor1.getString(1));
                employeeMap.put(COLUMN_NAME,cursor1.getString(2));
                employeeMap.put(COLUMN_EMAIL,cursor1.getString(3));
                employeeMap.put(COLUMN_BALANCE,cursor1.getString(4));
            } while (cursor1.moveToNext());


        }
        return employeeMap;
    }



public ArrayList<HashMap<String,String>> getAllEmployees(){
    ArrayList<HashMap<String,String>> employeeArrayList = new ArrayList<HashMap<String, String>>();
    String selectQuery ="SELECT * FROM "+TABLE_NAME;
   SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor1 = db.rawQuery(selectQuery,null);
    if(cursor1.moveToFirst()){
        do{
            HashMap<String,String> employeeMap = new HashMap<String, String>();
            employeeMap.put(COLUMN_NO,cursor1.getString(0));
            employeeMap.put(COLUMN_ID,cursor1.getString(1));
            employeeMap.put(COLUMN_NAME,cursor1.getString(2));
            employeeMap.put(COLUMN_EMAIL,cursor1.getString(3));
            employeeMap.put(COLUMN_BALANCE,cursor1.getString(4));
employeeArrayList.add(employeeMap);
        }while (cursor1.moveToNext());

        }

return employeeArrayList;
    }
}

