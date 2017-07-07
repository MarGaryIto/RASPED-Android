package com.example.adminprospera.rasped_tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class FragAdmHorarios extends Fragment  {

    public FragAdmHorarios() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return inflater.inflate(R.layout.frag_ad_horarios, container, false);
    }

    //para llenar este fragment se usa el metodo onActivityCreated
    //algunas variables y metodos de este fragment funcionan diferente en comparacion de un activity
    //el siguiente metodo se encarga de llenar el listView de personal
    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        ListView lv_ad_horarios = (ListView) getActivity().findViewById(R.id.lv_ad_horarios);

        //el context en un fragment se declara asi
        Context context = this.getActivity();

        //generar el archivo que contenga los datos en cache
        SharedPreferences sp_datosHorario =
                context.getSharedPreferences(getString(R.string.sp_datosHorarios_key),Context.MODE_PRIVATE);

        //almacena la cantidad de personas en una variable
        int dimensionHorarios = sp_datosHorario.getInt(getString(R.string.sp_dimensionHorarios_key),1);

        //inicializar el arreglo que contendra el personal junto con su dimencion
        final String[] elementos = new String[dimensionHorarios];

        //llenado del arreglo, si no se llenan todos y se llama devuelve un error por campos nulos
        for (int i=0;dimensionHorarios>i;i++){
            String horario = sp_datosHorario.getString(getString(R.string.sp_hrNombre_key)+i,"null");
            elementos[i] = horario;
        }

        //construccion de un arreglo que actuara como adapter para el ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                android.R.layout.simple_expandable_list_item_1, elementos);
        //asignacion del adapter al ListVier
        lv_ad_horarios.setAdapter(adapter);

        //Escuchador para los items del ListView que se allan presionado
        lv_ad_horarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //en caso de presionar algun elemento del ListView se ejecutara este codigo
                Toast.makeText(getActivity().getApplicationContext(), elementos[position],
                        Toast.LENGTH_SHORT).show();

            }//void onItemClick
        });//setOnItemClickListener
    }//void onActivityCreated
}
