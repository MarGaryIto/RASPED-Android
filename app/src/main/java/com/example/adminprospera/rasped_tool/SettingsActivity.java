package com.example.adminprospera.rasped_tool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity {

    //instanciar elementos de preferences globales
    PreferenceScreen ps_cerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        /*Instanciar elementos de preferences locales*/
        PreferenceScreen ps_editarDatos = (PreferenceScreen) findPreference("ps_editarDatos");
        PreferenceScreen ps_editarContraseña = (PreferenceScreen) findPreference("ps_editarContraseña");
        PreferenceScreen ps_informacion = (PreferenceScreen) findPreference("ps_informacion");
        ps_cerrarSesion = (PreferenceScreen) findPreference("ps_cerrarSesion");

        /*Establecer el intent que contiene la ruta del activity a abrir*/
        Intent intentEditaDatos = new Intent(this, PersonalActivity.class);
        Context context = this.getApplicationContext();
        SharedPreferences sp_datosPersonal = context.getSharedPreferences(getString(R.string.sp_datosPersonal_key),Context.MODE_PRIVATE);
        String sede = sp_datosPersonal.getString(getString(R.string.sp_sedePersonal_key),"null");
        String cupo = sp_datosPersonal.getString(getString(R.string.sp_cupoPersonal_key),"null");
        intentEditaDatos.putExtra("editar",true);
        intentEditaDatos.putExtra("cupo",sede+cupo);
        Intent intentEditaContrasena = new Intent(this, ContrasenaActivity.class);
        Intent intentInformacion = new Intent(this, InformacionActivity.class);

        /*ejecutar el inten*/
        ps_editarDatos.setIntent(intentEditaDatos);
        ps_editarContraseña.setIntent(intentEditaContrasena);
        ps_informacion.setIntent(intentInformacion);

        //asignar un escuchador al item preference cerrar sesion
        ps_cerrarSesion.setOnPreferenceClickListener(listener);
    }

    //escuchador que acciona las tareas de un item de tipo preference
    Preference.OnPreferenceClickListener listener = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            //si se acciono cerrar sesion, mostrar cuadro de confirmacion
            mostrarCuadroConfirmacion();
            return true;
        }
    };

    //clase privada que contiene el cuadro de confirmacion
    private void mostrarCuadroConfirmacion(){
        Toast.makeText(getApplicationContext(),"click",Toast.LENGTH_SHORT).show();
        //contruccion del alerdialog
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        //asignar sus propiedaes
        builder.setMessage(getString(R.string.ms_cerrarSesion))
                .setTitle(getString(R.string.st_cerrarSesion))
                .setPositiveButton(getString(R.string.st_aceptar), new DialogInterface.OnClickListener()  {
                    public void onClick(DialogInterface dialog, int id) {
                        //si fue accionado el boton positivo, cerrar sesion
                        cerrarSesion();
                        dialog.cancel();
                    }
                })
                .setNegativeButton(getString(R.string.st_cancelar), new DialogInterface.OnClickListener()  {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }

    //metodo privado que cierra la sesion y establece los datos
    private void cerrarSesion(){
    //obtencon de context
        Context context = this.getApplicationContext();

        //extraer archivos temporales
        SharedPreferences sp_datosPersonal =
                context.getSharedPreferences(getString(R.string.sp_datosPersonal_key),
                        Context.MODE_PRIVATE);
        SharedPreferences sp_datosPuestos =
                context.getSharedPreferences(getString(R.string.sp_datosPuestos_key),
                        Context.MODE_PRIVATE);

        //limpiar los archivos temporales
        sp_datosPersonal.edit().clear().apply();
        sp_datosPuestos.edit().clear().apply();

        //cerrar este activity y abrir AccederActivity
        abrirAccederActivity();
    }

    private void abrirAccederActivity(){
        Intent home = new Intent(this, AccederActivity.class);startActivity(home);
        finish();
    }
}
