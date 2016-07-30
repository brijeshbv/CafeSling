package com.example.brijeshvishwanat.cafesling;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class NewUserDb extends AppCompatActivity {


    EditText nameEmpEt ;
    EditText idEt;
    EditText emailIdEt;
    public static final String initialBalance="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_db);

        Intent getFromMainActivity = getIntent();

        nameEmpEt=(EditText)findViewById(R.id.empName);
        idEt=(EditText)findViewById(R.id.empNewId);
        emailIdEt=(EditText)findViewById(R.id.empEmail);






    }

    public void goToMain(View view) {


        String nameEmp = nameEmpEt.getText().toString();
        String id = idEt.getText().toString();
        String emailId = emailIdEt.getText().toString();

/*
        ArrayList<HashMap<String,String>> employeeList= toCreateDb.getAllEmployees();
        ListAdapter adapter = new SimpleAdapter(NewUserDb.this,employeeList,R.layout.employee_info_to_list,
                new String[]{"id","nameEmp","emailId"}, new int[]{R.id.idEmpTv,R.id.nameEmpTv,R.id.emailEmpTv});
        ListView listView= (ListView) findViewById(R.id.EmpListView);
        listView.setAdapter(adapter);
*/
if ( nameEmp.length() != 0 && id.length() != 0 && emailId.length() != 0){


    HashMap<String,String> queryValuesMap = new HashMap<String, String>();
    queryValuesMap.put("empid",id);
    queryValuesMap.put("name",nameEmp);
    queryValuesMap.put("email",emailId);
    queryValuesMap.put("balance",initialBalance);
    DbTool toCreateDb = new DbTool(this);
    toCreateDb.insertEmpData(queryValuesMap);

    Intent intent = new Intent(getApplicationContext(), AdminPage.class);
    startActivity(intent);
    finish();

}else if ( nameEmp.length() == 0 || id.length() == 0 || emailId.length() == 0){
    Toast.makeText(this,"One of the fields is empty",Toast.LENGTH_SHORT).show();
}

    }
}
