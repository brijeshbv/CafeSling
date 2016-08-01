package com.example.brijeshvishwanat.cafesling;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;



/**
 * Created by Brijesh.Vishwanat on 7/25/2016.
 */
public class EmployeeDetail extends AppCompatActivity {

    public String name;
    public String balance;
    public  String email;
    public  String empId;
    public TextView empNameTv ;
    public TextView empEmailTv;
    public TextView empIdTv;
    public TextView empBalanceTv;
    EditText editName;
    EditText editEmail;
    EditText editId;
    String id;
    String balanceFromTransac;
    String prevMonthDate;
    private static final String COLUMN_BALANCE = "balance";
    Spinner dateSortBalance;
    public int cashInHand;
    SharedPreferences sharedpreferences;
    HashMap<String,String> balanceTillDate;

    DbTool dbAccess= new DbTool(this);
    DbReceipt dbReceipt = new DbReceipt(this);
    DbOrder dbOrder = new DbOrder(this);
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.employee_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.edit_user) {
            editUserInfo();
            return true;
        }
        if (id == R.id.delete_user) {
            removeUserFromDb();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent= getIntent();
        String id =intent.getStringExtra(ListViewDisplay.EXTRA_MESSAGE);




        setContentView(R.layout.employee_detail_for_admin);



        HashMap<String,String> empDetail = dbAccess.searchEmpDetails(id);
        name= empDetail.get("name");
        balance = empDetail.get("balance");
        email = empDetail.get("email");
        empId = empDetail.get("empid");
        empNameTv = (TextView) findViewById(R.id.textViewName);
        empIdTv = (TextView) findViewById(R.id.textViewId);
        empEmailTv  = (TextView) findViewById(R.id.textViewEmail);
        empBalanceTv = (TextView)findViewById(R.id.textViewBalance);
        empNameTv.append(name);
        empEmailTv.append(email);
        empIdTv.append(empId);

        //empBalanceTv.append(balance);

        //spinnerdatesort



        balanceTillDate= new HashMap<String, String>();

        dateSortBalance = (Spinner)findViewById(R.id.spinner);
        dateSortBalance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = sdf.format(c.getTime());
int currMonth =Integer.valueOf( strDate.substring(5,7));
                if (currMonth != 1) {
                    int prevMonth = currMonth - 1;
                    prevMonthDate = strDate.substring(0,4)+"-"+String.valueOf(String.format("%02d",prevMonth));
                } else if (currMonth== 1)
                {
                    int currYear =Integer.valueOf( strDate.substring(0,4));
                    int prevMonth = 12;
                    int preYear = currYear-1;
                   prevMonthDate = String.valueOf(preYear)+"-"+String.valueOf(prevMonth);
                }
                if (dateSortBalance.getSelectedItem().equals("Till Month End")){

                  balanceTillDate=  dbOrder.getBalanceForDate(empId,prevMonthDate);
         String balanceTillDatestr= balanceTillDate.get(COLUMN_BALANCE);


                    empBalanceTv.setText("Balance : "+balanceTillDatestr);
                } else if (dateSortBalance.getSelectedItem().equals("Overall") ){
                    HashMap<String,String> allTransactionsBalance ;
                    allTransactionsBalance = dbOrder.getBalance(empId);

                    balanceFromTransac = allTransactionsBalance.get(COLUMN_BALANCE);
                    empBalanceTv.setText("Balance : "+balanceFromTransac);
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                HashMap<String,String> allTransactionsBalance ;
                allTransactionsBalance = dbOrder.getBalance(empId);

                balanceFromTransac = allTransactionsBalance.get(COLUMN_BALANCE);
                empBalanceTv.append(balanceFromTransac);

            }
        });


        //spinnerdatesort
        //from order table





        super.onCreate(savedInstanceState);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }
    public void removeUserFromDb() {

        AlertDialog.Builder theDialog = new AlertDialog.Builder(this);
        theDialog.setTitle("Delete user");
        theDialog.setMessage("Are you Sure?");
        theDialog.setCancelable(false);
        theDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DbTool dbAccess= new DbTool(getApplicationContext());
                //dbAccess.deleteContact(id);
                dbAccess.deleteContact(empId);
                Intent intent = new Intent(getApplicationContext(),AdminPage.class);
                startActivity(intent);
                finish();

            }
        });
        theDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Intent intent = new Intent(getActivity(),AdminPage.class);
                // startActivity(intent);

            }
        });

        AlertDialog dialog1 = theDialog.create();
        dialog1.show();

    }


    public void editUserInfo() {

        setContentView(R.layout.edit_user_admin);
         editName = (EditText)findViewById(R.id.empNameEditEt);
        editEmail = (EditText)findViewById(R.id.empEmailEditEt);
         editId = (EditText)findViewById(R.id.empIdEditEt);
        editName.setText(name);
        editEmail.setText(email);
        editId.setText(empId);

    }

    public void updateEntry(View view) {

        String nameEmp = editName.getText().toString();
        String idToSet = editId.getText().toString();
        String emailId = editEmail.getText().toString();



        HashMap<String,String> queryValuesMap = new HashMap<String, String>();
        queryValuesMap.put("empid",idToSet);
        queryValuesMap.put("name",nameEmp);
        queryValuesMap.put("email",emailId);
        queryValuesMap.put("balance",balance);
      dbAccess.updateContact(queryValuesMap);
        Intent intent = new Intent(this,AdminPage.class);
        startActivity(intent);
        finish();

    }

    //GRG

    public void AmountPaid(View view) {

        final EditText etAmountPaid  = (EditText) findViewById(R.id.etAmountPaid);
        final String etAmountPaidSt = etAmountPaid.getText().toString();


        AlertDialog.Builder paymentAlert = new AlertDialog.Builder(this);
        paymentAlert.setTitle("Receipt");
        paymentAlert.setMessage("Amount received is Rs." +etAmountPaidSt);
        //To Disable back button and outside touch
        paymentAlert.setCancelable(false);

        paymentAlert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                HashMap<String,String> queryValuesMap = new HashMap<String, String>();
                queryValuesMap.put("empid",empId);
                queryValuesMap.put("amountPaid",etAmountPaidSt);


                queryValuesMap.put("balance",balanceFromTransac);

                dbReceipt.newReceipt(queryValuesMap);

                //for cash in hand

              //  sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

             //   cashInHand = cashInHandSharedPref + etAmountPaidSt;

if (Integer.valueOf(etAmountPaidSt)> Integer.valueOf(balanceFromTransac))
{
    int overPaidAmount = Integer.valueOf(etAmountPaidSt)-Integer.valueOf(balanceFromTransac);

    updateBalanceOrder(balanceFromTransac);
    overpayUpdate(overPaidAmount);
    Intent intent = new Intent(getApplication(), AdminPage.class);
    startActivity(intent);
finish();

}else {
    updateBalanceOrder(etAmountPaidSt);
    Intent intent = new Intent(getApplication(), AdminPage.class);
    startActivity(intent);
    finish();
}
                updateCashInHand(etAmountPaidSt);




            }


        });
        paymentAlert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }

        );

        AlertDialog dialog = paymentAlert.create();
        dialog.show();

    }



    private void updateCashInHand(String etAmountPaidSt) {

        String PasswordDatabase = "passwordDataBase";
        String cashInHand = "cashinhand";
        SharedPreferences sharedPreferences = getSharedPreferences(PasswordDatabase, Context.MODE_PRIVATE);
        String cashInHandVar = sharedPreferences.getString(cashInHand,"null");

        if(cashInHandVar.equals("null"))
        {
            Toast.makeText(getBaseContext(), "Error: No cashInHand Variable exits", Toast.LENGTH_SHORT).show();
        }
        else
        {
            int newCashInHand = Integer.valueOf(cashInHandVar) + Integer.valueOf(etAmountPaidSt);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(cashInHand,String.valueOf(newCashInHand));
            editor.apply();
        }
    }
    //GRG

    //update balance in order table

    private void updateBalanceOrder(String etAmountPaidSt) {


    dbOrder.balanceUpdate(etAmountPaidSt,empId);

        HashMap<String,String> empDetail = dbAccess.searchEmpDetails(empId);
        name= empDetail.get("name");
        balance = empDetail.get("balance");
        email = empDetail.get("email");
        empId = empDetail.get("empid");
        empNameTv = (TextView) findViewById(R.id.textViewName);
        empIdTv = (TextView) findViewById(R.id.textViewId);
        empEmailTv  = (TextView) findViewById(R.id.textViewEmail);
        empBalanceTv = (TextView)findViewById(R.id.textViewBalance);
        empNameTv.append(name);
        empEmailTv.append(email);
        empIdTv.append(empId);
        HashMap<String,String> allTransactionsBalance ;
        allTransactionsBalance = dbOrder.getBalance(empId);

        balanceFromTransac = allTransactionsBalance.get(COLUMN_BALANCE);
        empBalanceTv.append(balanceFromTransac);
    }

