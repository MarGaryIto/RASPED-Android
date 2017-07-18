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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RegistradorActivity extends AppCompatActivity {

    Toolbar tb_reg;
    TextView tv_reg_tBDescrip;
    Button bt_reg_scanear;


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
        bt_reg_scanear = (Button) findViewById(R.id.bt_reg_scanear);

        //poblar titulo y subtitulo de ActionBar
        poblarActionBar();

        bt_reg_scanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(RegistradorActivity.this).initiateScan();
            }
        });
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
        mostrarToast("resultado"+scan_result);
    }

    private void mostrarToast(String mensaje){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, mensaje, duration);
        toast.show();
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
                case R.id.it_cerrarSesion:
                    cerrarSesion();
                    break;
            }
            return false;
        }
    };

    //metodo privado para abrir confiuracionActivity
    private void abrirConfiguracionActivity(){
        Intent intent = new Intent(this, ConfiguracionActivity.class);
        startActivityForResult(intent,0);
        //finish();
    }

    private void actualizarTablas(){
        //code
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