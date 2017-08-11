package com.example.adminprospera.rasped_tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class FragUsuHoy extends Fragment {

    private ListAdapter adapterListView;
    ArrayList<ModelAdministrador> model = new ArrayList<>();
    private Boolean lvLleno = false;

    public FragUsuHoy() {
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
        View view = inflater.inflate(R.layout.frag_usu_hoy, container, false);

        //traer el elemento spinner para trabajar con el
        ListView lv_usAsistencias = (ListView) view.findViewById(R.id.lv_us_hoy);

        //evaluacion: si el listView esta vacio se poblara
        if (!lvLleno) {
            poblarModelAdministrador();
        }

        //crear el adaptador que contiene los datos del personal
        adapterListView = new AdapterAdministrador(getActivity(), model);

        //asignar el adaptador al listView
        lv_usAsistencias.setAdapter(adapterListView);

        //una vez lleno el listView, se configura su estado a lleno
        lvLleno = true;

        //asignar un listener al listView
        //lv_adm_asistencias.setOnItemClickListener(listenerListView);

        return view;
    }

    //metodo privado para poblar el model para el ListView
    private void poblarModelAdministrador() {

        SharedPreferences sp_datosAsistenciasCupo = this.getActivity().getSharedPreferences(
                "sp_datosAsistenciasCupo", Context.MODE_PRIVATE);
        //Instanciar las clases
        ModelAdministrador modelAdministrador;

        int dimension = sp_datosAsistenciasCupo.getInt("registros", 0);

        for (int i = 0; i < dimension; i++) {
            modelAdministrador = new ModelAdministrador();

            //asignar las variables
            modelAdministrador.setNombrePersonal(sp_datosAsistenciasCupo.getString("fecha" + i, "null"));
            modelAdministrador.setCupo("");
            modelAdministrador.setFecha(sp_datosAsistenciasCupo.getString("hr_entrada" + i, "null"));
            modelAdministrador.setHora(sp_datosAsistenciasCupo.getString("hr_salida" + i, "null"));
            modelAdministrador.setId_personal(sp_datosAsistenciasCupo.getString("i" + i, "null"));

            modelAdministrador.setCodigo(i);

            //una vez asignadas las variables, se añaden al model
            model.add(modelAdministrador);

        }
    }
}
