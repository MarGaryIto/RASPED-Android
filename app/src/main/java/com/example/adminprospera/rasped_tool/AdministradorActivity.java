package com.example.adminprospera.rasped_tool;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AdministradorActivity extends AppCompatActivity {

    //Generar variables globales para esta clase
    TabLayout tl_ad;
    ViewPager vp_ad;
    FloatingActionButton fab_personal;
    Toolbar tb_ad;
    JSONArray jsonArray = null;
    String linkAsistencias = "https://rasped.herokuapp.com/content/asistencias.php";
    String linkRetardos = "https://rasped.herokuapp.com/content/retardos.php";
    String linkFaltas = "https://rasped.herokuapp.com/content/faltas.php";
    CuadrosDialogo cuadrosDialogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //crear el entonrno inicial
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);

        //establecer y mantener conexion externa
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        //Traer los componentes de activity_administradora esta clase para programarles
        vp_ad = (ViewPager) findViewById(R.id.vp_ad);
        tl_ad = (TabLayout) findViewById(R.id.tl_ad);
        fab_personal = (FloatingActionButton) findViewById(R.id.fab_anadeUsuario);
        tb_ad = (Toolbar) findViewById(R.id.tb_ad);

        //poblar el viewPager con su TabLayout (pestañas y contenido de ellas)
        setupViewPager(vp_ad);
        tl_ad.setupWithViewPager(vp_ad);

        //poblar titulo y subtitulo de ActionBar
        poblarActionBar();

        fab_personal.setOnClickListener(listenerFAB);

        evaluarConexion();
    }

    //comprobar si la Network es habilitada
    public boolean conexcionInternet() {
        try {
            Context context = getBaseContext();
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
        }catch (Exception e){
            //Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    //evalua conexion
    private void evaluarConexion(){

        if (conexcionInternet()){
            mostrarToast("conextado");
            llenarListas();
        }else {
            mostrarToast("desconectado");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //metodo privado para llenar arreglo arrayIdSedes
    private void llenarListas(){
        //creacion de un hilo
        Thread tr = new Thread(){
            @Override
            public void run(){

                //casteo de una clase exterior
                MetodosJson metodosJson = new MetodosJson();

                //llenar la url
                URL ulrAsistencias = metodosJson.crearURL(linkAsistencias);
                URL ulrRetardos = metodosJson.crearURL(linkRetardos);
                URL ulrFaltas = metodosJson.crearURL(linkFaltas);

                //obtencion del json atraves de la clase exterior y la url
                final String jsonAsistencias = metodosJson.obtenerJSON(ulrAsistencias);
                final String jsonRetardos = metodosJson.obtenerJSON(ulrRetardos);
                final String jsonFaltas = metodosJson.obtenerJSON(ulrFaltas);

                //creacion de un runOnUiThread para usar la clase llenaArreglo()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llenaPrefersAsistencias(jsonAsistencias);
                        llenaPrefersRetardos(jsonRetardos);
                        llenaPrefersFaltas(jsonFaltas);
                    }
                });
            }
        };
        //ejecucion del hilo
        tr.run();
    }


    //metodo privado que extrae datos de la base de datos remotos auxiliado de un JavaScript Object
    // Notation (JSON) y almacenarlos en memoria temporal
    private void llenaPrefersAsistencias(String json){
        try{

            //preparar el objeto JSON que contiene los datos de la consulta a la base de datos
            jsonArray = new JSONArray(json);

            //asigna a una variable el numero de registros de la consulta
            int registros = jsonArray.length();

            //prepara un archivo temporal llamado sp_asistencias
            Context context = this.getApplicationContext();
            SharedPreferences sp_asistencias = context.getSharedPreferences(getString(R.string.sp_asistencias_key),Context.MODE_PRIVATE);

            //prepara un editor que permitira poblar el archivo temporal sp_asistencias
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sp_asistencias.edit();

            //ejecucion de un ciclo, se repetira segun el numero de registros existentes
            for (int i=0;i<registros;i++){

                //prepara el registro numero i al que se le extraera cada valor
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                //el editor poblara de strings con una llave y un valor, la llave identifica el
                // registro, el valor sera extraido del registro i tal como el cupo
                editor.putString("asistencias"+getString(R.string.sp_cupoPersonal_key)+i,jsonObject.getString("cupo"));
                editor.putString("asistencias"+getString(R.string.sp_id_personal_key)+i,jsonObject.getString("id_personal"));
                editor.putString("asistencias"+getString(R.string.sp_id_asistencias_key)+i,jsonObject.getString("id_asistencias"));
                editor.putString("asistencias"+getString(R.string.sp_nombre_personal_key)+i,jsonObject.getString("nombre_personal"));
                editor.putString("asistencias"+getString(R.string.sp_apellido_m_key)+i,jsonObject.getString("apellido_m"));
                editor.putString("asistencias"+getString(R.string.sp_apellido_p_key)+i,jsonObject.getString("apellido_p"));
                editor.putString("asistencias"+getString(R.string.sp_fecha_key)+i,jsonObject.getString("fecha"));
                editor.putString("asistencias"+getString(R.string.sp_hr_entrada_key)+i,jsonObject.getString("hr_entrada"));
                editor.putString("asistencias"+getString(R.string.sp_hr_comida_i_key)+i,jsonObject.getString("hr_comida_i"));
                editor.putString("asistencias"+getString(R.string.sp_hr_comida_f_key)+i,jsonObject.getString("hr_comida_f"));
                editor.putString("asistencias"+getString(R.string.sp_hr_salida_key)+i,jsonObject.getString("hr_salida"));
            }

            int registrosViejos = sp_asistencias.getInt(getString(R.string.sp_numAsistencias_key),0);

            //finalmente se almancena el numero de asistencias registradas en los datos temporales
            editor.putInt(getString(R.string.sp_numAsistencias_key),registros);

            //una vez ingresados los datos en el sp_asistencias se cierra el editso
            editor.apply();

            notificacionesDesde(registrosViejos);

        }catch (JSONException e){

            //el contenido de catch se ejecutara solo en caso de que el contenido del try haya tenido
            // errores, en este caso se extrae el error y se muestra en un cuadro de dialogo
            cuadrosDialogo.cuadroDialogo("ok",e.toString(),e.getMessage(),this);
        }
    }

    private void notificacionesDesde(int registrosViejos){
        Context context = this.getApplicationContext();
        SharedPreferences sp_asistencias = context.getSharedPreferences(getString(R.string.sp_asistencias_key),Context.MODE_PRIVATE);
        int nuevosRegistros = sp_asistencias.getInt(getString(R.string.sp_numAsistencias_key),0);
        String cadena = "";
        while (registrosViejos<nuevosRegistros){
            String personal = sp_asistencias.getString("asistencias"+getString(R.string.sp_nombre_personal_key)+registrosViejos,"null");
            String entrada = sp_asistencias.getString("asistencias"+getString(R.string.sp_hr_entrada_key)+registrosViejos,"null");
            cadena += "\n"+personal+" | "+entrada;
            registrosViejos+=1;
        }
        mostrarNotificacion(cadena);

    }

    //metodo privado que llena el arreglo arrayIdSedes que llena el lv_ad_personal
    private void llenaPrefersRetardos(String json){
        try{
            //preparar el arreglo JSON
            jsonArray = new JSONArray(json);
            int registros = jsonArray.length();
            //preparar el arreglo Android

            Context context = this.getApplicationContext();
            SharedPreferences sp_retardos = context.getSharedPreferences(getString(R.string.sp_retardos_key),Context.MODE_PRIVATE);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sp_retardos.edit();

            for (int i=0;i<registros;i++){
                //preparar un objeto JSON para la extraccion de datos
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                editor.putString("retardos"+getString(R.string.sp_id_personal_key)+i,jsonObject.getString("id_personal"));
                editor.putString("retardos"+getString(R.string.sp_cupoPersonal_key)+i,jsonObject.getString("cupo"));
                editor.putString("retardos"+getString(R.string.sp_id_retardos_key)+i,jsonObject.getString("id_asistencias"));
                editor.putString("retardos"+getString(R.string.sp_nombre_personal_key)+i,jsonObject.getString("nombre_personal"));
                editor.putString("retardos"+getString(R.string.sp_apellido_m_key)+i,jsonObject.getString("apellido_m"));
                editor.putString("retardos"+getString(R.string.sp_apellido_p_key)+i,jsonObject.getString("apellido_p"));
                editor.putString("retardos"+getString(R.string.sp_fecha_key)+i,jsonObject.getString("fecha"));
                editor.putString("retardos"+getString(R.string.sp_hr_entrada_key)+i,jsonObject.getString("hr_entrada"));
                editor.putString("retardos"+getString(R.string.sp_hr_comida_i_key)+i,jsonObject.getString("hr_comida_i"));
                editor.putString("retardos"+getString(R.string.sp_hr_comida_f_key)+i,jsonObject.getString("hr_comida_f"));
                editor.putString("retardos"+getString(R.string.sp_hr_salida_key)+i,jsonObject.getString("hr_salida"));

            }
            editor.putInt(getString(R.string.sp_numRetardos_key),registros);
            editor.apply();
        }catch (JSONException e){
            cuadrosDialogo.cuadroDialogo("ok",e.toString(),e.getMessage(),this);
        }
    }

    //metodo privado que llena el arreglo arrayIdSedes que llena el lv_ad_personal
    private void llenaPrefersFaltas(String json){
        try{
            //preparar el arreglo JSON
            jsonArray = new JSONArray(json);
            int registros = jsonArray.length();
            //preparar el arreglo Android

            Context context = this.getApplicationContext();
            SharedPreferences sp_faltas = context.getSharedPreferences(getString(R.string.sp_faltas_key),Context.MODE_PRIVATE);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sp_faltas.edit();

            for (int i=0;i<registros;i++){
                //preparar un objeto JSON para la extraccion de datos
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                editor.putString("faltas"+getString(R.string.sp_id_personal_key)+i,jsonObject.getString("id_personal"));
                editor.putString("faltas"+getString(R.string.sp_cupoPersonal_key)+i,jsonObject.getString("cupo"));
                editor.putString("faltas"+getString(R.string.sp_id_falta_key)+i,jsonObject.getString("id_falta"));
                editor.putString("faltas"+getString(R.string.sp_nombre_personal_key)+i,jsonObject.getString("nombre_personal"));
                editor.putString("faltas"+getString(R.string.sp_apellido_m_key)+i,jsonObject.getString("apellido_m"));
                editor.putString("faltas"+getString(R.string.sp_apellido_p_key)+i,jsonObject.getString("apellido_p"));
                editor.putString("faltas"+getString(R.string.sp_fecha_key)+i,jsonObject.getString("fecha"));

            }
            editor.putInt(getString(R.string.sp_numFaltas_key),registros);
            editor.apply();
        }catch (JSONException e){
            cuadrosDialogo.cuadroDialogo("ok",e.toString(),e.getMessage(),this);
        }
    }

    View.OnClickListener listenerFAB = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            abrirPersonalActivity();
        }
    };

    //metodo privado para abrir personalActivity
    private void abrirPersonalActivity(){
        Intent intent = new Intent(this, PersonalActivity.class);
        intent.putExtra("editar",false);
        startActivityForResult(intent,0);
        //finish();
    }

    //metodo privado para abrir confiuracionActivity
    private void abrirConfiguracionActivity(){
        startActivity(new Intent(this, SettingsActivity.class));
        //Intent intent = new Intent(this, SettingsActivity.class);
        //startActivityForResult(intent,0);
        //finish();
    }

    //metodo privado para abrir confiuracionActivity
    private void abrirTodosHorarios(){
        Intent intent = new Intent(this, TodosHorariosActivity.class);
        startActivityForResult(intent,0);
        //finish();
    }

    //metodo privado para abrir confiuracionActivity
    private void abrirTodoPersonal(){
        Intent intent = new Intent(this, TodoPersonalActivity.class);
        startActivityForResult(intent,0);
        //finish();
    }

    //metodo privado para asignarle titulo y subtitulo al toolbar con los datos de usuario
    private void poblarActionBar(){
        Context context = this.getApplicationContext();
        SharedPreferences sp_datosPersonal = context.getSharedPreferences(getString(R.string.sp_datosPersonal_key),Context.MODE_PRIVATE);
        String sede = sp_datosPersonal.getString(getString(R.string.sp_cupoPersonal_key),"null");
        String cupo = sp_datosPersonal.getString(getString(R.string.sp_cupoPersonal_key),"null");
        String nombre_personal = sp_datosPersonal.getString(getString(R.string.sp_nombrePersonal_key),"null");
        String apellidos = sp_datosPersonal.getString(getString(R.string.sp_apellidoPPersonal_key),"null");
        String lada = sp_datosPersonal.getString(getString(R.string.sp_ladaPersonal_key),"null");
        String telefono = sp_datosPersonal.getString(getString(R.string.sp_telefonoPersonal_key),"null");
        String puesto = sp_datosPersonal.getString(getString(R.string.sp_puestoPersonal_key),"null");
        tb_ad.setTitle(puesto +" | "+ nombre_personal +" "+ apellidos);
        tb_ad.setSubtitle(sede+cupo +" | "+ lada+telefono);

        //configurar el toolbar con un estilo personalizado en este caso con ab_personal
        tb_ad.inflateMenu(R.menu.ab_administrador);
        tb_ad.setOnMenuItemClickListener(onMenuItemClickListener);

    }

    //escuchador para el toolbar, asignando una accion al activar un item
    Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.it_todoPersonal:
                    abrirTodoPersonal();
                    break;
                case R.id.it_horarios:
                    abrirTodosHorarios();
                    break;
                case R.id.it_configuracion:
                    abrirConfiguracionActivity();
                    break;
                case R.id.it_actualizar:
                    actualizarTablas();
                    break;
            }
            return false;
        }
    };

    private void actualizarTablas(){
        //code
    }

    //metodo privado que codifica el contenido del viewPager(pestañas superiores)
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragAdmAsistencias(), getString(R.string.st_asistencia));
        adapter.addFragment(new FragAdmRetardos(),  getString(R.string.st_retardos));
        adapter.addFragment(new FragAdmFaltas(),  getString(R.string.st_faltas));
        viewPager.setAdapter(adapter);
    }

    //clase privada que asigna el contenido y comportamiento del ViewPager(contenido de cada pestaña)
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        //asignar un adaptador al viewPager
        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        //obtener el item(pestaña) seleccionado para mostrar su respectivo contenido
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        //obtener la cuenta
        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        //mostrar contenido del fragment(contenido de la respectiva pestaña)
        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        //obtener el nombre del fragment
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    //metodo privado que mostrara un toast, de mensaje contendra variables
    private void mostrarToast(String mensaje){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, mensaje, duration);
        toast.show();
    }

    public void mostrarNotificacion(String mensj) {

        //construccion del notification
        NotificationCompat.Builder mBuilder;
        NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        //parametros del la notificacion almacenados en variables
        int icono = R.drawable.ic_stat_name;//icono
        Intent i=new Intent(this, AdministradorActivity.class);//donde se ejecuta,a donde lleva
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);//preparacion
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);//sonido

        //asignacion de parametros
        mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                .setContentIntent(pendingIntent)//actividad a realizar
                .setSmallIcon(icono)//icono
                .setContentTitle(getString(R.string.app_name))//titulo
                .setContentText(mensj)//mensaje
                .setVibrate(new long[] {100, 250, 100, 500})//vibracion
                .setAutoCancel(false)//cancelar automaticamente(no)
                .setColor(getResources().getColor(R.color.colorPrimaryDark))
                .setSound(defaultSound);//sonido

        //mostrar notificacion
        mNotifyMgr.notify(1, mBuilder.build());
    }
}
