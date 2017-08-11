package com.example.adminprospera.rasped_tool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class FragAdmFaltas extends Fragment {

    private Adapter adapterListView;
    ArrayList<ModelAdministrador> model = new ArrayList<>();
    private Boolean lvLleno = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflar el layout para este fragment
        View view = inflater.inflate(R.layout.fragment_frag_adm_asistencias, container, false);

        //evaluacion: si el listView esta vacio se poblara
        if (!lvLleno){
            poblarModelAdministrador();
        }

        adapterListView = new AdapterAdministrador(getActivity(),model);

        //traer el elemento spinner para trabajar con el
        ListView lv_adm_asistencias = (ListView) view.findViewById(R.id.lv_adm_asistencias);

        lv_adm_asistencias.setAdapter((ListAdapter) adapterListView);

        lv_adm_asistencias.setOnItemClickListener(listenerListView);

        return view;
    }

    private void poblarModelAdministrador(){

        SharedPreferences sp_faltas = this.getActivity().getSharedPreferences(
                getString(R.string.sp_faltas_key), Context.MODE_PRIVATE);
        //Instanciar las clases
        ModelAdministrador modelAdministrador;

        int dimension = sp_faltas.getInt(
                getString(R.string.sp_numFaltas_key),0);

        for(int i = 0;i<dimension;i++){
            modelAdministrador = new ModelAdministrador();
            //asignar las variables
            modelAdministrador.setNombrePersonal(sp_faltas.getString(
                    "faltas"+getString(R.string.sp_nombre_personal_key)+i,"null"));

            modelAdministrador.setCupo(sp_faltas.getString(
                    "faltas"+getString(R.string.sp_cupoPersonal_key)+i,"null"));


            modelAdministrador.setFecha(sp_faltas.getString(
                    "faltas"+getString(R.string.sp_fecha_key)+i,"null"));

            modelAdministrador.setHora("");

            modelAdministrador.setCodigo(i);

            //una vez asignadas las variables, se añaden al model
            model.add(modelAdministrador);
        }

        lvLleno = true;
    }

    AdapterView.OnItemClickListener listenerListView = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                ModelAdministrador modelAdministrador = (ModelAdministrador) adapterListView.getItem(position);
                //extraer el id del personal seleccionado
                String cupo = modelAdministrador.getCupo();

                //preparar un intent que abrira el activity del usuario seleccionado
                Intent intent = (new Intent(getActivity(), UsuariosActivity.class));

                //enviar parametro: id_personal
                intent.putExtra("cupo",cupo);

                //ejecucion del activity
                startActivityForResult(intent,0);
            }catch (Exception e){
                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    };
}
