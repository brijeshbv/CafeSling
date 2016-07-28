package com.example.brijeshvishwanat.cafesling;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ReceiptsListView extends AppCompatActivity {

    private static final String Serial_NO = "slno";
    private static final String RECEIPT_NO = "receiptno";
    private static final String Employee_ID = "empid";
    private static final String SQL_DateTime = "datentime";
    private static final String Paid_Amount = "amountpaid";
    private static final String BALANCE = "balance";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipts_list_view);

        DbReceipt toCreateDb = new DbReceipt(this);
        ArrayList<HashMap<String,String>> receiptsList= toCreateDb.getAllReceipts();


        ListAdapter adapter = new SimpleAdapter(ReceiptsListView.this,receiptsList, R.layout.receipts_info_to_list,
                new String[]{Serial_NO,RECEIPT_NO,Employee_ID,SQL_DateTime,Paid_Amount,BALANCE}, new int[]{R.id.rlvSlNo, R.id.rlvReceiptNo, R.id.rlvEmployeeID, R.id.rlvDateTime, R.id.rlvAmountPaid, R.id.rlvBalance});
        ListView listView= (ListView) findViewById(R.id.ReceiptListView);
        listView.setAdapter(adapter);
    }
}