//when user overpays
    private void overpayUpdate(int overPaidAmount) {





      final String COLUMN_ID = "empid";
        final String COLUMN_UNIQUE = "uniqueno";
          final String COLUMN_SQLDT = "sqldt";
          final String COLUMN_COST = "cost";
          final String COLUMN_BALANCE = "balance";
          final String COLUMN_STATUS = "status";
         final String COLUMN_PAID = "paid";
        final String COLUMN_MEAL = "mealtype";


    HashMap<String,String>    empOverPays = new HashMap<String, String>();

        empOverPays.put(COLUMN_ID,empId);
        empOverPays.put(COLUMN_UNIQUE,String.valueOf(System.currentTimeMillis()));
        empOverPays.put(COLUMN_MEAL,"overpay");
        empOverPays.put(COLUMN_COST,"0");
        empOverPays.put(COLUMN_PAID,"no");
        overPaidAmount = - overPaidAmount;
        empOverPays.put(COLUMN_BALANCE,String.valueOf(overPaidAmount));
        empOverPays.put(COLUMN_STATUS,"pending");
        dbOrder.insertOrder(empOverPays);

        HashMap<String,String> empDetail = dbAccess.searchEmpDetails(empId);
        name= empDetail.get("name");
        balance = empDetail.get("balance");
        email = empDetail.get("email");
        empId = empDetail.get("empid");
        empNameTv = (TextView) findViewById(R.id.textViewName);
        empIdTv = (TextView) findViewById(R.id.textViewId);
        empEmailTv  = (TextView) findViewById(R.id.textViewEmail);
        empBalanceTv = (TextView)findViewById(R.id.textViewBalance);
        empNameTv.append(name);
        empEmailTv.append(email);
        empIdTv.append(empId);
        HashMap<String,String> allTransactionsBalance ;
        allTransactionsBalance = dbOrder.getBalance(empId);

        balanceFromTransac = allTransactionsBalance.get(COLUMN_BALANCE);
        empBalanceTv.append(balanceFromTransac);
    }




}
