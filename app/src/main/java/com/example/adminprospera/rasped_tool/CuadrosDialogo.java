package com.example.adminprospera.rasped_tool;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

public class CuadrosDialogo extends DialogFragment {

    public void cuadroConfirmacion(String Message,String Title){
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(Message)
                .setTitle(Title);

        // 3. Add the buttons
        builder.setPositiveButton(R.string.st_aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.setNegativeButton(R.string.st_cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        // 4. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        dialog.show();
    }


}
