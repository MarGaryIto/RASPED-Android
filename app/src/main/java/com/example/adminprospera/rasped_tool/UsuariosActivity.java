package com.example.adminprospera.rasped_tool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class UsuariosActivity extends AppCompatActivity {

    //Generar variables globales para esta clase
    private Toolbar tb_us;
    TextView tv_us_tBDescrip;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        tb_us = (Toolbar) findViewById(R.id.tb_us);
        ViewPager vp_us = (ViewPager) findViewById(R.id.vp_us);
        setupViewPager(vp_us);

        TabLayout tl_us = (TabLayout) findViewById(R.id.tl_us);
        tl_us.setupWithViewPager(vp_us);

        tv_us_tBDescrip = (TextView) findViewById(R.id.tv_us_tBDescrip);

        //poblar titulo y subtitulo de ActionBar
        poblarActionBar();

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
        tb_us.setTitle(nombre_user+" "+apellidos);
        tb_us.setSubtitle(sede+cupo +" | "+ lada+telefono);
        tv_us_tBDescrip.setText(puesto);
        //configurar el toolbar con un estilo personalizado en este caso con ab_personal
        tb_us.inflateMenu(R.menu.ab_personal);
        tb_us.setOnMenuItemClickListener(onMenuItemClickListener);
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
                case R.id.it_cerrarSesion:
                    cerrarSesion();
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
        Intent intent = new Intent(this, ConfiguracionActivity.class);
        startActivityForResult(intent,0);
        //finish();
    }

    //metodo privado para cerrar sesion
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
        Intent intent = new Intent(this, AccederActivity.class);
        startActivityForResult(intent,0);
        finish();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragUsuHoy(), getString(R.string.st_hoy));
        adapter.addFragment(new FragUsuSemanal(),  getString(R.string.st_semanal));
        adapter.addFragment(new FragUsuMensual(),  getString(R.string.st_mensual));
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
