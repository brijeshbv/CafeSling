package com.example.brijeshvishwanat.cafesling;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class PurchaseListView extends AppCompatActivity {

    private static final String Serial_NO = "slno";
    private static final String Voucher_NO = "voucherno";
    private static final String SQL_DateTime = "datentime";
    private static final String Paid_Amount = "amountpaid";
    private static final String Cash_in_Hand = "cashinhand";
    private static final String Remark = "remark";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_list_view);

        DbPurchase toCreateDb = new DbPurchase(this);
        ArrayList<HashMap<String,String>> purchasesList= toCreateDb.getAllPurchases();


        ListAdapter adapter = new SimpleAdapter(PurchaseListView.this,purchasesList, R.layout.purchase_info_to_list,
                new String[]{Serial_NO,Voucher_NO,SQL_DateTime,Paid_Amount,Cash_in_Hand,Remark}, new int[]{R.id.plvSlNo, R.id.plvVoucherNo, R.id.plvDateNTime, R.id.plvAmountPaid, R.id.plvCashInHand, R.id.plvRemark});
        ListView listView= (ListView) findViewById(R.id.PurchaseListView);
        listView.setAdapter(adapter);
    }
}
