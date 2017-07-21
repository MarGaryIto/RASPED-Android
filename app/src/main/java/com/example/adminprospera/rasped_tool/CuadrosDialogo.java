package com.example.adminprospera.rasped_tool;


import android.app.Activity;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

public class CuadrosDialogo extends DialogFragment {

    public void cuadroDialogo(String Boton, String Message, String Title, Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(Message)
                .setTitle(Title)
                .setCancelable(false)
                .setNeutralButton(Boton,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void cuadroDialogoConInput(String Message, String Title, Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final EditText input = new EditText(activity);
        builder.setMessage(Message)
                .setTitle(Title)
                .setView(input)
                .setPositiveButton(getString(R.string.st_aceptar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton(getString(R.string.st_cancelar),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

        AlertDialog alert = builder.create();
        alert.show();
    }

}
