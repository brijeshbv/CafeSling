package com.example.brijeshvishwanat.cafesling;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Brijesh.Vishwanat on 7/25/2016.
 */
public class TabFragment1 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View tabView = inflater.inflate(R.layout.tab_fragment_set_prices, container, false);
        //setContentView(R.layout.listview_prices);

        final DbPrice mealList = new DbPrice(getContext());
        ArrayList<HashMap<String, String>> mealListHash = mealList.getAllMeals();


        ListAdapter adapter = new SimpleAdapter(getContext(), mealListHash, R.layout.menu_into_list,
                new String[]{"mealtype", "price"}, new int[]{R.id.mealType, R.id.mealPrice});
        ListView listView = (ListView) tabView.findViewById(R.id.mealListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, long l) {

                LayoutInflater inflater = getActivity().getLayoutInflater();
                View priceEdit = inflater.inflate(R.layout.setprice, null);


                final EditText priceSetEt = (EditText) priceEdit.findViewById(R.id.editedPrice);


                final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
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


                        String mealFromList = ((TextView) view.findViewById(R.id.mealType)).getText().toString();

                        HashMap<String, String> mealUpHash = new HashMap<String, String>();
                        final String priceSet = priceSetEt.getText().toString();
                        mealUpHash.put("mealtype", mealFromList);
                        mealUpHash.put("price", priceSet);
                        mealList.updatePrice(mealUpHash);
                        Intent intent = new Intent(getContext(), AdminPage.class);
                        startActivity(intent);

                    }
                });

                AlertDialog dialog = alert.create();
                dialog.show();


            }
        });

        return tabView;
    }
}
