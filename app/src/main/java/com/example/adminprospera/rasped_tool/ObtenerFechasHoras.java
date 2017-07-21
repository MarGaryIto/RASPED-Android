package com.example.adminprospera.rasped_tool;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ObtenerFechasHoras {

    public String hora(){
        Date date = new Date();
        return DateFormat.getTimeInstance().format(date);
    }

    public String fecha(){

        long ahora = System.currentTimeMillis();
        Date fecha = new Date(ahora);
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy:MM:dd");
        //String salida =

        //Date date = new Date();
        return df.format(fecha);
    }

}
