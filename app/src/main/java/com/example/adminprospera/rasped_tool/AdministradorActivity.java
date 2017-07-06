package com.example.adminprospera.rasped_tool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
    FloatingActionButton fab_personal,fab_horarios,fab_puestos;
    Toolbar tb_ad;
    String[] arraryPersonal;
    JSONArray jsonArray = null;
    URL url = null;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //crear el entonrno inicial
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //establecer y mantener conexion externa
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        //Traer los componentes de activity_administradora esta clase para programarles
        vp_ad = (ViewPager) findViewById(R.id.vp_ad);
        tl_ad = (TabLayout) findViewById(R.id.tl_ad);
        fab_personal = (FloatingActionButton) findViewById(R.id.fab_anadeUsuario);
        fab_horarios = (FloatingActionButton) findViewById(R.id.fab_anadeHorario);
        fab_puestos = (FloatingActionButton) findViewById(R.id.fab_anadePuesto);
        tb_ad = (Toolbar) findViewById(R.id.tb_ad);

        //poblar el viewPager con su TabLayout (pestañas y contenido de ellas)
        setupViewPager(vp_ad);
        tl_ad.setupWithViewPager(vp_ad);

        //configurar los Floating Action Buttons (FAB) y sus respectivas tareas
        cerrarTodosFAB();
        abrirFAB(fab_personal);
        TareasFAB();

        //asignar un escuchador para el vp_ad en caso de ser scrolleado,seleccionado un item
        vp_ad.addOnPageChangeListener(onPageChangeListener);

        //poblar titulo y subtitulo de ActionBar
        poblarActionBar();



    }

    //declaracion del escuchador para asignarse a un ViewPager
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //devuelve valores del la pagina scrolleada
        }

        @Override
        public void onPageSelected(int position) {
            //devuelve un numero entero para indicar la pestaña activa o seleccionada desde el 0
            //al activar o seleccionar una nueva pestaña cerrar cualquier FAB abierto
            cerrarTodosFAB();

            //posterior a cerrar un FAB anterior, se abrira un nuevo FAB segun su pestaña
            switch (position){
                case 0:
                    //en caso de que la pestaña activa sea la 0 abrir el fab de añadir personal
                    abrirFAB(fab_personal);
                    break;
                case 1:
                    //en caso de que la pestaña activa sea la 1 abrir el fab de añadir horario
                    abrirFAB(fab_horarios);
                    break;
                case 2:
                    //en caso de que la pestaña activa sea la 2 abrir el fab de añadir puesto
                    abrirFAB(fab_puestos);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //devuelve es estado del scroll en un identificador de tipo int
        }
    };

    //metodo privado que asigna a los FAB un escuchador y una accion al ser activados respectivamente
    private void TareasFAB(){
        //escuchador y actividad para el FAB de añadir personal
        fab_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirPersonalActivity();
            }
        });
        //escuchador y actividad para el FAB de añadir horario
        fab_horarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirHorariosActivity();
            }
        });
        //escuchador y actividad para el FAB de añadir puesto
        fab_puestos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sbrirPuestosActivity();
            }
        });
    }

    //metodo privado para llenar arreglo arrayIdSedes
    public void llenarArregloPersonal(){
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
        //almacenarDatosPersonal();
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

    //metodo privado que llena el arreglo arrayIdSedes que llena el lv_ad_personal
    private void llenaArregloPersonal(String json){
        try{
            //preparar el arreglo JSON
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

    //metodo privado para abrir personalActivity
    private void abrirPersonalActivity(){
        Intent intent = new Intent(this, PersonalActivity.class);
        startActivityForResult(intent,0);
        //finish();
    }

    //metodo privado para abrir horariosActivity
    private void abrirHorariosActivity(){
        Intent intent = new Intent(this, HorariosActivity.class);
        startActivityForResult(intent,0);
        //finish();
    }

    //metodo privado para abrir puestosActivity
    private void sbrirPuestosActivity(){
        Intent intent = new Intent(this, PersonalActivity.class);
        startActivityForResult(intent,0);
        //finish();
    }

    //metodo privado para abrir confiuracionActivity
    private void abrirConfiguracionActivity(){
        Intent intent = new Intent(this, ConfiguracionActivity.class);
        startActivityForResult(intent,0);
        //finish();
    }

    //metodo para cerrar un FAB con su respectiva animacion
    private void cerrarFAB(FloatingActionButton FAB){
        FAB.animate()
                .scaleX(0)
                .scaleY(0)
                .setDuration(400);
        FAB.hide();
    }

    //metodo para abrir un FAB con su respectiva animacion
    private void abrirFAB(FloatingActionButton FAB){
        FAB.show();
        FAB.setScaleX(0);
        FAB.setScaleY(0);
        FAB.animate()
                .scaleX(1)
                .scaleY(1)
                .setDuration(400);
    }

    //metodo privado que cierra todos los FAB
    private void cerrarTodosFAB(){
        cerrarFAB(fab_horarios);
        cerrarFAB(fab_personal);
        cerrarFAB(fab_puestos);
    }

    //metodo privado para asignarle titulo y subtitulo al toolbar con los datos de usuario
    private void poblarActionBar(){
        Context context = this.getApplicationContext();
        SharedPreferences sp_datosPersonal = context.getSharedPreferences(getString(R.string.sp_datosPersonal_key),Context.MODE_PRIVATE);
        String cupo = sp_datosPersonal.getString(getString(R.string.sp_cupoPersonal_key),"null");
        String nombre_personal = sp_datosPersonal.getString(getString(R.string.sp_nombrePersonal_key),"null");
        String apellidos = sp_datosPersonal.getString(getString(R.string.sp_apellidoPPersonal_key),"null");
        String telefono = sp_datosPersonal.getString(getString(R.string.sp_telefonoPersonal_key),"null");
        String puesto = sp_datosPersonal.getString(getString(R.string.sp_puestoPersonal_key),"null");
        tb_ad.setTitle(puesto +" | "+ nombre_personal +" "+ apellidos);
        tb_ad.setSubtitle(cupo +" | "+ telefono);

        //configurar el toolbar con un estilo personalizado en este caso con ab_personal
        tb_ad.inflateMenu(R.menu.ab_personal);
        tb_ad.setOnMenuItemClickListener(onMenuItemClickListener);
    }

    Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.it_configuracion:
                    abrirConfiguracionActivity();
                    break;
            }
            return false;
        }
    };

    //metodo privado que codifica el contenido del viewPager(pestañas superiores)
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragAdmPersonal(), getString(R.string.st_personal));
        adapter.addFragment(new FragAdmHorarios(),  getString(R.string.st_horarios));
        adapter.addFragment(new FragAdmPuestos(),  getString(R.string.st_puestos));
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
}
