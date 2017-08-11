package com.example.adminprospera.rasped_tool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class FragAdmAsistencias extends Fragment {

    private ListAdapter adapterListView;
    ArrayList<ModelAdministrador> model = new ArrayList<>();
    private Boolean lvLleno = false;

    public FragAdmAsistencias() {
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
        View view = inflater.inflate(R.layout.fragment_frag_adm_asistencias, container, false);

        //traer el elemento spinner para trabajar con el
        ListView lv_adm_asistencias = (ListView) view.findViewById(R.id.lv_adm_asistencias);

        //evaluacion: si el listView esta vacio se poblara
        if (!lvLleno){
            poblarModelAdministrador();
        }

        //crear el adaptador que contiene los datos del personal
        adapterListView = new AdapterAdministrador(getActivity(),model);

        //asignar el adaptador al listView
        lv_adm_asistencias.setAdapter(adapterListView);

        //una vez lleno el listView, se configura su estado a lleno
        lvLleno = true;

        //asignar un listener al listView
        lv_adm_asistencias.setOnItemClickListener(listenerListView);

        return view;
    }

    //metodo privado para poblar un modelo, dicho modelo contendra los datos para poblar una lista
    private void poblarModelAdministrador(){

        //preparar archivo temporal sp_asistencias que contiene todas las asistencias extraidas de
        // la base de datos remota
        SharedPreferences sp_asistencias = this.getActivity().getSharedPreferences(
                getString(R.string.sp_asistencias_key), Context.MODE_PRIVATE);

        //prepara el modelo
        ModelAdministrador modelAdministrador;

        //almacenar en una variable el numero de asistencias registradas
        int dimension = sp_asistencias.getInt(getString(R.string.sp_numAsistencias_key),0);

        SharedPreferences sp_datosPersonal = this.getActivity().getSharedPreferences(getString(R.string.sp_datosPersonal_key),Context.MODE_PRIVATE);
        String cupoCache = sp_datosPersonal.getString(getString(R.string.sp_cupoPersonal_key),"null");

        //ciclo for que se ejecutara segun el numero de asistencias
        for(int i = 0;i<dimension;i++){


            //preparar nuevo espacio para el modelo
            modelAdministrador = new ModelAdministrador();
            //asignar las variables  cupo, fecha etc. desde los valores temporales
            String cupo = sp_asistencias.getString(
                    "asistencias"+getString(R.string.sp_cupoPersonal_key)+i,"null");

            Log.e("cache: "+cupoCache,"cupo: "+cupo);

            //if (cupoCache.equals(cupo)){
                modelAdministrador.setNombrePersonal(sp_asistencias.getString(
                        "asistencias"+getString(R.string.sp_nombre_personal_key)+i,"null"));
                modelAdministrador.setCupo(cupo);
                modelAdministrador.setFecha(sp_asistencias.getString(
                        "asistencias"+getString(R.string.sp_fecha_key)+i,"null"));
                modelAdministrador.setHora(sp_asistencias.getString(
                        "asistencias"+getString(R.string.sp_hr_entrada_key)+i,"null"));
                modelAdministrador.setId_personal(sp_asistencias.getString(
                        "asistencias"+getString(R.string.sp_id_personal_key)+i,"null"));
                modelAdministrador.setCodigo(i);

                //una vez asignadas las variables, se añaden al model
                model.add(modelAdministrador);
            //}

        }
    }

    AdapterView.OnItemClickListener listenerListView = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                ModelAdministrador modelAdministrador = (ModelAdministrador) adapterListView.getItem(position);
                //extraer el id del personal seleccionado
                String cupo = modelAdministrador.getCupo();

                Toast.makeText(getActivity(),cupo,Toast.LENGTH_SHORT).show();

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
