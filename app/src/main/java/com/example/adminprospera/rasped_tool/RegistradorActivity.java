package com.example.adminprospera.rasped_tool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class RegistradorActivity extends AppCompatActivity {

    Toolbar tb_reg;
    TextView tv_reg_tBDescrip;
    ObtenerFechasHoras obtenerFechasHoras = new ObtenerFechasHoras();
    CuadrosDialogo cuadrosDialogo = new CuadrosDialogo();

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrador);

        ViewPager vp_reg = (ViewPager) findViewById(R.id.vp_reg);
        setupViewPager(vp_reg);

        TabLayout tl_reg = (TabLayout) findViewById(R.id.tl_reg);
        tl_reg.setupWithViewPager(vp_reg);
        tb_reg = (Toolbar) findViewById(R.id.tb_reg);
        tv_reg_tBDescrip = (TextView) findViewById(R.id.tv_reg_tBDescrip);

        //poblar titulo y subtitulo de ActionBar
        poblarActionBar();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        final IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        handleResult(scanResult);
    }

    private void handleResult(IntentResult scanResult) {
        if (scanResult != null) {
            updateUITextViews(scanResult.getContents(), scanResult.getFormatName());
        } else {
            Toast.makeText(this, "No se ha leído nada :(", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUITextViews(String scan_result, String scan_result_format) {
        evaluaResultado(""+scan_result);
    }

    private void evaluaResultado(String resultado){

        if(resultado.length()==6){

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            SharedPreferences prefs = getSharedPreferences(getString(R.string.sp_tiempos_key),Context.MODE_PRIVATE);
            String tiempo = prefs.getString(getString(R.string.sp_tiempos_key),"");
            prefs.edit().clear();
            String fecha = obtenerFechasHoras.fecha();
            String hora = obtenerFechasHoras.hora();

            HttpHandler handler = new HttpHandler();
            String txt = handler.postAsistencia(tiempo,resultado,fecha,hora);

            if(txt.equals("true")){
                cuadrosDialogo.cuadroDialogo(
                        getString(R.string.st_aceptar),
                        getString(R.string.st_registroExitoso),
                        getString(R.string.st_hey),
                        this
                );
            }else {
                cuadrosDialogo.cuadroDialogo(
                        getString(R.string.st_aceptar),
                        getString(R.string.ms_usuarioNoEncontrado),
                        getString(R.string.st_hey),
                        this
                );
            }



        }else {
            cuadrosDialogo.cuadroDialogo(
                    getString(R.string.st_aceptar),
                    getString(R.string.ms_errorDatos),
                    getString(R.string.st_hey),
                    this
            );
        }
    }

    //metodo privado para asignarle titulo y subtitulo al toolbar con los datos de usuario
    private void poblarActionBar(){
        Context context = this.getApplicationContext();
        SharedPreferences sp_datosPersonal = context.getSharedPreferences(getString(R.string.sp_datosPersonal_key),Context.MODE_PRIVATE);
        String nombre_user = sp_datosPersonal.getString(getString(R.string.sp_nombrePersonal_key),"null");
        String cupo = sp_datosPersonal.getString(getString(R.string.sp_cupoPersonal_key),"null");
        String sede = sp_datosPersonal.getString(getString(R.string.sp_sedePersonal_key),"null");
        String apellidos = sp_datosPersonal.getString(getString(R.string.sp_apellidoPPersonal_key),"null");
        String lada = sp_datosPersonal.getString(getString(R.string.sp_ladaPersonal_key),"null");
        String telefono = sp_datosPersonal.getString(getString(R.string.sp_telefonoPersonal_key),"null");
        String puesto = sp_datosPersonal.getString(getString(R.string.sp_puestoPersonal_key),"null");
        tb_reg.setTitle(nombre_user+" "+apellidos);
        tb_reg.setSubtitle(sede+cupo +" | "+ lada+telefono);
        tv_reg_tBDescrip.setText(puesto);
        //configurar el toolbar con un estilo personalizado en este caso con ab_personal
        tb_reg.inflateMenu(R.menu.ab_personal);
        tb_reg.setOnMenuItemClickListener(onMenuItemClickListener);
    }

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

    //metodo privado para abrir confiuracionActivity
    private void abrirConfiguracionActivity(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent,0);
        //finish();
    }

    private void actualizarTablas(){
        //code
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragRegEscanear(), getString(R.string.st_escanear));
        adapter.addFragment(new FragRegCapturar(),  getString(R.string.st_capturar));
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

}