package com.example.adminprospera.rasped_tool;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class PersonalActivity extends AppCompatActivity {

    //Declaracion de variables utilizables en toda la clase PersonalActivity
    EditText et_pe_nombre,et_pe_apPaterno,et_pe_apMaterno,et_pe_lada,et_pe_telefono,et_pe_sede,et_pe_cupo;
    Spinner sp_pe_horario,sp_pe_area,sp_pe_tipo;
    CuadrosDialogo cuadrosDialogo = new CuadrosDialogo();
    LetrasNumerosAleatorios aleatorios = new LetrasNumerosAleatorios();
    private static final long tiempoEspera = 4500;
    int dimensionCodigo = 7;
    Boolean editar = false;

    MetodosJson metodosJson = new MetodosJson();
    JSONArray jsonArrayHorarios,jsonArrayPuestos;
    String linkTodosPuestos = "https://rasped.herokuapp.com/content/puestos.php";
    String linkTodosHorarios = "https://rasped.herokuapp.com/content/horariosTable.php";
    String linkTodosTipos = "https://rasped.herokuapp.com/content/horariosTable.php";
    URL urlHorarios,urlPuestos,urlTipos = null;
    String[] arraryHorarios,arrayPuestos,arrayTipos;
    int registrosHorarios,registrosPuestos,registrosTipos = 0;

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
        sp_pe_area = (Spinner) findViewById(R.id.sp_pe_area);
        sp_pe_tipo = (Spinner) findViewById(R.id.sp_pe_tipo);

        extraerHorariosPuestosTipos();


        //obtener la tarea a realizar si es editar(true) o insertar (false)
        editar = getIntent().getExtras().getBoolean("editar",false);

        //poblar los spinners
        poblarSpinners();

        //si la tarea es editar, se llenaran los datos
        if (editar){
            llenarCampos();
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

    private void extraerHorariosPuestosTipos(){
        //creacion de un hilo
        Thread tr = new Thread(){
            @Override
            public void run(){

                //casteo de una clase exterior
                MetodosJson metodosJson = new MetodosJson();

                //llenar la url
                urlHorarios = metodosJson.crearURL(linkTodosHorarios);
                urlPuestos = metodosJson.crearURL(linkTodosPuestos);

                //obtencion del json atraves de la clase exterior y la url
                final String jsonHorarios = metodosJson.obtenerJSON(urlHorarios);
                final String jsonPuestos = metodosJson.obtenerJSON(urlPuestos);
                //creacion de un runOnUiThread para usar la clase llenaArreglo()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llenaArregloHorariosPuestos(jsonHorarios,jsonPuestos);
                    }
                });
            }
        };
        //ejecucion del hilo
        tr.run();
    }

    private void llenaArregloHorariosPuestos(String jsonHorarios,String jsonPuestos){
        try{
            //preparar el objeto JSON que contendra los resultados de una consulta de base de datos
            jsonArrayHorarios = new JSONArray(jsonHorarios);
            jsonArrayPuestos = new JSONArray(jsonPuestos);
        }catch (JSONException e){
            mostrarToast("error: "+e.getMessage());
        }
        registrosHorarios = jsonArrayHorarios.length();
        registrosPuestos = jsonArrayPuestos.length();
        //preparar el arreglo Android
        arraryHorarios = new String[registrosHorarios];
        arrayPuestos = new String[registrosPuestos];
        try{
            for (int i=0;i<registrosHorarios;i++){
                //preparar un objeto JSON para la extraccion de datos
                JSONObject jsonObject = jsonArrayHorarios.getJSONObject(i);
                //almacenar los datos en el arreglo columna por columna
                arraryHorarios[i] = jsonObject.getString("hr_nombre");
            }
            for (int i=0;i<registrosPuestos;i++){
                //preparar un objeto JSON para la extraccion de datos
                JSONObject jsonObject = jsonArrayPuestos.getJSONObject(i);
                //almacenar los datos en el arreglo columna por columna
                arrayPuestos[i] = jsonObject.getString("nombre_puesto");
            }
        }catch (JSONException e){
            mostrarToast("error: "+e.getMessage());
        }
    }

    //metodo privado que pobla los spinner
    private void poblarSpinners(){
        //Creamos la lista
        LinkedList puestos = new LinkedList();
        LinkedList horarios = new LinkedList();
        LinkedList tipos = new LinkedList();

        for(int i=0;i<registrosHorarios;i++){
            //La poblamos con los ejemplos
            horarios.add(new ObjetosPuestos(i,arraryHorarios[i]));
        }

        for(int i=0;i<registrosPuestos;i++){
            puestos.add(new ObjetosPuestos(i,arrayPuestos[i])  );
        }
        puestos.add(new ObjetosPuestos(100,getString(R.string.st_agregar)));

        tipos.add(new ObjetosPuestos(2,getString(R.string.st_administrador)));
        tipos.add(new ObjetosPuestos(3,getString(R.string.st_registrador)));
        tipos.add(new ObjetosPuestos(4,getString(R.string.st_personal)));

        //Creamos el adaptador
        ArrayAdapter adapterPuestos = new ArrayAdapter(this, android.R.layout.simple_spinner_item, puestos);
        ArrayAdapter adapterHorarios = new ArrayAdapter(this, android.R.layout.simple_spinner_item, horarios);
        ArrayAdapter adapterTipos = new ArrayAdapter(this, android.R.layout.simple_spinner_item, tipos);

        //Añadimos el layout para el menú y se lo damos al spinner
        adapterPuestos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterHorarios.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_pe_area.setAdapter(adapterPuestos);
        sp_pe_horario.setAdapter(adapterHorarios);
        sp_pe_tipo.setAdapter(adapterTipos);

        sp_pe_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String elemento = parent.getItemAtPosition(position).toString();
                if (elemento.equals(getString(R.string.st_agregar))){
                    agregarPuesto();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void agregarPuesto(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        builder.setTitle(getString(R.string.st_agregar))
                .setView(input)
                .setPositiveButton(getString(R.string.st_agregar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                agregarPuesto(input.getText().toString());
                                dialog.cancel();
                            }
                        })
                .setNegativeButton(getString(R.string.st_cancelar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void agregarPuesto(String puesto){
        if (puesto.length()>0){
            HttpHandler handler = new HttpHandler();
            String resultado = handler.agregarPuesto(puesto);
            if (resultado.equals("true")){
                mostrarToast(getString(R.string.st_registroExitoso));
            }
            sp_pe_horario.setAdapter(null);
            sp_pe_area.setAdapter(null);
            extraerHorariosPuestosTipos();
            poblarSpinners();

        }else {
            cuadrosDialogo.cuadroDialogo(
                    getString(R.string.st_aceptar),
                    getString(R.string.ms_camposVacios),
                    getString(R.string.st_hey),
                    this
            );
        }
    }

    //metodo privato devuelve mensaje de error en caso de campos vacios
    private void evaluarCampos() {

        if ((et_pe_nombre.getText().toString().length() > 0) &&
                (et_pe_apPaterno.getText().toString().length() > 0) &&
                (et_pe_apMaterno.getText().toString().length() > 0) &&
                (et_pe_lada.getText().toString().length() > 0) &&
                (et_pe_telefono.getText().toString().length() > 0) &&
                (et_pe_sede.getText().toString().length() > 0) &&
                (et_pe_cupo.getText().toString().length() > 0)) {

            if (editar){
                editarDatosPersonal();
            }else {
                insertarDatosPersonal();
            }
        }else {

            cuadrosDialogo.cuadroDialogo(
                    getString(R.string.st_aceptar),
                    getString(R.string.ms_camposVacios),
                    getString(R.string.st_hey),
                    this);
        }
    }

    private void editarDatosPersonal(){
        String  nombre_personal = et_pe_nombre.getText().toString();
        String apellido_m = et_pe_apPaterno.getText().toString();
        String apellido_p = et_pe_apMaterno.getText().toString();
        String lada = et_pe_lada.getText().toString();
        String telefono = et_pe_telefono.getText().toString();
        String sede = et_pe_sede.getText().toString();
        String cupo = et_pe_cupo.getText().toString();
        String contrasena = aleatorios.codigoAleatorio(dimensionCodigo);

        HttpHandler handler = new HttpHandler();
        String respuesta = handler.postEditarPersonal(nombre_personal, apellido_m, apellido_p,
                contrasena, lada, telefono,sede, cupo);

        if (respuesta.equals("true")){
            edicionCorrecta();
            enviarContrasena(lada+telefono,getString(R.string.ms_tuContrasena)+" "+contrasena);
        }else {
            errorEdicion("Error: "+respuesta);
        }
    }

    private void insertarDatosPersonal(){
        String  nombre_personal = et_pe_nombre.getText().toString();
        String apellido_m = et_pe_apPaterno.getText().toString();
        String apellido_p = et_pe_apMaterno.getText().toString();
        String lada = et_pe_lada.getText().toString();
        String telefono = et_pe_telefono.getText().toString();
        String sede = et_pe_sede.getText().toString();
        String cupo = et_pe_cupo.getText().toString();
        String contrasena = aleatorios.codigoAleatorio(dimensionCodigo);
        String horario = sp_pe_horario.getSelectedItem().toString();
        String puesto = sp_pe_area.getSelectedItem().toString();
        String typo = sp_pe_tipo.getSelectedItem().toString();

        if (typo.equals(getString(R.string.st_administrador))){
            typo = "2";
        }else if (typo.equals(getString(R.string.st_registrador))){
            typo = "3";
        }else if (typo.equals(getString(R.string.st_personal))){
            typo = "4";
        }

        HttpHandler handler = new HttpHandler();
        String respuesta = handler.postPersonal(nombre_personal, apellido_m, apellido_p,
                contrasena, lada, telefono,sede, cupo,horario,puesto,typo);

        if (respuesta.equals("true")){
            inserccionCorrecta();
            enviarContrasena(lada+telefono,getString(R.string.ms_tuContrasena)+" "+contrasena);
        }else {
            errorInserccion("Error: "+respuesta);
        }
    }

    private void llenarCampos(){
        Context context = this.getApplicationContext();
        SharedPreferences sp_datosPersonal = context.getSharedPreferences(getString(R.string.sp_datosPersonal_key),Context.MODE_PRIVATE);


        et_pe_nombre.setText(sp_datosPersonal.getString(getString(R.string.sp_nombrePersonal_key),"null"));
        et_pe_apPaterno.setText(sp_datosPersonal.getString(getString(R.string.sp_apellidoPPersonal_key),"null"));
        et_pe_apMaterno.setText(sp_datosPersonal.getString(getString(R.string.sp_apellidoMPersonal_key),"null"));
        et_pe_lada.setText(sp_datosPersonal.getString(getString(R.string.sp_ladaPersonal_key),"null"));
        et_pe_telefono.setText(sp_datosPersonal.getString(getString(R.string.sp_telefonoPersonal_key),"null"));
        et_pe_sede.setText(sp_datosPersonal.getString(getString(R.string.sp_sedePersonal_key),"null"));
        et_pe_cupo.setText(sp_datosPersonal.getString(getString(R.string.sp_cupoPersonal_key),"null"));
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

    private void edicionCorrecta(){
        cuadrosDialogo.cuadroDialogo(
                getString(R.string.st_aceptar),
                getString(R.string.st_registroExitoso),
                getString(R.string.st_hey),
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

    private void errorEdicion(String respuesta){
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
