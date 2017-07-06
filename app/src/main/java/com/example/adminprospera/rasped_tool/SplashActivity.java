package com.example.adminprospera.rasped_tool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    //duracion del splash, 2 segundos
    private static final long SPLASH_SCREEN_DELAY = 2000;
    ImageView iv_sp_icono;
    Animation dezp_derecha,dezp_izquierda;
    TextView tv_sp_appName;
    private static final String PREFS_NAME = "datosUsuario";
    private String[] datosPersonal;
    String[] arrayPersonal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //establecer orientacion portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //asignar contenido del activity
        setContentView(R.layout.activity_splash);

        //traer componentes del activity a la clase
        iv_sp_icono = (ImageView) findViewById(R.id.iv_sp_icono);
        tv_sp_appName = (TextView) findViewById(R.id.tv_sp_appName);
        dezp_derecha = AnimationUtils.loadAnimation(this, R.anim.dezp_derecha);
        dezp_izquierda = AnimationUtils.loadAnimation(this,R.anim.dezp_izquierda);

        //asignar animaciones a los componentes
        dezp_derecha.reset();
        iv_sp_icono.startAnimation(dezp_derecha);
        dezp_izquierda.reset();
        tv_sp_appName.startAnimation(dezp_izquierda);

        arrayPersonal = new String[]{"dos", "tres"};

        //extrae datos temporales
        evaluaCache();
    }



    //metodo privado para evaluar desde datos temporales, si ya se inicio sesion
    private void evaluaCache(){
        Context context = this.getApplicationContext();
        SharedPreferences sp_datosPersonal = context.getSharedPreferences(getString(R.string.sp_datosPersonal_key),Context.MODE_PRIVATE);
        String usuario = sp_datosPersonal.getString(getString(R.string.sp_usuarioPersonal_key),"null");
        mostrarToast("usuario");
        switch (usuario){
            case "root":
                //codigo para usuario root
                break;
            case "admin":
                abrirAdministradorActivity();
                break;
            case "regis":
                abrirRegistradorActivity();
                break;
            case "user":
                abrirUsusariosActivity();
                break;
            default:
                //cerar este layout y continuar con logg en caso de no existir algun usuario
                abrirAccederActivity();
                break;
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
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //script para abrir AdministradorActivity
                Intent intent = new Intent().setClass(SplashActivity.this,AdministradorActivity.class);
                startActivity(intent);

                //cerrar splashActivity (actual activity)
                finish();
            }
        };

        //ejecutar tiempo de espera para simular la carga de la aplicacion
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }

    //metodo privada para abrir usuariosActivity
    private void abrirUsusariosActivity(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //script para abrir UsuariosActivity
                Intent intent = new Intent().setClass(SplashActivity.this,UsuariosActivity.class);
                startActivity(intent);

                //cerrar splashActivity (actual activity)
                finish();
            }
        };

        //ejecutar tiempo de espera para simular la carga de la aplicacion
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }

    //metodo privado para abrir registradorActivity
    private void abrirRegistradorActivity(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //script para abrir RegistradorActivity
                Intent intent = new Intent().setClass(SplashActivity.this,RegistradorActivity.class);
                startActivity(intent);

                //cerrar splashActivity (actual activity)
                finish();
            }
        };

        //ejecutar tiempo de espera para simular la carga de la aplicacion
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);

    }

    private void abrirAccederActivity(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //script para abrir AccederActivity
                Intent intent = new Intent().setClass(SplashActivity.this,AccederActivity.class);
                startActivity(intent);

                //cerrar splashActivity (actual activity)
                finish();
            }
        };

        //ejecutar tiempo de espera para simular la carga de la aplicacion
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);

    }

    public String[] getArrayPersonal() {
        return arrayPersonal;
    }

    public void setArrayPersonal(String[] arrayPersonal) {
        this.arrayPersonal = arrayPersonal;
    }
}
