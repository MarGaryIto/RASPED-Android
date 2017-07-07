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

public class FragAdmPersonal extends Fragment {

    ListView lv_ad_personal;

    public FragAdmPersonal() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return inflater.inflate(R.layout.frag_ad_personal, container, false);
    }

    //para llenar este fragment se usa el metodo onActivityCreated
    //algunas variables y metodos de este fragment funcionan diferente en comparacion de un activity
    //el siguiente metodo se encarga de llenar el listView de personal
    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        lv_ad_personal = (ListView) getActivity().findViewById(R.id.lv_ad_personal);

        //el context en un fragment se declara asi
        Context context = this.getActivity();

        //generar el archivo que contenga los datos en cache
        SharedPreferences sp_datosPersonal =
                context.getSharedPreferences(getString(R.string.sp_datosPersonal_key),
                        Context.MODE_PRIVATE);

        //almacena la cantidad de personas en una variable
        int dimensionPersonal =
                sp_datosPersonal.getInt(getString(R.string.sp_dimensionArrayPersonal_key),1);

        //inicializar el arreglo que contendra el personal junto con su dimencion
        final String[] elementos = new String[dimensionPersonal];

        //llenado del arreglo, si no se llenan todos y se llama devuelve un error por campos nulos
        for (int i=0;dimensionPersonal>i;i++){
            String personal =
                    sp_datosPersonal.getString(getString(R.string.sp_nombrePersonal_key)+i,"null");
            elementos[i] = personal;
        }

        //construccion de un arreglo que actuara como adapter para el ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                android.R.layout.simple_expandable_list_item_1, elementos);
        //asignacion del adapter al ListVier
        lv_ad_personal.setAdapter(adapter);

        //Escuchador para los items del ListView que se allan presionado
        lv_ad_personal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //en caso de presionar algun elemento del ListView se ejecutara este codigo
                Toast.makeText(getActivity().getApplicationContext(), elementos[position],
                        Toast.LENGTH_SHORT).show();
            }//onItemClick
        });//setOnItemClickListener
    }//onActivityCreated
}//FragAdmPersonal
