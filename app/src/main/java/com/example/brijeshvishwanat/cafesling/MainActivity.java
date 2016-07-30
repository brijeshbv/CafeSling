package com.example.brijeshvishwanat.cafesling;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
public String dateToday;

 public static String EXTRA_MESSAGE= "EXTRA_MESSAGE";

    //GRG

    public String PasswordDatabase = "passwordDataBase";
    public String adminPassword = "adminPassword";
    public String cashInHand = "cashinhand";
    //GRG
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_main);

dateToday=getDateToday();
        FragmentManager fragmentManager= getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Configuration configuration = getResources().getConfiguration();
        if(configuration.orientation== Configuration.ORIENTATION_LANDSCAPE)
        {
            LandscapeMain landscapeMain = new LandscapeMain();
            fragmentTransaction.replace(android.R.id.content,landscapeMain);
        }else {
            PortraitMain  portraitMain= new PortraitMain();
            fragmentTransaction.replace(android.R.id.content,portraitMain);
        }
        fragmentTransaction.commit();


//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DbPrice dbPrice = new DbPrice(this);


        //GRG
        //SharedPreferences sharedPreferences = getPreferences(MODE_WORLD_WRITEABLE);
        SharedPreferences sharedPreferences = getSharedPreferences(PasswordDatabase, Context.MODE_PRIVATE);

        String var = sharedPreferences.getString(adminPassword, "0");
        if(var.equals("0")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(adminPassword, "sling");
            editor.commit();
        }
        String var2 = sharedPreferences.getString(cashInHand, "null");
        if(var2.equals("null")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(cashInHand, "0");
            editor.commit();
        }
        //GRG
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onEmpIdReceive(View view) {

        DbTool dbTool = new DbTool(this);

        EditText empIdEt = (EditText) findViewById(R.id.empId);
        String empId = empIdEt.getText().toString();

        HashMap<String, String> empExists = dbTool.searchEmpDetails(empId);
        if (empExists.size() != 0) {

            Intent intentChoice = new Intent(this, EmployeeChoice.class);
            intentChoice.putExtra(EXTRA_MESSAGE, empId);
            startActivity(intentChoice);
        }else if (empExists.size() == 0)
        {
            Snackbar.make((View)findViewById(R.id.portCoordinatorLayout)," Wrong user ID", Snackbar.LENGTH_SHORT).show();
          // Toast.makeText(getBaseContext(), "User does not exist", Toast.LENGTH_SHORT).show();

        }
    }



//GRG
    public void adminLogin(View view) {

        LayoutInflater inflater = getLayoutInflater();
        View adminLogin = inflater.inflate(R.layout.admin_login, null);

        final EditText etAdminPassword = (EditText) adminLogin.findViewById(R.id.adminPassword);
        final CheckBox cbShowPassword = (CheckBox) adminLogin.findViewById(R.id.checkBox);

        cbShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etAdminPassword.setTransformationMethod(null);
                } else {
                    etAdminPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Admin Login");
        alert.setView(adminLogin);

        //To Disable back button and outside touch
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }

        });

        alert.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                SharedPreferences sharedPreferences = getSharedPreferences(PasswordDatabase, Context.MODE_PRIVATE);

                //if(etAdminPassword.getText().toString().equals(adminPassword)) {
                //if(etAdminPassword.getText().toString().equals(getString(R.string.admin_password))) {
                if(etAdminPassword.getText().toString().equals(sharedPreferences.getString(adminPassword,"1"))) {
                    //Toast.makeText(getBaseContext(), "Right password", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), AdminPage.class);
                    startActivity(intent);
                }
                else if(sharedPreferences.getString(adminPassword,"1").equals("1")){
                    Toast.makeText(getBaseContext(), "No admin password exits", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                }

            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();
    }

    //GRG
public String getDateToday(){
    Date date = new Date();

    return date.toString();
}
//GRG
    public void changeAdminPassword(){
        LayoutInflater inflater = getLayoutInflater();
        View adminPasswordChange = inflater.inflate(R.layout.admin_password_change, null);


        final EditText etCurrentAdminPassword = (EditText) adminPasswordChange.findViewById(R.id.etCurrentAdminPassword);
        final EditText etNewAdminPassword = (EditText) adminPasswordChange.findViewById(R.id.etNewAdminPassword);
        final EditText etRetypeNewAdminPassword = (EditText) adminPasswordChange.findViewById(R.id.etRetypeNewAdminPassword);
        final CheckBox cbShowPassword = (CheckBox) adminPasswordChange.findViewById(R.id.cbAdminChangePassword);

        cbShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etCurrentAdminPassword.setTransformationMethod(null);
                    etNewAdminPassword.setTransformationMethod(null);
                    etRetypeNewAdminPassword.setTransformationMethod(null);
                } else {
                    etCurrentAdminPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etNewAdminPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etRetypeNewAdminPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        final AlertDialog.Builder alertChangeAdminPassword = new AlertDialog.Builder(this);
        alertChangeAdminPassword.setTitle("Change Password");
        alertChangeAdminPassword.setView(adminPasswordChange);

        //To Disable back button and outside touch
        alertChangeAdminPassword.setCancelable(false);
        alertChangeAdminPassword.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }

        });

        alertChangeAdminPassword.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String PasswordDatabase = "passwordDataBase";
                String adminPassword = "adminPassword";
                SharedPreferences sharedPreferences = getSharedPreferences(PasswordDatabase, Context.MODE_PRIVATE);

                //if(etAdminPassword.getText().toString().equals(adminPassword)) {
                if(etCurrentAdminPassword.getText().toString().equals(sharedPreferences.getString(adminPassword,"0"))) {

                    if(etNewAdminPassword.getText().toString().equals(etRetypeNewAdminPassword.getText().toString())) {

                        //String p = etNewAdminPassword.getText().toString();
                        sharedPreferences.edit().putString(adminPassword,etNewAdminPassword.getText().toString());
                        sharedPreferences.edit().commit();
                        Toast.makeText(getBaseContext(), "New password Set", Toast.LENGTH_SHORT).show();

                        //Intent intent = new Intent(getApplicationContext(), AdminPage.class);
                        //startActivity(intent);
                    }
                    else
                    {Toast.makeText(getBaseContext(), "New password Don't Match", Toast.LENGTH_SHORT).show();}
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Current password is Wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });

        AlertDialog dialog = alertChangeAdminPassword.create();
        dialog.show();

    }

    //GRG
}
