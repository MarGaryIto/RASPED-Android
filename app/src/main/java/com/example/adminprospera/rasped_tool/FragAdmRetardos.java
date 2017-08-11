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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FragAdmRetardos extends Fragment {

    private Adapter adapterListView;
    ArrayList<ModelAdministrador> model = new ArrayList<>();
    private Boolean lvLleno = false;

    public FragAdmRetardos() {
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
        View view = inflater.inflate(R.layout.fragment_frag_adm_retardos, container, false);

        //traer el elemento spinner para trabajar con el
        ListView lv_adm_retardos = (ListView) view.findViewById(R.id.lv_adm_retardos);

        //evaluacion: si el listView esta vacio se poblara
        if (!lvLleno){
            poblarModelAdministrador();
        }

        //crear el adaptador que contiene los datos del personal
        adapterListView = new AdapterAdministrador(getActivity(),model);

        //asignar el adaptador al listView
        lv_adm_retardos.setAdapter((ListAdapter) adapterListView);

        //una vez lleno el listView, se configura su estado a lleno
        lvLleno = true;

        //asignar un listener al listView
        lv_adm_retardos.setOnItemClickListener(listenerListView);

        return view;
    }

    //metodo privado para poblar el model para el ListView
    private void poblarModelAdministrador(){

        SharedPreferences sp_retardos = this.getActivity().getSharedPreferences(
                getString(R.string.sp_retardos_key), Context.MODE_PRIVATE);
        //Instanciar las clases
        ModelAdministrador modelAdministrador;

        int dimension = sp_retardos.getInt(
                getString(R.string.sp_numRetardos_key),0);

        for(int i = 0;i<dimension;i++){
            modelAdministrador = new ModelAdministrador();
            //asignar las variables
            modelAdministrador.setNombrePersonal(sp_retardos.getString(
                    "retardos"+getString(R.string.sp_nombre_personal_key)+i,"null"));

            modelAdministrador.setCupo(sp_retardos.getString(
                    "retardos"+getString(R.string.sp_cupoPersonal_key)+i,"null"));


            modelAdministrador.setFecha(sp_retardos.getString(
                    "retardos"+getString(R.string.sp_fecha_key)+i,"null"));

            modelAdministrador.setHora(sp_retardos.getString(
                    "retardos"+getString(R.string.sp_hr_entrada_key)+i,"null"));

            modelAdministrador.setCodigo(i);

            //una vez asignadas las variables, se añaden al model
            model.add(modelAdministrador);
        }

        lvLleno=true;
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
