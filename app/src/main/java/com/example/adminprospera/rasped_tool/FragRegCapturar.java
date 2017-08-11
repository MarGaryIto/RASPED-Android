package com.example.adminprospera.rasped_tool;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FragRegCapturar extends Fragment {

    Button bt_reg_entrada_cap,bt_reg_inicioComida_cap,bt_reg_finComida_cap,bt_reg_salida_cap;
    ObtenerFechasHoras obtenerFechasHoras = new ObtenerFechasHoras();
    CuadrosDialogo cuadrosDialogo = new CuadrosDialogo();
    String tiempos = "";//,"hr_comida_i","hr_comida_f","hr_salida"};

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
        View view = inflater.inflate(R.layout.frag_reg_capturar, container, false);

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
                //establecer el tipo de tiempo de registro
                tiempos = "hr_entrada";
                //obtener el cupo
                obtenerCupo();
            }else if(id == bt_reg_inicioComida_cap.getId()){
                //establecer el tipo de tiempo de registro
                tiempos = "hr_comida_i";
                //obtener el cupo
                obtenerCupo();
            }else if(id == bt_reg_finComida_cap.getId()){
                //establecer el tipo de tiempo de registro
                tiempos = "hr_comida_f";
                //obtener el cupo
                obtenerCupo();
            }else if(id == bt_reg_salida_cap.getId()){
                //establecer el tipo de tiempo de registro
                tiempos = "hr_salida";
                //obtener el cupo
                obtenerCupo();
            }
            editor.apply();
        }
    };

    private void obtenerCupo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Title");

        // Set up the input
        final EditText input = new EditText(getActivity());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //aqui se extrae el cupo cuando se activa el boton aceptar
                Context c = getContext();
                String resul = input.getText().toString();
                //despues el cupo se manda a registrar
                crearRegistro(resul);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void crearRegistro(String cupo){

        String fecha = obtenerFechasHoras.fecha();
        String hora = obtenerFechasHoras.hora();

        HttpHandler handler = new HttpHandler();
        String txt = handler.postAsistencia(tiempos,cupo,fecha,hora);

        if(txt.equals("true")){
            cuadrosDialogo.cuadroDialogo(
                    getString(R.string.st_aceptar),
                    getString(R.string.st_registroExitoso),
                    getString(R.string.st_hey),
                    getActivity()
            );
        }else {
            cuadrosDialogo.cuadroDialogo(
                    getString(R.string.st_aceptar),
                    getString(R.string.ms_usuarioNoEncontrado),
                    getString(R.string.st_hey),
                    getActivity()
            );
        }
    }
}
