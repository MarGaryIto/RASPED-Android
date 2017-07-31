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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class FragAdmAsistencias extends Fragment {

    Spinner sp_adm_filtrar;
    private ListView lv_adm_asistencias;
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
        sp_adm_filtrar = (Spinner) view.findViewById(R.id.sp_adm_filtrar);
        lv_adm_asistencias = (ListView) view.findViewById(R.id.lv_adm_asistencias);

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

        //usar la clase configurarSpinner para poblar y establecer actividades al sp_adm_filtrar
        configurarSpinner();

        //asignar un listener al listView
        lv_adm_asistencias.setOnItemClickListener(listenerListView);

        return view;
    }

    //metodo privado para poblar el model para el ListView
    private void poblarModelAdministrador(){

        SharedPreferences sp_asistencias = this.getActivity().getSharedPreferences(
                getString(R.string.sp_asistencias_key), Context.MODE_PRIVATE);
        //Instanciar las clases
        ModelAdministrador modelAdministrador;

        int dimension = sp_asistencias.getInt(
                getString(R.string.sp_numAsistencias_key),0);

        for(int i = 0;i<dimension;i++){
            modelAdministrador = new ModelAdministrador();
            //asignar las variables
            modelAdministrador.setNombrePersonal(sp_asistencias.getString(
                    "asistencias"+getString(R.string.sp_nombre_personal_key)+i,"null"));

            modelAdministrador.setCupo(sp_asistencias.getString(
                    "asistencias"+getString(R.string.sp_cupoPersonal_key)+i,"null"));


            modelAdministrador.setFecha(sp_asistencias.getString(
                    "asistencias"+getString(R.string.sp_fecha_key)+i,"null"));

            modelAdministrador.setHora(sp_asistencias.getString(
                    "asistencias"+getString(R.string.sp_hr_entrada_key)+i,"null"));

            modelAdministrador.setCodigo(i);

            //una vez asignadas las variables, se añaden al model
            model.add(modelAdministrador);
        }
    }

    //metodo para llenar spinner para filtrar el contenido
    private void configurarSpinner(){

        //crear arreglo que contenga los encabezados del filtro
        String[] filtro = {
                getString(R.string.st_diario),
                getString(R.string.st_semanal),
                getString(R.string.st_mensual)};

        //colocar los encabezados del filtro al spinner
        sp_adm_filtrar.setAdapter(new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item,
                        filtro));

        //Asignar listener al spinner para establecer tarea al item/filtro seleccionado
        sp_adm_filtrar.setOnItemSelectedListener(listenerSpinner);
    }

    AdapterView.OnItemClickListener listenerListView = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                ModelAdministrador modelAdministrador = (ModelAdministrador) adapterListView.getItem(position);
                Toast.makeText(getActivity(),modelAdministrador.getCupo(),Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    };

    //listener el Spinner que contiene las actividades al ser accionado algun item
    AdapterView.OnItemSelectedListener listenerSpinner = new AdapterView.OnItemSelectedListener() {
        //el parametro primero es al presionar un item/filtro
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id){

            //se evalua el numero de item presionado, el texto no porque varia al cambiar idioma
            switch (pos){
                case 0:
                    filtrarDiario();
                    break;
                case 1:
                    filtrarSemanal();
                    break;
                case 2:
                    filtrarMensual();
                    break;
            }
        }

        //el parametro segundo obligadorio es al no tener algun item activo
        @Override
        public void onNothingSelected(AdapterView<?> parent){ /*obligatorio, aún vacio*/   }
    };



    //metodo privado que filtra los registros a solo los de la fecha actual
    private void filtrarDiario(){
        Context context = getContext();
        SharedPreferences sp_retardos = context.getSharedPreferences(getString(R.string.sp_retardos_key),Context.MODE_PRIVATE);
    }

    //metodo privado que filtra los registros a solo los de la semana actual
    private void filtrarSemanal(){

    }

    //metodo privado que filtra los registros a solo los del mes actual
    private void filtrarMensual(){

    }

    private void mostrarToast(String mensg){
        Toast.makeText(getActivity(),mensg,Toast.LENGTH_SHORT).show();
    }

}
