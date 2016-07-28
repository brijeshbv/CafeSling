package com.example.brijeshvishwanat.cafesling;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
    private static final String COLUMN_BALANCE = "balance";

    DbTool dbAccess= new DbTool(this);
    DbReceipt dbReceipt = new DbReceipt(this);
    DbOrder dbOrder = new DbOrder(this);
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

        //from order table


        HashMap<String,String> allTransactionsBalance ;
        allTransactionsBalance = dbOrder.getBalance(empId);

        balanceFromTransac = allTransactionsBalance.get(COLUMN_BALANCE);
        empBalanceTv.append(balanceFromTransac);


        super.onCreate(savedInstanceState);
    }

    public void removeUserFromDb(View view) {

        MyDialogFragment myDialogFragment= new MyDialogFragment();
        myDialogFragment.setId(id);
        myDialogFragment.show(getFragmentManager(),"THE DIALOG");



    }

    public void editUserInfo(View view) {

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
if (Integer.valueOf(etAmountPaidSt)> Integer.valueOf(balanceFromTransac))
{
    int overPaidAmount = Integer.valueOf(etAmountPaidSt)-Integer.valueOf(balanceFromTransac);

    updateBalanceOrder(balanceFromTransac);
    overpayUpdate(overPaidAmount);
    Intent intent = new Intent(getApplication(), AdminPage.class);
    startActivity(intent);


}else {
    updateBalanceOrder(etAmountPaidSt);
    Intent intent = new Intent(getApplication(), AdminPage.class);
    startActivity(intent);
}




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
