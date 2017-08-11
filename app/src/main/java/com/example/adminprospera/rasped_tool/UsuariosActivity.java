package com.example.adminprospera.rasped_tool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UsuariosActivity extends AppCompatActivity {

    //Generar variables globales para esta clase
    private Toolbar tb_us;
    TextView tv_us_tBDescrip;
    String cupo = "0";

    //variables generales o globales para la extraccion de datos asistenciales
    JSONArray jsonArrayAsistencias,jsonArrayRetardos,jsonArrayFaltas,jsonArrayPersonal;
    URL urlAsistencias,urlRetardos,urlFaltas,urlPersonal = null;
    String[][] arrayAsistencias,arrayRetardos,arrayFaltas,arrayPersonal;
    String titulo,subtitulo,describpcion = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        //establecer y mantener conexion externa
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        //obtener el personal a visualizar

        cupo = getIntent().getExtras().getString("cupo","0");
        extraerRegistros();

        //instanciar elementos para trabajar con ellos
        tv_us_tBDescrip = (TextView) findViewById(R.id.tv_us_tBDescrip);
        tb_us = (Toolbar) findViewById(R.id.tb_us);
        ViewPager vp_us = (ViewPager) findViewById(R.id.vp_us);
        TabLayout tl_us = (TabLayout) findViewById(R.id.tl_us);

        //asignar las paguinas al viewPager
        setupViewPager(vp_us);
        tl_us.setupWithViewPager(vp_us);

        personalizarToolBar();
    }

    //metodo privado que llenara el activity con datos de un personal general ingresado
    private void personalizarToolBar(){
        //configurar el toolbar con un estilo personalizado en este caso con ab_personal
        tb_us.inflateMenu(R.menu.ab_personal);
        tb_us.setOnMenuItemClickListener(onMenuItemClickListener);
        //personalizar titulos
        tb_us.setTitle(titulo);
        tb_us.setSubtitle(subtitulo);
        tv_us_tBDescrip.setText(describpcion);
    }

    /* EXTRAER LOS DATOS REMOSTOS DE ASISTENCIAS, FALTAS, INASISTENCIAS */

    //metodo privado para extraer los datos remotos de todos los horarios;
    private void extraerRegistros(){
        final String linkAsistencias = "https://rasped.herokuapp.com/content/asistencias_por_cupo.php?cupo="+cupo;
        final String linkRetardos = "https://rasped.herokuapp.com/content/retardos_por_cupo.php?cupo="+cupo;
        final String linkFaltas = "https://rasped.herokuapp.com/content/faltas_por_cupo.php?cupo="+cupo;
        final String linkPersonal = "https://rasped.herokuapp.com/content/personal_por_cupo.php?cupo="+cupo;
        //creacion de un hilo
        Thread tr = new Thread(){
            @Override
            public void run(){

                //casteo de una clase exterior
                MetodosJson metodosJson = new MetodosJson();

                //crear variables url con el link
                urlAsistencias = metodosJson.crearURL(linkAsistencias);
                urlRetardos = metodosJson.crearURL(linkRetardos);
                urlFaltas = metodosJson.crearURL(linkFaltas);
                urlPersonal = metodosJson.crearURL(linkPersonal);

                //obtencion del json atraves de la clase exterior y la url
                final String jsonAsistencias = metodosJson.obtenerJSON(urlAsistencias);
                final String jsonRetardos = metodosJson.obtenerJSON(urlRetardos);
                final String jsonFalta = metodosJson.obtenerJSON(urlFaltas);
                final String jsonPersonal = metodosJson.obtenerJSON(urlPersonal);

                //creacion de un runOnUiThread para usar la clase llenaArreglo()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llenaArregloRegistros(jsonAsistencias,jsonRetardos,jsonFalta,jsonPersonal);

                    }
                });
            }
        };
        //ejecucion del hilo
        tr.run();
        almacenarRegistros();
    }

    private void llenaArregloRegistros(String jsonAsist,String jsonRet,String jsonFalt,String jsonPer){
        try{
            //preparar el objeto JSON que contendra los resultados de una consulta de base de datos
            jsonArrayAsistencias = new JSONArray(jsonAsist);
            jsonArrayRetardos = new JSONArray(jsonRet);
            jsonArrayFaltas = new JSONArray(jsonFalt);
            jsonArrayPersonal = new  JSONArray(jsonPer);

        }catch (JSONException e){
            mostrarToast("error: "+e.getMessage());
        }
        int noAsistencias = jsonArrayAsistencias.length();
        int noRetardos = jsonArrayRetardos.length();
        int noFaltas = jsonArrayFaltas.length();

        //preparar el arreglo Android
        arrayAsistencias= new String[6][noAsistencias];
        arrayRetardos= new String[6][noRetardos];
        arrayFaltas= new String[6][noFaltas];
        try{
            //asistencias
            for (int i=0;i<noAsistencias;i++){
                //preparar un objeto JSON para la extraccion de datos
                JSONObject jsonObject = jsonArrayAsistencias.getJSONObject(i);

                //almacenar los datos en el arreglo columna por columna
                arrayAsistencias[0][i] = jsonObject.getString("fecha");
                arrayAsistencias[1][i] = jsonObject.getString("hr_entrada");
                arrayAsistencias[2][i] = jsonObject.getString("hr_comida_i");
                arrayAsistencias[3][i] = jsonObject.getString("hr_comida_f");
                arrayAsistencias[4][i] = jsonObject.getString("hr_salida");
                arrayAsistencias[5][i] = ""+i;
            }
            //retardos
            for (int i=0;i<noRetardos;i++){
                //preparar un objeto JSON para la extraccion de datos
                JSONObject jsonObject = jsonArrayRetardos.getJSONObject(i);

                //almacenar los datos en el arreglo columna por columna
                arrayRetardos[0][i] = jsonObject.getString("fecha");
                arrayRetardos[1][i] = jsonObject.getString("hr_entrada");
                arrayRetardos[2][i] = jsonObject.getString("hr_comida_i");
                arrayRetardos[3][i] = jsonObject.getString("hr_comida_f");
                arrayRetardos[4][i] = jsonObject.getString("hr_salida");
                arrayRetardos[5][i] = ""+i;
            }
            //faltas
            for (int i=0;i<noFaltas;i++){
                //preparar un objeto JSON para la extraccion de datos
                JSONObject jsonObject = jsonArrayFaltas.getJSONObject(i);

                //almacenar los datos en el arreglo columna por columna
                arrayFaltas[0][i] = jsonObject.getString("fecha");
                arrayFaltas[1][i] = jsonObject.getString("hr_entrada");
                arrayFaltas[2][i] = jsonObject.getString("hr_comida_i");
                arrayFaltas[3][i] = jsonObject.getString("hr_comida_f");
                arrayFaltas[4][i] = jsonObject.getString("hr_salida");
                arrayFaltas[5][i] = ""+i;
            }
            //personal
            JSONObject jsonObject = jsonArrayPersonal.getJSONObject(0);

            String personal = jsonObject.getString("nombre_personal");
            String apellidoP = jsonObject.getString("apellido_p");
            String apellidoM = jsonObject.getString("apellido_m");
            titulo = personal+" "+apellidoP+" "+apellidoM;

            String sede = jsonObject.getString("sede");
            String cupo = jsonObject.getString("cupo");
            String lada = jsonObject.getString("lada");
            String telefono = jsonObject.getString("telefono");
            subtitulo = sede+cupo+" | "+lada+telefono;

            String puesto = jsonObject.getString("puesto");
            describpcion = puesto;
        }catch (JSONException e){
            mostrarToast("error: "+e.getMessage());
        }
    }

    private void almacenarRegistros(){
        try {
            Context context = this;
            SharedPreferences.Editor editor;

            //asistencias
            SharedPreferences sp_datosAsistenciasCupo =
                    context.getSharedPreferences("sp_datosAsistenciasCupo",
                            Context.MODE_PRIVATE);

            editor = sp_datosAsistenciasCupo.edit();
            int noAsistencias = arrayAsistencias[0].length;
            for (int i=0;i<noAsistencias;i++){
                editor.putString("fecha"+i,arrayAsistencias[0][i]);
                editor.putString("hr_entrada"+i,arrayAsistencias[1][i]);
                editor.putString("hr_comida_i"+i,arrayAsistencias[2][i]);
                editor.putString("hr_comida_f"+i,arrayAsistencias[3][i]);
                editor.putString("hr_salida"+i,arrayAsistencias[4][i]);
                editor.putString("i"+i,arrayAsistencias[5][i]);
            }
            editor.putInt("registros",noAsistencias);
            editor.apply();

            //retardos
            SharedPreferences sp_datosRetardosCupo =
                    context.getSharedPreferences("sp_datosRetardosCupo",
                            Context.MODE_PRIVATE);

            editor = sp_datosRetardosCupo.edit();
            int noRetardos = arrayRetardos[0].length;
            for (int i=0;i<noRetardos;i++){
                editor.putString("fecha"+i,arrayRetardos[0][i]);
                editor.putString("hr_entrada"+i,arrayRetardos[1][i]);
                editor.putString("hr_comida_i"+i,arrayRetardos[2][i]);
                editor.putString("hr_comida_f"+i,arrayRetardos[3][i]);
                editor.putString("hr_salida"+i,arrayRetardos[4][i]);
                editor.putString("i"+i,arrayRetardos[5][i]);
            }
            editor.putInt("registros",noRetardos);
            editor.apply();

            //faltas
            SharedPreferences sp_datosFaltasCupo =
                    context.getSharedPreferences("sp_datosFaltasCupo",
                            Context.MODE_PRIVATE);

            editor = sp_datosFaltasCupo.edit();
            int noFaltas = arrayFaltas[0].length;
            for (int i=0;i<noFaltas;i++){
                editor.putString("fecha"+i,arrayFaltas[0][i]);
                editor.putString("hr_entrada"+i,arrayFaltas[1][i]);
                editor.putString("hr_comida_i"+i,arrayFaltas[2][i]);
                editor.putString("hr_comida_f"+i,arrayFaltas[3][i]);
                editor.putString("hr_salida"+i,arrayFaltas[4][i]);
                editor.putString("i"+i,arrayFaltas[5][i]);
            }
            editor.putInt("registros",noFaltas);
            editor.apply();

        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }

    }

    /* CODIFICACION DEL VIEWPAGER **/

    //escuchador para el toolbar, asignando una accion al activar un item
    Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
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

    //metodo privado para abrir confiuracionActivity
    private void abrirConfiguracionActivity(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent,0);
        //finish();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragUsuHoy(), getString(R.string.st_asistencia));
        adapter.addFragment(new FragUsuSemanal(),  getString(R.string.st_retardos));
        adapter.addFragment(new FragUsuMensual(),  getString(R.string.st_faltas));
        viewPager.setAdapter(adapter);
    }
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

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

}
