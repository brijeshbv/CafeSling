package com.example.brijeshvishwanat.cafesling;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Brijesh.Vishwanat on 7/23/2016.
 */
public class ListViewDisplay extends AppCompatActivity {
    public static String EXTRA_MESSAGE= "EXTRA_MESSAGE";
    String id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        setContentView(R.layout.to_display_listview);

        DbTool toCreateDb = new DbTool(this);
        ArrayList<HashMap<String,String>> employeeList= toCreateDb.getAllEmployees();


        ListAdapter adapter = new SimpleAdapter(ListViewDisplay.this,employeeList,R.layout.employee_info_to_list,
                new String[]{"empid","name"}, new int[]{R.id.idEmpTv,R.id.nameEmpTv});
        ListView listView= (ListView) findViewById(R.id.EmpListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

String empArray =String.valueOf(adapterView.getItemAtPosition(position));
                int length = empArray.length();
                id = empArray.substring(length-4,length-1);
                Intent sendDetail = new Intent(ListViewDisplay.this,EmployeeDetail.class);
                sendDetail.putExtra(EXTRA_MESSAGE,id);
                startActivity(sendDetail);
            }
;
        });

        //LayoutInflater listInflate = getLayoutInflater();
       // View v=  listInflate.inflate(R.layout.list_view_employee,null);
       // LinearLayout linearLayout= (LinearLayout)findViewById(R.id.listviewgoeshere);
        //linearLayout.addView(v);

        super.onCreate(savedInstanceState);
    }
}
