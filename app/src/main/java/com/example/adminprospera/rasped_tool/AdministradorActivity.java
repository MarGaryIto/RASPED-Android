package com.example.adminprospera.rasped_tool;

import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class AdministradorActivity extends AppCompatActivity {

    //Generar variables globales para esta clase
    TabLayout tl_ad;
    ViewPager vp_ad;
    ListView lv_ad_personal;
    FloatingActionButton fab_administrador;

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
        lv_ad_personal = (ListView) findViewById(R.id.lv_ad_personal);
        fab_administrador = (FloatingActionButton) findViewById(R.id.fab_administrador);

        //poblar el viewPager con su TabLayout (pestañas y contenido de ellas)
        setupViewPager(vp_ad);
        tl_ad.setupWithViewPager(vp_ad);

        //animar y configurar el Floating Action Buttons (FAB)
        cerrarFAB();
        configurarFABPersonal();
        tareaDelFAB();

        //llenado e impresion del arreglo
        //llenarArreglo();
        //mostrarToast(arraryPersonal[0]);

        //establecer un adaptador que contendra el estilo y el arreglo de datos para el lv_ad_personal
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.lay_list_view,R.id.tv_nombreItem,arrayIdSedes);

        //asignar al lv_ad_personal el adaptador adapter que contiene los datos y estilo
        //lv_ad_personal.setAdapter(adapter);

    }

    private void tareaDelFAB(){
        fab_administrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirPersonalActivity();
            }
        });
    }

    //metodo privado para abrir personalActivity
    private void abrirPersonalActivity(){
        Intent intent = new Intent(this, PersonalActivity.class);
        startActivityForResult(intent,0);
        //finish();
    }

    //metodo para animaciones del FAB
    private void cerrarFAB(){
        fab_administrador.setScaleX(0);
        fab_administrador.setScaleY(0);
    }

    //metodo para animaciones del FAB
    private void configurarFABPersonal(){
        fab_administrador.animate()
                .scaleX(1)
                .scaleY(1)
                .setDuration(600)
                .setStartDelay(1000);
    }

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
