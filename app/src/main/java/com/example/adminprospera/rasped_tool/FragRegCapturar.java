package com.example.adminprospera.rasped_tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragRegCapturar extends Fragment {

    Button bt_reg_entrada_cap,bt_reg_inicioComida_cap,bt_reg_finComida_cap,bt_reg_salida_cap;

    public FragRegCapturar() {
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
        View view = inflater.inflate(R.layout.frag_reg_escanear, container, false);

        //Instanciar los botones
        bt_reg_entrada_cap = (Button) view.findViewById(R.id.bt_reg_entrada_cap);
        bt_reg_inicioComida_cap = (Button) view.findViewById(R.id.bt_reg_inicioComida_cap);
        bt_reg_finComida_cap = (Button) view.findViewById(R.id.bt_reg_finComida_cap);
        bt_reg_salida_cap = (Button) view.findViewById(R.id.bt_reg_salida_cap);

        //asignar un listener a cada boton
        bt_reg_entrada_cap.setOnClickListener(listener);
        bt_reg_inicioComida_cap.setOnClickListener(listener);
        bt_reg_finComida_cap.setOnClickListener(listener);
        bt_reg_salida_cap.setOnClickListener(listener);

        return view;
    }

    //escuchador o listener
    View.OnClickListener listener  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //id del boton presionado
            int id = v.getId();

            Context context = getActivity().getApplicationContext();
            SharedPreferences prefs = context.getSharedPreferences(getString(R.string.sp_tiempos_key),Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            //evaluar el id con cada boton para asignarle una respectiva tarea
            if (id == bt_reg_entrada_cap.getId()){
                //todos los botones escanearan
                editor.putString(getString(R.string.sp_tiempos_key), "hr_entrada");editor.apply();
                new IntentIntegrator(getActivity()).initiateScan();
                //al escanear se registrara entrada, salida o comida
            }else if(id == bt_reg_inicioComida_cap.getId()){
                editor.putString(getString(R.string.sp_tiempos_key), "hr_comida_i");editor.apply();
                new IntentIntegrator(getActivity()).initiateScan();
            }else if(id == bt_reg_finComida_cap.getId()){
                editor.putString(getString(R.string.sp_tiempos_key), "hr_comida_f");editor.apply();
                new IntentIntegrator(getActivity()).initiateScan();
            }else if(id == bt_reg_salida_cap.getId()){
                editor.putString(getString(R.string.sp_tiempos_key), "hr_salida");editor.apply();
                new IntentIntegrator(getActivity()).initiateScan();
            }
            editor.apply();
        }
    };
}
