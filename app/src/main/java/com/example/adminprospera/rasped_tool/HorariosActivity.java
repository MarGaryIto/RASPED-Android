package com.example.adminprospera.rasped_tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class HorariosActivity extends AppCompatActivity {

    private boolean editar = false;
    private String i = "0";
    EditText et_hr_nombreHorario,et_hr_entrada,et_hr_salida,et_hr_comidaInicio,et_hr_comidaFin,et_hr_tolerancia;
    CuadrosDialogo cuadrosDialogo = new CuadrosDialogo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);

        //obtener la peticion de editar o no algun horario
        editar = getIntent().getExtras().getBoolean("editar",false);

        //instancear los elementos del activity
        et_hr_nombreHorario = (EditText) findViewById(R.id.et_hr_nombreHorario);
        et_hr_entrada = (EditText) findViewById(R.id.et_hr_entrada);
        et_hr_salida = (EditText) findViewById(R.id.et_hr_salida);
        et_hr_comidaInicio = (EditText) findViewById(R.id.et_hr_comidaInicio);
        et_hr_comidaFin = (EditText) findViewById(R.id.et_hr_comidaFin);
        et_hr_tolerancia = (EditText) findViewById(R.id.et_hr_tolerancia);

        //prepara los campos para adicionar campo en caso de que asi sea
        if (editar){
            prepararCampos();
        }

    }

    //metodo para asignarle un toolbar personalizado a PersonalActivity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ab_editar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //metodo que devuelve algun item seleccionado del toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.it_aceptar:
                evaluarCampos();
                return true;
            case R.id.it_cancelar:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //metodo privato devuelve mensaje de error en caso de campos vacios
    private void evaluarCampos() {

        if ((et_hr_nombreHorario.getText().toString().length() > 0) &&
                (et_hr_entrada.getText().toString().length() > 0) &&
                (et_hr_salida.getText().toString().length() > 0) &&
                (et_hr_comidaInicio.getText().toString().length() > 0) &&
                (et_hr_comidaFin.getText().toString().length() > 0) &&
                (et_hr_tolerancia.getText().toString().length() > 0)){

            if (editar){
                editarDatosHorario();
            }else {
                insertarDatosHorario();
            }
        }else {

            cuadrosDialogo.cuadroDialogo(
                    getString(R.string.st_aceptar),
                    getString(R.string.ms_camposVacios),
                    getString(R.string.st_hey),
                    this);
        }
    }

    //metodo privado para anexar horarios
    private void insertarDatosHorario(){

        String  nombreHorario = et_hr_nombreHorario.getText().toString();
        String hrEntrada = et_hr_entrada.getText().toString();
        String hrSalida = et_hr_salida.getText().toString();
        String comidaI = et_hr_comidaInicio.getText().toString();
        String comidaF = et_hr_comidaFin.getText().toString();
        String tolerancia = et_hr_tolerancia.getText().toString();

        HttpHandler handler = new HttpHandler();
        String respuesta = handler.agregarHorario(nombreHorario,hrEntrada,hrSalida,comidaI,comidaF,tolerancia);

        if (respuesta.equals("true")){
            cuadrosDialogo.cuadroDialogo(
                    getString(R.string.st_aceptar),
                    getString(R.string.st_adicionExitosa),
                    getString(R.string.st_hey),
                    this);
        }else {
            Log.e("Error: ",respuesta);
        }
    }

    private void editarDatosHorario(){
        String  nombreHorario = et_hr_nombreHorario.getText().toString();
        String hrEntrada = et_hr_entrada.getText().toString();
        String hrSalida = et_hr_salida.getText().toString();
        String comidaI = et_hr_comidaInicio.getText().toString();
        String comidaF = et_hr_comidaFin.getText().toString();
        String tolerancia = et_hr_tolerancia.getText().toString();

        HttpHandler handler = new HttpHandler();
        String respuesta = handler.agregarHorario(nombreHorario,hrEntrada,hrSalida,comidaI,comidaF,tolerancia);

        if (respuesta.equals("true")){
            cuadrosDialogo.cuadroDialogo(
                    getString(R.string.st_aceptar),
                    getString(R.string.st_adicionExitosa),
                    getString(R.string.st_hey),
                    this);
        }else {
            Log.e("Error: ",respuesta);
        }
    }

    //metodo privado para poblar los campos en caso de que se usen para editar
    private void prepararCampos(){
        Context context = this;
        SharedPreferences sp_datosHorarios =
                context.getSharedPreferences("sp_horariosTable",
                        Context.MODE_PRIVATE);
        i = getIntent().getExtras().getString("i","0");
        String hr_nombre = sp_datosHorarios.getString("hr_nombre"+i,"0");
        String hr_entrada = sp_datosHorarios.getString("hr_entrada"+i,"0");
        String hr_comida_i = sp_datosHorarios.getString("hr_comida_i"+i,"0");
        String hr_comida_f = sp_datosHorarios.getString("hr_comida_f"+i,"0");
        String hr_salida = sp_datosHorarios.getString("hr_salida"+i,"0");
        String tolerancia = sp_datosHorarios.getString("tolerancia"+i,"0");

        et_hr_nombreHorario.setText(hr_nombre);
        et_hr_entrada.setText(hr_entrada);
        et_hr_salida.setText(hr_salida);
        et_hr_comidaInicio.setText(hr_comida_i);
        et_hr_comidaFin.setText(hr_comida_f);
        et_hr_tolerancia.setText(tolerancia);
    }

    //metodo privado que mostrara un toast, de mensaje contendra variables
    private void mostrarToast(String mensaje){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, mensaje, duration);
        toast.show();
    }
}
