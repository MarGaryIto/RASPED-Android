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

import java.lang.reflect.Array;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    //duracion del splash, 3 segundos
    private static final long SPLASH_SCREEN_DELAY = 3000;
    ImageView iv_sp_icono;
    Animation dezp_derecha,dezp_izquierda;
    TextView tv_sp_appName;
    private static final String PREFS_NAME = "datosUsuario";
    String[] datosPersonal=null;

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

        //extrae datos temporales
        evaluaCache();

        //cerar este layout
        cerrarSplash();

    }

    //metodo privado para evaluar desde datos temporales, si ya se inicio sesion
    private void evaluaCache(){
        SharedPreferences sp_datosUsuario = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String idPersonal = sp_datosUsuario.getString("id_personal","null");
        switch (idPersonal){
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
                //en caso de no existir un usuario, continuar la actividad default del splash
                break;
        }
    }

    //metodo privada para abrir administradorActivity
    private void abrirAdministradorActivity(){
        Intent intent = new Intent(this, AdministradorActivity.class);
        startActivityForResult(intent,0);
    }

    //metodo privada para abrir usuariosActivity
    private void abrirUsusariosActivity(){
        Intent intent = new Intent(this, UsuariosActivity.class);
        startActivityForResult(intent,0);
    }

    //metodo privado para abrir registradorActivity
    private void abrirRegistradorActivity(){
        Intent intent = new Intent(this, RegistradorActivity.class);
        startActivityForResult(intent,0);
    }

    private void cerrarSplash(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //script para brir accederActivity
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
}
