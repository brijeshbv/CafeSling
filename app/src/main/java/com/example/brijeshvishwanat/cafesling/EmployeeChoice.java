package com.example.brijeshvishwanat.cafesling;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Brijesh.Vishwanat on 7/25/2016.
 */

public class EmployeeChoice  extends AppCompatActivity{
    public String name;
    public String balance;
    public  String email;
    public TextView empNameTv ;
    public TextView empEmailTv;
    public TextView empIdTv;
    public TextView empBalanceTv;


    DbTool toolDb = new DbTool(this);
    DbPrice priceDb = new DbPrice(this);
    DbOrder orderDb = new DbOrder(this);



    private static final String COLUMN_ID = "empid";
    private static final String COLUMN_UNIQUE = "uniqueno";
    private static final String COLUMN_SQLDT = "sqldt";
    private static final String COLUMN_COST = "cost";
    private static final String COLUMN_PAID = "paid";
    private static final String COLUMN_BALANCE = "balance";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_MEAL = "mealtype";
    String empIdMakingChoiceBreak;
    TextView empIdMakingChoiceTvBreak;
    HashMap<String,String> empChoiceBreak;

    String empIdMakingChoiceLunch;
    TextView empIdMakingChoiceTvLunch;
    HashMap<String,String> empChoiceLunch;
String empId;
    String balanceFromTransac;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_menu);
        Intent intent = getIntent();
        empId = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        DbTool dbAccess= new DbTool(this);

// test purpose
        DbOrder dbOrder = new DbOrder(this);

        HashMap<String,String> allTransactionsBalance ;
        allTransactionsBalance = dbOrder.getBalance(empId);

        balanceFromTransac = allTransactionsBalance.get(COLUMN_BALANCE);

        //test purpose

        HashMap<String,String> empDetail = dbAccess.searchEmpDetails(empId);
        name= empDetail.get("name");
        balance = empDetail.get("balance");
        email = empDetail.get("email");
empNameTv = (TextView) findViewById(R.id.nameTv);
        empIdTv = (TextView) findViewById(R.id.idTv);
       empEmailTv  = (TextView) findViewById(R.id.emailTv);
        empBalanceTv = (TextView)findViewById(R.id.balanceTv);
        empNameTv.setText(name);
        empEmailTv.setText(email);
        empIdTv.setText(empId);
        empBalanceTv.append(" "+balanceFromTransac);





    }

    public void addToLunch(View view) {




        empChoiceLunch = new HashMap<String, String>();
     empIdMakingChoiceTvLunch= (TextView)findViewById(R.id.idTv);
       empIdMakingChoiceLunch = empIdMakingChoiceTvLunch.getText().toString();


   HashMap<String,String> priceMap= priceDb.getPrice("'lunch'");
        String priceLunch = priceMap.get("price");
        empChoiceLunch.put(COLUMN_ID,empIdMakingChoiceLunch);
        empChoiceLunch.put(COLUMN_UNIQUE,String.valueOf(System.currentTimeMillis()));
        empChoiceLunch.put(COLUMN_MEAL,"lunch");
        empChoiceLunch.put(COLUMN_COST,priceLunch);
        empChoiceLunch.put(COLUMN_PAID,"no");
        empChoiceLunch.put(COLUMN_BALANCE,priceLunch);
        empChoiceLunch.put(COLUMN_STATUS,"pending");
        orderDb.insertOrder(empChoiceLunch);









        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void addToBreakfast(View view) {

      empChoiceBreak = new HashMap<String, String>();
        empIdMakingChoiceTvBreak= (TextView)findViewById(R.id.idTv);
  empIdMakingChoiceBreak = empIdMakingChoiceTvBreak.getText().toString();


        HashMap<String,String> priceMap= priceDb.getPrice("'break'");
        String priceBreakfast = priceMap.get("price");
        empChoiceBreak.put(COLUMN_ID,empIdMakingChoiceBreak);
        empChoiceBreak.put(COLUMN_UNIQUE,String.valueOf(System.currentTimeMillis()));
        empChoiceBreak.put(COLUMN_MEAL,"breakfast");
        empChoiceBreak.put(COLUMN_COST,priceBreakfast);
        empChoiceBreak.put(COLUMN_PAID,"no");
        empChoiceBreak.put(COLUMN_BALANCE,priceBreakfast);
        empChoiceBreak.put(COLUMN_STATUS,"pending");
        orderDb.insertOrder(empChoiceBreak);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
