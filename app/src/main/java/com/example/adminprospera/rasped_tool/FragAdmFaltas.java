package com.example.adminprospera.rasped_tool;

import android.content.Context;
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

    Spinner sp_adm_filtrar;
    private ListView lv_adm_asistencias;
    private Adapter adapterListView;
    ArrayList<ModelAdministrador> model = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflar el layout para este fragment
        View view = inflater.inflate(R.layout.fragment_frag_adm_asistencias, container, false);

        poblarModelAdministrador();

        adapterListView = new AdapterAdministrador(getActivity(),model);

        //traer el elemento spinner para trabajar con el
        sp_adm_filtrar = (Spinner) view.findViewById(R.id.sp_adm_filtrar);
        lv_adm_asistencias = (ListView) view.findViewById(R.id.lv_adm_asistencias);

        lv_adm_asistencias.setAdapter((ListAdapter) adapterListView);

        //usar la clase configurarSpinner para poblar y establecer actividades al sp_adm_filtrar
        configurarSpinner();

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

    }

    //metodo privado que filtra los registros a solo los de la semana actual
    private void filtrarSemanal(){

    }

    //metodo privado que filtra los registros a solo los del mes actual
    private void filtrarMensual(){

    }
    //metodo privado que mostrara un toast, de mensaje contendra variables
    private void mostrarToast(String mensaje){
        Context context = getContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, mensaje, duration);
        toast.show();
    }
}
