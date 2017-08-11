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

public class ContrasenaActivity extends AppCompatActivity {

    EditText et_antContrasena,et_nvaContrasena,et_repContrasena;
    CuadrosDialogo cuadrosDialogo;
    HttpHandler httpHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrasena);

        //instanciar los EditText para tratarlos
        et_antContrasena = (EditText) findViewById(R.id.et_antContrasena);
        et_nvaContrasena = (EditText) findViewById(R.id.et_nvaContrasena);
        et_repContrasena = (EditText) findViewById(R.id.et_repContrasena);
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
                //evaluarCampos();
                return true;
            case R.id.it_cancelar:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void evaluarCampos() {

        if ((et_antContrasena.getText().toString().length() > 0) &&
                (et_nvaContrasena.getText().toString().length() > 0) &&
                (et_repContrasena.getText().toString().length() > 0)) {

            String antContrasena = et_antContrasena.getText().toString();
            String nvaContrasena = et_nvaContrasena.getText().toString();
            String repContrasena = et_repContrasena.getText().toString();

            //httpHandler.editarContrasena();

            Context context = getApplicationContext();
            SharedPreferences sp_datosPersonal = context.getSharedPreferences(getString(R.string.sp_datosPersonal_key),Context.MODE_PRIVATE);
            String sp_antContrasena = sp_datosPersonal.getString("sp_contrasenaPersonal","null");

            Log.e("ant cont",sp_antContrasena);
            mostrarToast(sp_antContrasena);
            /*HttpHandler handler = new HttpHandler();
            String respuesta = handler.postPersonal(nombre_personal, apellido_m, apellido_p,
                    contrasena, lada, telefono,sede, cupo);

            if (respuesta.equals("true")){
                inserccionCorrecta();
                enviarContrasena(lada+telefono,getString(R.string.ms_tuContrasena)+" "+contrasena);
            }else {
                errorInserccion("Error: "+respuesta);
            }*/
        }else {

            cuadrosDialogo.cuadroDialogo(
                    getString(R.string.st_aceptar),
                    getString(R.string.ms_camposVacios),
                    getString(R.string.st_hey),
                    this);
        }
    }
    //metodo privado que mostrara un toast, de mensaje contendra variables
    private void mostrarToast(String mensaje){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, mensaje, duration);
        toast.show();
    }
}
