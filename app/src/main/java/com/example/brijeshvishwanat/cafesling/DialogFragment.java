package com.example.brijeshvishwanat.cafesling;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Brijesh.Vishwanat on 7/25/2016.
*/
 class MyDialogFragment extends DialogFragment {


    public void setId(String id) {
        this.id = id;
    }

    protected String id;
     boolean delete =false;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder theDialog = new AlertDialog.Builder(getActivity());
        theDialog.setTitle("Delete user");
        theDialog.setMessage("Are you Sure?");
        theDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DbTool dbAccess= new DbTool(getActivity());
                    dbAccess.deleteContact(id);
                    Intent intent = new Intent(getActivity(),AdminPage.class);
                    startActivity(intent);


            }
        });
        theDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getActivity(),AdminPage.class);
                startActivity(intent);

            }
        });
        return theDialog.create();
    }
}
