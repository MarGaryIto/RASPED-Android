package com.example.adminprospera.rasped_tool;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class PersonalActivity extends AppCompatActivity {

    //Declaracion de variables utilizables en toda la clase PersonalActivity
    EditText et_pe_nombre,et_pe_apPaterno,et_pe_apMaterno,et_pe_lada,et_pe_telefono,et_pe_sede,et_pe_cupo;
    Spinner sp_pe_horario,sp_pe_area;
    CuadrosDialogo cuadrosDialogo = new CuadrosDialogo();
    LetrasNumerosAleatorios aleatorios = new LetrasNumerosAleatorios();
    private static final long tiempoEspera = 4500;
    int dimensionCodigo = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //inflar o reyenar el layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        //instanciar los elementos del Activity para ser manipulados
        et_pe_nombre = (EditText) findViewById(R.id.et_pe_nombre);
        et_pe_apPaterno = (EditText) findViewById(R.id.et_pe_apPaterno);
        et_pe_apMaterno = (EditText) findViewById(R.id.et_pe_apMaterno);
        et_pe_lada = (EditText) findViewById(R.id.et_pe_lada);
        et_pe_telefono = (EditText) findViewById(R.id.et_pe_telefono);
        et_pe_sede = (EditText) findViewById(R.id.et_pe_sede);
        et_pe_cupo = (EditText) findViewById(R.id.et_pe_cupo);
        sp_pe_horario = (Spinner) findViewById(R.id.sp_pe_horario);
        sp_pe_horario = (Spinner) findViewById(R.id.sp_pe_horario);

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

    private void evaluarCampos() {

        if ((et_pe_nombre.getText().toString().length() > 0) &&
                (et_pe_apPaterno.getText().toString().length() > 0) &&
                (et_pe_apMaterno.getText().toString().length() > 0) &&
                (et_pe_lada.getText().toString().length() > 0) &&
                (et_pe_telefono.getText().toString().length() > 0) &&
                (et_pe_sede.getText().toString().length() > 0) &&
                (et_pe_cupo.getText().toString().length() > 0)) {

            String  nombre_personal = et_pe_nombre.getText().toString();
            String apellido_m = et_pe_apPaterno.getText().toString();
            String apellido_p = et_pe_apMaterno.getText().toString();
            String lada = et_pe_lada.getText().toString();
            String telefono = et_pe_telefono.getText().toString();
            String sede = et_pe_sede.getText().toString();
            String cupo = et_pe_cupo.getText().toString();
            String contrasena = aleatorios.codigoAleatorio(dimensionCodigo);

            HttpHandler handler = new HttpHandler();
            String respuesta = handler.postPersonal(nombre_personal, apellido_m, apellido_p,
                    contrasena, lada, telefono,sede, cupo);

            if (respuesta.equals("true")){
                inserccionCorrecta();
                enviarContrasena(lada+telefono,getString(R.string.ms_tuContrasena)+" "+contrasena);
            }else {
                errorInserccion("Error: "+respuesta);
            }
        }else {

            cuadrosDialogo.cuadroDialogo(
                    getString(R.string.st_aceptar),
                    getString(R.string.ms_camposVacios),
                    getString(R.string.st_hey),
                    this);
        }
    }

    private void enviarContrasena(String telefono,String contrasena){
        HttpHandler handler = new HttpHandler();
        handler.postContrasena(telefono,contrasena);
    }

    private void inserccionCorrecta(){
        cuadrosDialogo.cuadroDialogo(
                getString(R.string.st_aceptar),
                getString(R.string.ms_contrasenaEnviada),
                getString(R.string.st_adicionExitosa),
                this);

        cerrarActivity();
    }

    private void errorInserccion(String respuesta){
        cuadrosDialogo.cuadroDialogo(
                getString(R.string.st_aceptar),
                getString(R.string.ms_errorDatos)+ "\nError: "+respuesta,
                getString(R.string.st_hey),
                this);
    }
    private void cerrarActivity(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //cerrar splashActivity (actual activity)
                finish();
            }
        };

        //ejecutar tiempo de espera para simular la carga de la aplicacion
        Timer timer = new Timer();
        timer.schedule(task, tiempoEspera);
    }

    //metodo privado que mostrara un toast, de mensaje contendra variables
    private void mostrarToast(String mensaje) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, mensaje, duration);
        toast.show();

    }
}
