package com.example.brijeshvishwanat.cafesling;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class Purchase extends AppCompatActivity {

    public String cashInHandVar;
    String PasswordDatabase = "passwordDataBase";
    String cashInHand = "cashinhand";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        TextView purchaseCashInHand = (TextView)findViewById(R.id.purchaseCashInHand);


        SharedPreferences sharedPreferences = getSharedPreferences(PasswordDatabase, Context.MODE_PRIVATE);
        cashInHandVar = sharedPreferences.getString(cashInHand,"null");

        if(cashInHandVar.equals("null"))
        {
            Toast.makeText(getBaseContext(), "Error: No cashInHand Variable exits", Toast.LENGTH_SHORT).show();
        }
        else
        {
            purchaseCashInHand.append(cashInHandVar);
        }
    }

    public void cancel(View view) {

        finish();
    }

    public void purchaseOrder(View view) {

        final EditText etPurchaseAmountPaid  = (EditText) findViewById(R.id.etPurchaseAmountPaid);
        final String etPurchaseAmountPaidSt = etPurchaseAmountPaid.getText().toString();
        final EditText etVoucherNumber  = (EditText) findViewById(R.id.etVoucherNumber);
        final String etVoucherNumberSt = etVoucherNumber.getText().toString();
        final EditText etRemarks  = (EditText) findViewById(R.id.etRemarks);
        final String etRemarksSt = etRemarks.getText().toString();

        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(this);
        confirmDialog.setTitle("Purchase Order");
        confirmDialog.setMessage("Are you Sure?");
        confirmDialog.setCancelable(false);
        confirmDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                DbPurchase dbPurchase = new DbPurchase(getApplicationContext());
                //dbAccess.deleteContact(id);
                //dbAccess.deleteContact(empId);

// cash shared pref update
                SharedPreferences sharedPreferences = getSharedPreferences(PasswordDatabase, Context.MODE_PRIVATE);
                cashInHandVar = sharedPreferences.getString(cashInHand,"null");
                int newCashInHand = Integer.valueOf(cashInHandVar)- Integer.valueOf(etPurchaseAmountPaidSt);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(cashInHand,String.valueOf(newCashInHand));
                editor.commit();

                // cash shared pref update
                HashMap<String,String> queryValuesMap = new HashMap<String, String>();
                queryValuesMap.put("purchaseAmount",etPurchaseAmountPaidSt);
                queryValuesMap.put("voucherNumber",etVoucherNumberSt);
                queryValuesMap.put("remarks",etRemarksSt);

                queryValuesMap.put("cashInHand",String.valueOf(newCashInHand));

                dbPurchase.newPurchase(queryValuesMap);




                Intent intent = new Intent(getApplicationContext(),AdminPage.class);
                startActivity(intent);
                finish();

            }
        });
        confirmDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Intent intent = new Intent(getActivity(),AdminPage.class);
                // startActivity(intent);

            }
        });

        AlertDialog dialog2 = confirmDialog.create();
        dialog2.show();
    }
}
