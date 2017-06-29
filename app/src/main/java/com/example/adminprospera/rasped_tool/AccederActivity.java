package com.example.adminprospera.rasped_tool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
    ListView lv_ad_personal;
    String st_telefono,st_contraseña;
    String[] arrayIdSedes = null;
    JSONArray jsonArray = null;
    URL url = null;
    private static final String PREFS_NAME = "datosUsuario";


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
        lv_ad_personal = (ListView) findViewById(R.id.lv_ad_personal);

        //asignarle la tarea acceder al hacer click en bt_ac_acceder
        bt_ac_acceder.setOnClickListener(tareaAcceder);

    }

    //crear tareaAcceder que se activirara al hacer click en un componente establecido
    View.OnClickListener tareaAcceder = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            datosACadena();
            evaluaCredenciales();
        }
    };

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
            llenarArreglo();

            //abrir un try para intentar evaluar el tipo de usuario y contraseña
            try {

                //extraer el tipo de usuario y almacenarlo en una variable
                String tipo = ""+arrayIdSedes[8];

                //segun el tipo de usuario, sera el tipo de layout a abrir
                if (Objects.equals(tipo, "root")){
                    mostrarToast("root");

                    //evaluacion para el usuario administrador
                }else if (Objects.equals(tipo, "admin")) {
                    //una vez extraido el tipo de usuario, se evalua la contraseña
                    if(evaluaContrasena()){
                        abrirAdministradorActivity();
                        poblarCacheDatosPersonal();
                    }

                    //evaluacion para el usuario registrador
                }else if (Objects.equals(tipo, "regis")) {
                    //una vez extraido el tipo de usuario, se evalua la contraseña
                    if (evaluaContrasena()){
                        abrirRegistradorActivity();
                        poblarCacheDatosPersonal();
                    }

                    //evaluacion para el usuario en general
                }else if (Objects.equals(tipo, "user")) {
                    //una vez extraido el tipo de usuario, se evalua la contraseña
                    if (evaluaContrasena()){
                        abrirUsusariosActivity();
                        poblarCacheDatosPersonal();
                    }

                    //si no se encontro usuario, entonces el usuario con los datos dijitados no existe
                }else {
                    mostrarToast(getString(R.string.ms_usuarioNoEncontrado));
                }

                //mostrar error en caso de fallo para la evaluacion de tipo de usuario y contraseña
            }catch (Exception e){
                mostrarToast("error: "+e.getMessage());
            }

            //si los campos de texto estan vacios, mostrar mensaje
        }else{
            mostrarToast(getString(R.string.ms_camposVacios));
        }
    }

    //
    private void poblarCacheDatosPersonal(){
        SharedPreferences sp_datosUsuario = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp_datosUsuario.edit();
        editor.putString("id_personal",arrayIdSedes[0]);
        editor.putString("cupo",arrayIdSedes[1]);
        editor.putString("nombre_personal",arrayIdSedes[2]);
        editor.putString("apellidos",arrayIdSedes[3]);
        editor.putString("telefono",arrayIdSedes[4]);
        editor.putString("contrasena",arrayIdSedes[5]);
        editor.putString("horario",arrayIdSedes[6]);
        editor.putString("puesto",arrayIdSedes[7]);
        editor.putString("usuario",arrayIdSedes[8]);
        editor.apply();
    }

    //metodo que devuelve un true o false segun la contresña coincida con una evaluacion
    private boolean evaluaContrasena(){

        //se extrae el metodo cifrar para cifrar la contraseña
        cifrar objCifrar = new cifrar();
        st_contraseña = objCifrar.md5(st_contraseña);

        //mostrar error en caso de que la contraseña haya sido incorrecta
        if(!arrayIdSedes[5].equals(st_contraseña)){
            mostrarToast(getString(R.string.ms_credencialesIncorrectas));
            return false;
        }

        //devolucion de un true (evaluacion de contraseña correcta)
        return true;
    }

    //metodo privado para llenar arreglo arrayIdSedes
    private void llenarArreglo(){
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
                mostrarToast(json);
                //creacion de un runOnUiThread para usar la clase llenaArreglo()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llenaArreglo(json);
                    }
                });
            }
        };
        //ejecucion del hilo
        tr.run();
    }

    //metodo privado para llenar la url que devuelve al personal
    private void urlPersonal(){
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
    private void llenaArreglo(String json){
        try{
            //preparar el arreglo JSON
            jsonArray = new JSONArray(json);
        }catch (JSONException e){
            mostrarToast("error: "+e.getMessage());
        }
        //preparar el arreglo Android
        arrayIdSedes = new String[9];
        try{

            //preparar un objeto JSON para la extraccion de datos
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            //almacenar los datos en el arreglo columna por columna
            arrayIdSedes[0] = jsonObject.getString("id_personal");
            arrayIdSedes[1] = jsonObject.getString("cupo");
            arrayIdSedes[2] = jsonObject.getString("nombre_personal");
            arrayIdSedes[3] = jsonObject.getString("apellidos");
            arrayIdSedes[4] = jsonObject.getString("telefono");
            arrayIdSedes[5] = jsonObject.getString("contrasena");
            arrayIdSedes[6] = jsonObject.getString("horario");
            arrayIdSedes[7] = jsonObject.getString("puesto");
            arrayIdSedes[8] = jsonObject.getString("usuario");

        }catch (JSONException e){
            mostrarToast("error: "+e.getMessage());
        }
    }

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
        Intent intent = new Intent(this, UsuariosActivity.class);
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

        public String md5(String texto) {
            return getCifrado(texto, "MD5");
        }

        public String sha1(String texto) {
            return getCifrado(texto, "SHA1");
        }
    }
}
