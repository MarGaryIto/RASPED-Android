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

public class FragAdmPuestos extends Fragment {

    public FragAdmPuestos() {
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
        return inflater.inflate(R.layout.frag_ad_puestos, container, false);
    }

    //para llenar este fragment se usa el metodo onActivityCreated
    //algunas variables y metodos de este fragment funcionan diferente en comparacion de un activity
    //el siguiente metodo se encarga de llenar el listView de personal
    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        ListView lv_ad_puestos = (ListView) getActivity().findViewById(R.id.lv_ad_puestos);

        //el context en un fragment se declara asi
        Context context = this.getActivity();

        //generar el archivo que contenga los datos en cache
        SharedPreferences sp_datosPuestos =
                context.getSharedPreferences(getString(R.string.sp_datosPuestos_key),Context.MODE_PRIVATE);

        //almacena la cantidad de personas en una variable
        int dimensionPuestos = sp_datosPuestos.getInt(getString(R.string.sp_dimensionPuestos_key),1);

        //inicializar el arreglo que contendra el personal junto con su dimencion
        final String[] elementos = new String[dimensionPuestos];

        //llenado del arreglo, si no se llenan todos y se llama devuelve un error por campos nulos
        for (int i=0;dimensionPuestos>i;i++){
            String puesto = sp_datosPuestos.getString(getString(R.string.sp_nombrePuesto_key)+i,"null");
            elementos[i] = puesto;
        }

        //construccion de un arreglo que actuara como adapter para el ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                android.R.layout.simple_expandable_list_item_1, elementos);
        //asignacion del adapter al ListVier
        lv_ad_puestos.setAdapter(adapter);

        //Escuchador para los items del ListView que se allan presionado
        lv_ad_puestos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //en caso de presionar algun elemento del ListView se ejecutara este codigo
                Toast.makeText(getActivity().getApplicationContext(), elementos[position],
                        Toast.LENGTH_SHORT).show();

            }//void onItemClick
        });//setOnItemClickListener
    }//void onActivityCreated
}
