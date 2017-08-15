package com.example.adminprospera.rasped_tool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Objects;

public class AccederActivity extends AppCompatActivity {

    //Generar variables globales para esta clase
    EditText et_ac_telefono, et_ac_contrasena;
    Button bt_ac_acceder;
    TextView tv_ac_restablecerContrasena;
    String st_telefono,st_contraseña;
    String[] arraryPersonal,arrayCredenciales,arrayPuestos,arrayHorarios;
    JSONArray jsonArray,jsonArrayHorarios,jsonArrayPuestos = null;
    URL url = null;
    String linkPuestos = "https://rasped.herokuapp.com/content/puestos.php";
    String linkHorarios = "https://rasped.herokuapp.com/content/horarios.php";
    CuadrosDialogo cuadrosDialogo;
    LetrasNumerosAleatorios aleatorios = new LetrasNumerosAleatorios();
    protected int splashTime = 3000;
    String[] name = {"A","N","D","R","O","I","D"};
    int timer =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceder);

        //establecer y mantener conexion externa
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        //Traer los componentes de accederActivity a esta clase para usarlos posteriormente
        et_ac_telefono = (EditText) findViewById(R.id.et_ac_telefono);
        et_ac_contrasena = (EditText) findViewById(R.id.et_ac_contrasena);
        bt_ac_acceder = (Button) findViewById(R.id.bt_ac_acceder);
        tv_ac_restablecerContrasena = (TextView) findViewById(R.id.tv_ac_restaurarContrasena);

        //asignarle la tarea acceder al hacer click en bt_ac_acceder
        bt_ac_acceder.setOnClickListener(tareaAcceder);

        tv_ac_restablecerContrasena.setOnClickListener(tareaAcceder);

        cuadrosDialogo = new CuadrosDialogo();


    }

    //crear tareaAcceder que se activirara al hacer click en un componente establecido
    View.OnClickListener tareaAcceder = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            AccederActivity.super.onRestart();
            if (id==bt_ac_acceder.getId()){
                datosACadena();
                evaluaCredenciales();

            }else if(id==tv_ac_restablecerContrasena.getId()){
                obtenerTelefono();
            }

        }
    };

    private void obtenerTelefono(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        builder.setTitle(getString(R.string.ms_restablecerContrasena))
                .setView(input)
                .setPositiveButton(getString(R.string.st_aceptar),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                recuperarContrasena(input.getText().toString());
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

    //
    private void recuperarContrasena(String telefono){
        String contrasena = aleatorios.codigoAleatorio(6);
        HttpHandler handler = new HttpHandler();
        String resultActCon = handler.editarContrasena(telefono,contrasena);
        Log.e("recuperarContrasena",resultActCon);
        cuadrosDialogo.cuadroDialogo(
                getString(R.string.st_aceptar),
                getString(R.string.ms_contrasenaEnviada),
                getString(R.string.st_hey),
                AccederActivity.this
        );
        handler.postContrasena(telefono,contrasena);
    }


    //metodo privado que almacena el contenido de los editText a cadenas de texto tipo String
    private void datosACadena(){
        st_telefono = et_ac_telefono.getText().toString();
        st_contraseña = et_ac_contrasena.getText().toString();
    }

    //metodo privado que evalua los
    private void evaluaCredenciales(){

        //evaluar que los campos de accesso esten llenos
        if((st_telefono.length()>0) && (st_contraseña.length()>0)){

            //si los campos tienen datos, llenar el arreglo que contiene los datos de usuario
            llenarArregloCredenciales();

            //abrir un try para intentar evaluar el tipo de usuario y contraseña
            try {

                //extraer el tipo de usuario y almacenarlo en una variable
                String tipo = ""+arrayCredenciales[11];

                //segun el tipo de usuario, sera el tipo de layout a abrir
                if (Objects.equals(tipo, "root")){
                    mostrarToast("root");

                    //evaluacion para el usuario administrador
                }else if (Objects.equals(tipo, "admin")) {
                    //una vez extraido el tipo de usuario, se evalua la contraseña
                    if(evaluaContrasena()){
                        abrirAdministradorActivity();
                        almacenarDatosCredencial();
                        llenarArregloPuestos();
                        llenarArregloHorarios();
                        cargarPreferDeConfig();
                    }

                    //evaluacion para el usuario registrador
                }else if (Objects.equals(tipo, "regis")) {
                    //una vez extraido el tipo de usuario, se evalua la contraseña
                    if (evaluaContrasena()){
                        almacenarDatosCredencial();
                        abrirRegistradorActivity();
                        cargarPreferDeConfig();
                    }

                    //evaluacion para el usuario en general
                }else if (Objects.equals(tipo, "user")) {
                    //una vez extraido el tipo de usuario, se evalua la contraseña
                    if (evaluaContrasena()){
                        almacenarDatosCredencial();
                        abrirUsusariosActivity();
                        cargarPreferDeConfig();
                    }

                    //si no se encontro usuario, entonces el usuario con los datos dijitados no existe
                }else {
                    cuadrosDialogo.cuadroDialogo(
                            getString(R.string.st_aceptar),
                            getString(R.string.ms_usuarioNoEncontrado),
                            getString(R.string.st_hey),
                            this
                    );
                }

                //mostrar error en caso de fallo para la evaluacion de tipo de usuario y contraseña
            }catch (Exception e){
                mostrarToast("error: "+e.getMessage());
            }

            //si los campos de texto estan vacios, mostrar mensaje
        }else{
            cuadrosDialogo.cuadroDialogo(
                    (getString(R.string.st_aceptar)),
                    (getString(R.string.ms_camposVacios)),
                    (getString(R.string.st_hey)),
                    AccederActivity.this);
        }
    }

    private void almacenarDatosCredencial(){
        Context context = this.getApplicationContext();
        SharedPreferences sp_datosPersonal = context.getSharedPreferences(getString(R.string.sp_datosPersonal_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp_datosPersonal.edit();

        editor.putString(getString(R.string.sp_idPersonal_key),arrayCredenciales[0]);
        editor.putString(getString(R.string.sp_sedePersonal_key),arrayCredenciales[1]);
        editor.putString(getString(R.string.sp_cupoPersonal_key),arrayCredenciales[2]);
        editor.putString(getString(R.string.sp_nombrePersonal_key),arrayCredenciales[3]);
        editor.putString(getString(R.string.sp_apellidoPPersonal_key),arrayCredenciales[4]);
        editor.putString(getString(R.string.sp_apellidoMPersonal_key),arrayCredenciales[5]);
        editor.putString(getString(R.string.sp_ladaPersonal_key),arrayCredenciales[6]);
        editor.putString(getString(R.string.sp_telefonoPersonal_key),arrayCredenciales[7]);
        editor.putString(getString(R.string.sp_horarioPersonal_key),arrayCredenciales[9]);
        editor.putString(getString(R.string.sp_puestoPersonal_key),arrayCredenciales[10]);
        editor.putString(getString(R.string.sp_usuarioPersonal_key),arrayCredenciales[11]);
        editor.putString("sp_contrasenaPersonal",et_ac_contrasena.getText().toString());
        editor.apply();
    }

    //metodo que devuelve un true o false segun la contresña coincida con una evaluacion
    private boolean evaluaContrasena(){

        //se extrae el metodo cifrar para cifrar la contraseña
        cifrar objCifrar = new cifrar();
        st_contraseña = objCifrar.md5(st_contraseña);

        //comparar la contraseña almacenada en el arreglo arrayCredenciales en la posicion 8
        // con la contraseña previamente encriptada ingresada
        if(arrayCredenciales[8].equals(st_contraseña)){

            //en caso de ser iguales, llenar el arregloPersonal con los datos del personal y
            // almacenarlos en memoria temporal del celular
            llenarArregloPersonal();

            //devolucion de un true (evaluacion de contraseña correcta)
            return true;
        }else{

            //si la contraseña es incorrecta mostrar un cuadro de alerta con el siguientecontenido:
            // un boton de aceptar, una describpcion, un titulo y en que activity se mostrara
            cuadrosDialogo.cuadroDialogo(
                    getString(R.string.st_aceptar),
                    getString(R.string.ms_credencialesIncorrectas),
                    getString(R.string.st_hey),
                    AccederActivity.this
            );
            return false;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //metodo privado para llenar arreglo arrayIdSedes
    private void llenarArregloPersonal(){
        //creacion de un hilo
        Thread tr = new Thread(){
            @Override
            public void run(){

                //casteo de una clase exterior
                MetodosJson metodosJson = new MetodosJson();

                //llenar la url
                urlPersonal();

                //obtencion del json atraves de la clase exterior y la url
                final String json = metodosJson.obtenerJSON(url);
                //creacion de un runOnUiThread para usar la clase llenaArreglo()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llenaArregloPersonal(json);
                    }
                });
            }
        };
        //ejecucion del hilo
        tr.run();
        almacenarDatosPersonal();
    }

    //metodo privado para llenar la url que devuelve al personal
    private void urlPersonal(){
        try {
            //la generacion de url requiere estar oncentrada en un try-catch
            String link = "https://rasped.herokuapp.com/content/personal.php";
            url = new URL(link);

            //impresion de error en caso de generacion de url erronea
        }catch (Exception e){
            mostrarToast("error: "+e.getMessage());
        }
    }

    //metodo privado que pobla arrayPersonal de la base de datos remota desde un JavaScrip Object
    // Notation (JSON) un formato de texto ligero para intercambio de datos
    private void llenaArregloPersonal(String json){
        try{
            //preparar el objeto JSON que contendra los resultados de una consulta de base de datos
            jsonArray = new JSONArray(json);
        }catch (JSONException e){
            mostrarToast("error: "+e.getMessage());
        }
        int registros = jsonArray.length();
        //preparar el arreglo Android
        arraryPersonal = new String[registros];
        try{

            for (int i=0;i<registros;i++){
                //preparar un objeto JSON para la extraccion de datos
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String sede = jsonObject.getString("sede");
                String cupo = jsonObject.getString("cupo");
                String nombre_personal = jsonObject.getString("nombre_personal");
                String apellido_p = jsonObject.getString("apellido_p");

                //almacenar los datos en el arreglo columna por columna
                arraryPersonal[i] = sede+cupo+" | "+nombre_personal+" "+apellido_p;
            }
        }catch (JSONException e){
            mostrarToast("error: "+e.getMessage());
        }
    }

    private void almacenarDatosPersonal(){
        Context context = this.getApplicationContext();
        SharedPreferences sp_datosPersonal = context.getSharedPreferences(getString(R.string.sp_datosPersonal_key),Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sp_datosPersonal.edit();
        int personal = arraryPersonal.length;
        for (int i=0;i<personal;i++){
            editor.putString(getString(R.string.sp_nombrePersonal_key)+i,arraryPersonal[i]);
        }
        editor.putInt(getString(R.string.sp_dimensionArrayPersonal_key),personal);
        editor.apply();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void cargarPreferDeConfig(){
        // Cargar valores por defecto
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
    }
    //metodo privado para llenar arreglo arrayIdSedes
    private void llenarArregloCredenciales(){
        //creacion de un hilo
        Thread tr = new Thread(){
            @Override
            public void run(){

                //casteo de una clase exterior
                MetodosJson metodosJson = new MetodosJson();

                //llenar la url
                urlCredenciales();

                //obtencion del json atraves de la clase exterior y la url
                final String json = metodosJson.obtenerJSON(url);
                //creacion de un runOnUiThread para usar la clase llenaArreglo()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llenaArregloCredenciales(json);
                    }
                });
            }
        };
        //ejecucion del hilo
        tr.run();
    }

    //metodo privado para llenar la url que devuelve al personal
    private void urlCredenciales(){
        try {
            //la generacion de url requiere estar oncentrada en un try-catch
            String link = "https://rasped.herokuapp.com/content/personal_tel.php?telefono="+st_telefono;
            url = new URL(link);

            //impresion de error en caso de generacion de url erronea
        }catch (Exception e){
            mostrarToast("error: "+e.getMessage());
        }
    }

    //metodo privado que llena el arreglo arrayIdSedes que llena el lv_ad_personal
    private void llenaArregloCredenciales(String json){
        try{
            //preparar el arreglo JSON
            jsonArray = new JSONArray(json);
        }catch (JSONException e){
            mostrarToast("error: "+e.getMessage());
        }
        //preparar el arreglo Android
        arrayCredenciales = new String[12];
        try{

            //preparar un objeto JSON para la extraccion de datos
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            //almacenar los datos en el arreglo columna por columna
            arrayCredenciales[0] = jsonObject.getString("id_personal");
            arrayCredenciales[1] = jsonObject.getString("sede");
            arrayCredenciales[2] = jsonObject.getString("cupo");
            arrayCredenciales[3] = jsonObject.getString("nombre_personal");
            arrayCredenciales[4] = jsonObject.getString("apellido_p");
            arrayCredenciales[5] = jsonObject.getString("apellido_m");
            arrayCredenciales[6] = jsonObject.getString("lada");
            arrayCredenciales[7] = jsonObject.getString("telefono");
            arrayCredenciales[8] = jsonObject.getString("contrasena");
            arrayCredenciales[9] = jsonObject.getString("horario");
            arrayCredenciales[10] = jsonObject.getString("puesto");
            arrayCredenciales[11] = jsonObject.getString("usuario");

        }catch (JSONException e){
            //mostrarToast("error: "+e.getMessage());
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //metodo privado para llenar arreglo arrayIdSedes
    private void llenarArregloPuestos(){
        //creacion de un hilo
        Thread tr = new Thread(){
            @Override
            public void run(){

                //invocacion de una clase exterior
                MetodosJson metodosJson = new MetodosJson();

                //generar una url con el link de puestos
                URL urlPuestos = metodosJson.crearURL(linkPuestos);

                //obtencion del json atraves de la clase exterior y la url
                final String jsonPuestos = metodosJson.obtenerJSON(urlPuestos);


                //creacion de un runOnUiThread para usar la clase llenaArreglo()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //preparar el arreglo JSON
                            jsonArrayPuestos = new JSONArray(jsonPuestos);
                            int registros = jsonArrayPuestos.length();

                            //preparar el arreglo Android
                            arrayPuestos = new String[registros];

                            //ciclo para el concentrado de registros
                            for (int i = 0; i < registros; i++) {
                                //preparar un objeto JSON para la extraccion de datos
                                JSONObject jsonObject = jsonArrayPuestos.getJSONObject(i);

                                String nombre_puesto = jsonObject.getString("nombre_puesto");

                                //almacenar los datos en el arreglo columna por columna
                                arrayPuestos[i] = nombre_puesto;
                            }//for

                        }catch(JSONException e){
                                mostrarToast("error: " + e.getMessage());
                        }//try-catch
                    }//run
                });//runOnUiThread
            }//run
        };//Thread tr
        //ejecucion del hilo
        tr.run();
        almacenarDatosPuestos();
    }//llenarArregloPuestos



    private void almacenarDatosPuestos(){
        Context context = this.getApplicationContext();
        SharedPreferences sp_datosPuestos =
                context.getSharedPreferences(getString(R.string.sp_datosPuestos_key),
                        Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sp_datosPuestos.edit();
        int puestos = arrayPuestos.length;
        for (int i=0;i<puestos;i++){
            editor.putString(getString(R.string.sp_nombrePuesto_key)+i,arrayPuestos[i]);
        }
        editor.putInt(getString(R.string.sp_dimensionPuestos_key),puestos);
        editor.apply();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //metodo privado para llenar arreglo arrayIdSedes
    private void llenarArregloHorarios(){
        //creacion de un hilo
        Thread tr = new Thread(){
            @Override
            public void run(){

                //invocacion de una clase exterior
                MetodosJson metodosJson = new MetodosJson();

                //generar una url con el link de puestos
                URL urlHorarios = metodosJson.crearURL(linkHorarios);

                //obtencion del json atraves de la clase exterior y la url
                final String jsonHorarios = metodosJson.obtenerJSON(urlHorarios);


                //creacion de un runOnUiThread para usar la clase llenaArreglo()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //preparar el arreglo JSON
                            jsonArrayHorarios = new JSONArray(jsonHorarios);
                            int registros = jsonArrayHorarios.length();

                            //preparar el arreglo Android
                            arrayHorarios = new String[registros];

                            //ciclo para el concentrado de registros
                            for (int i = 0; i < registros; i++) {
                                //preparar un objeto JSON para la extraccion de datos
                                JSONObject jsonObject = jsonArrayHorarios.getJSONObject(i);

                                String hr_nombre = jsonObject.getString("hr_nombre");

                                //almacenar los datos en el arreglo columna por columna
                                arrayHorarios[i] = hr_nombre;
                            }//for

                        }catch(JSONException e){
                            mostrarToast("error: " + e.getMessage());
                        }//try-catch
                    }//run
                });//runOnUiThread
            }//run
        };//Thread tr
        //ejecucion del hilo
        tr.run();
        almacenarDatosHorarios();
    }//llenarArregloHorarios



    private void almacenarDatosHorarios(){
        Context context = this.getApplicationContext();
        SharedPreferences sp_datosHorarios =
                context.getSharedPreferences(getString(R.string.sp_datosHorarios_key),
                        Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sp_datosHorarios.edit();
        int horarios = arrayHorarios.length;
        for (int i=0;i<horarios;i++){
            editor.putString(getString(R.string.sp_hrNombre_key)+i,arrayHorarios[i]);
        }
        editor.putInt(getString(R.string.sp_dimensionHorarios_key),horarios);
        editor.apply();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //metodo privado que mostrara un toast, de mensaje contendra variables
    private void mostrarToast(String mensaje){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, mensaje, duration);
        toast.show();
    }

    //metodo privada para abrir administradorActivity
    private void abrirAdministradorActivity(){
        Intent intent = new Intent(this, AdministradorActivity.class);
        startActivityForResult(intent,0);
        finish();
    }

    //metodo privada para abrir usuariosActivity
    private void abrirUsusariosActivity(){
        Context context = this.getApplicationContext();
        SharedPreferences sp_datosPersonal = context.getSharedPreferences(getString(R.string.sp_datosPersonal_key),Context.MODE_PRIVATE);
        String sede = sp_datosPersonal.getString(getString(R.string.sp_sedePersonal_key),"0");
        String cupo = sp_datosPersonal.getString(getString(R.string.sp_cupoPersonal_key),"0");
        String parametro_cupo = sede+cupo;
        Intent intent = new Intent(this, UsuariosActivity.class);
        intent.putExtra("cupo",parametro_cupo);
        startActivityForResult(intent,0);
        finish();
    }

    //metodo privado para abrir registradorActivity
    private void abrirRegistradorActivity(){
        Intent intent = new Intent(this, RegistradorActivity.class);
        startActivityForResult(intent,0);
        finish();
    }

    //clase privada para cifrar
    private class cifrar {
        private String getCifrado(String texto, String hashType) {
            try {
                java.security.MessageDigest md = java.security.MessageDigest.getInstance(hashType);
                byte[] array = md.digest(texto.getBytes());
                StringBuilder sb = new StringBuilder();

                for (byte anArray : array) {
                    sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
                }
                return sb.toString();
            } catch (java.security.NoSuchAlgorithmException e) {
                System.err.println("Error " + e.getMessage());
            }
            return "";
        }

        private String md5(String texto) {
            return getCifrado(texto, "MD5");
        }
    }
}
