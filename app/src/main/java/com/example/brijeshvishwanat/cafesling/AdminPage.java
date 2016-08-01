package com.example.brijeshvishwanat.cafesling;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminPage extends AppCompatActivity {

    EditText empIdforSearch;
    String breakfastPrice;
    int breakfastPriceInt;
    String lunchPrice;
    int lunchPriceInt;
    public static String EXTRA_MESSAGE= "EXTRA_MESSAGE";
EditText  searchIdEt;
    TextView cashInHandDisp;
    DbPrice dbPrice = new DbPrice(this);
    public String cashInHandVar;
    String PasswordDatabase = "passwordDataBase";
    String cashInHand = "cashinhand";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        Intent intent = getIntent();


        empIdforSearch=(EditText) findViewById(R.id.etSearchByEmployeeID);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Dashboard"));

        tabLayout.addTab(tabLayout.newTab().setText("Menu"));
        tabLayout.addTab(tabLayout.newTab().setText("Manage"));


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                // cashinhand
                viewPager.setCurrentItem(tab.getPosition());



            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

    }





    public void generateListOfMeals(final View view) {

setContentView(R.layout.listview_prices);

        final DbPrice mealList = new DbPrice(this);
        ArrayList<HashMap<String,String>> mealListHash= mealList.getAllMeals();


        ListAdapter adapter = new SimpleAdapter(this,mealListHash,R.layout.menu_into_list,
                new String[]{"mealtype","price"}, new int[]{R.id.mealType,R.id.mealPrice});
        ListView listView= (ListView) findViewById(R.id.mealListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView,final View view, final int position, long l) {

                LayoutInflater inflater = getLayoutInflater();
                View priceEdit = inflater.inflate(R.layout.setprice, null);


               final EditText priceSetEt = (EditText) priceEdit.findViewById(R.id.editedPrice);


                final AlertDialog.Builder alert = new AlertDialog.Builder(AdminPage.this);
                alert.setTitle("Edit Price");
                alert.setView(priceEdit);

                //To Disable back button and outside touch
                alert.setCancelable(false);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }

                });

                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                   String mealFromList=     ((TextView) view.findViewById(R.id.mealType)).getText().toString();

                        HashMap<String,String> mealUpHash = new HashMap<String, String>();
                        final String priceSet = priceSetEt.getText().toString();
                        mealUpHash.put("mealtype",mealFromList);
                        mealUpHash.put("price",priceSet);
                        mealList.updatePrice(mealUpHash);
                        Intent intent = new Intent(getApplicationContext(), AdminPage.class);
                        startActivity(intent);

                    }
                });

                AlertDialog dialog = alert.create();
                dialog.show();


            }
        });
    }

/*
    public void searchByEmployeeID(View view) {

        String balanceReturned =toCreateDb.searchEmpDetails(empIdforSearch.getText().toString());
      String toToast=  "The Balance of" +empIdforSearch+"is"+balanceReturned;
        Toast.makeText(getBaseContext(),toToast, Toast.LENGTH_SHORT).show();
    }
*/
    public void createListView(View view) {
        Intent listViewIntent = new Intent(getApplication(),ListViewDisplay.class);
        startActivity(listViewIntent);

    }

    public void searchEmployee(View view) {


        DbTool dbTool = new DbTool(this);
        searchIdEt = (EditText)findViewById(R.id.etSearchByEmployeeID);

        HashMap<String, String> empExists = dbTool.searchEmpDetails(searchIdEt.getText().toString());
        if (empExists.size() != 0) {
            Intent searchIntent = new Intent(this,EmployeeDetail.class);
            searchIntent.putExtra(EXTRA_MESSAGE,searchIdEt.getText().toString());
            startActivity(searchIntent);


        }else if (empExists.size() == 0)
        {
            Toast.makeText(getBaseContext(), "User does not exist", Toast.LENGTH_SHORT).show();

        }

    }

    public void addEmployee(View view) {
        Intent toNewUserIntent = new Intent(this,NewUserDb.class);
        startActivity(toNewUserIntent);
    }

    public void addMeal(View view) {
        setContentView(R.layout.addmeal);

    }

    public void setMeal(View view) {
        EditText mealTypeEt = (EditText)findViewById(R.id.mealType);
        EditText mealPriceEt = (EditText)findViewById(R.id.mealPrice);
        String mealType= mealTypeEt.getText().toString();
        String mealPrice = mealPriceEt.getText().toString();
        HashMap<String,String> newMeal = new HashMap<String, String>();
        newMeal.put("mealtype",mealType);
        newMeal.put("price",mealPrice);
        dbPrice.insertPrice(newMeal);
        Intent intent = new Intent(getApplicationContext(), AdminPage.class);
        startActivity(intent);



    }

    //GRG



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.change_password) {
            changeAdminPassword();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(adminPassword,etNewAdminPassword.getText().toString());
                        editor.apply();
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

    public void receiptsListView(View view) {
        Intent receiptsListViewIntent = new Intent(getApplication(),ReceiptsListView.class);
        startActivity(receiptsListViewIntent);
    }

    public void purchaseListView(View view) {
        Intent purchaseListViewIntent = new Intent(getApplication(),PurchaseListView.class);
        startActivity(purchaseListViewIntent);
        // finish();
    }

    public void makePayment(View view) {
        Intent purchaseIntent = new Intent(getApplication(),Purchase.class);
        startActivity(purchaseIntent);

    }



    //GRG
}
