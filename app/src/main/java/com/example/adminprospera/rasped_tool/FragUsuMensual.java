package com.example.adminprospera.rasped_tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class FragUsuMensual extends Fragment {

    private Adapter adapterListView;
    ArrayList<ModelAdministrador> model = new ArrayList<>();
    private Boolean lvLleno = false;

    public FragUsuMensual() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflar el layout para este fragment
        View view = inflater.inflate(R.layout.frag_usu_mensual, container, false);

        //evaluacion: si el listView esta vacio se poblara
        if (!lvLleno) {
            poblarModelAdministrador();
        }

        adapterListView = new AdapterAdministrador(getActivity(), model);

        //traer el elemento spinner para trabajar con el
        ListView lv_us_mensual = (ListView) view.findViewById(R.id.lv_us_mensual);

        lv_us_mensual.setAdapter((ListAdapter) adapterListView);

        //lv_adm_asistencias.setOnItemClickListener(listenerListView);

        return view;
    }

    private void poblarModelAdministrador() {

        SharedPreferences sp_datosFaltasCupo = this.getActivity().getSharedPreferences(
                "sp_datosFaltasCupo", Context.MODE_PRIVATE);
        //Instanciar las clases
        ModelAdministrador modelAdministrador;

        int dimension = sp_datosFaltasCupo.getInt("registros", 0);

        for (int i = 0; i < dimension; i++) {
            modelAdministrador = new ModelAdministrador();
            //asignar las variables
            modelAdministrador.setNombrePersonal(sp_datosFaltasCupo.getString("fecha" + i, "null"));
            modelAdministrador.setCupo("");
            modelAdministrador.setFecha(sp_datosFaltasCupo.getString("hr_entrada" + i, "null"));
            modelAdministrador.setHora(sp_datosFaltasCupo.getString("hr_salida" + i, "null"));
            modelAdministrador.setId_personal(sp_datosFaltasCupo.getString("i" + i, "null"));
            modelAdministrador.setCodigo(i);

            //una vez asignadas las variables, se añaden al model
            model.add(modelAdministrador);
        }

        lvLleno = true;
    }
}
