package com.example.adminprospera.rasped_tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class ContrasenaActivity extends AppCompatActivity {

    EditText et_antContrasena,et_nvaContrasena;
    CuadrosDialogo cuadrosDialogo = new CuadrosDialogo();
    HttpHandler httpHandler = new HttpHandler();

    String antCont,nvaCont,telefono = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrasena);

        //establecer y mantener conexion externa
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        //instanciar los EditText para tratarlos
        et_antContrasena = (EditText) findViewById(R.id.et_antContrasena);
        et_nvaContrasena = (EditText) findViewById(R.id.et_nvaContrasena);

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

        antCont = et_antContrasena.getText().toString();
        nvaCont = et_nvaContrasena.getText().toString();

        Context context = this.getApplicationContext();
        SharedPreferences sp_datosPersonal = context.getSharedPreferences(getString(R.string.sp_datosPersonal_key),Context.MODE_PRIVATE);
        telefono = sp_datosPersonal.getString(getString(R.string.sp_ladaPersonal_key),"null");
        telefono += sp_datosPersonal.getString(getString(R.string.sp_telefonoPersonal_key),"null");
        String cont = sp_datosPersonal.getString("sp_contrasenaPersonal","null");

        if((antCont.length()>0) && (nvaCont.length()>0)){

            if (antCont.equals(cont)){

                httpHandler.editarContrasena(telefono,nvaCont);

                SharedPreferences.Editor editor = sp_datosPersonal.edit();
                editor.putString("sp_contrasenaPersonal",nvaCont);

                mostrarToast(getString(R.string.st_registroExitoso));

                finish();
            }else {
                mostrarToast(getString(R.string.st_errorContrasena));
            }


        }else {
            mostrarToast(getString(R.string.ms_camposVacios));
        }
    }

    //metodo privado que mostrara un toast, de mensaje contendra variables
    private void mostrarToast(String mensaje){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, mensaje, duration);
        toast.show();
    }

    //clase privada para cifrar
    private class Cifrar {
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
