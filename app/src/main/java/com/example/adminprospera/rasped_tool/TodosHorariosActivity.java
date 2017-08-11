package com.example.adminprospera.rasped_tool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class TodosHorariosActivity extends AppCompatActivity {

    private ListAdapter adapterListView;
    JSONArray jsonArrayHorarios;
    ArrayList<ModelAdministrador> model = new ArrayList<>();
    private Boolean lvLleno = false;
    ListView lv_todosHorarios;
    MetodosJson metodosJson = new MetodosJson();
    String linkTodosHorarios = "https://rasped.herokuapp.com/content/horariosTable.php";
    URL urlHorarios = null;
    String[][] arraryHorarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todos_horarios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //establecer y mantener conexion externa
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirAgregarHorario();
            }
        });

        extraerTodosHorarios();

        lv_todosHorarios = (ListView) findViewById(R.id.lv_todosHorarios);

        if (!lvLleno) {
            poblarModelAdministrador();
        }

        //crear el adaptador que contiene los datos del personal
        adapterListView = new AdapterAdministrador(TodosHorariosActivity.this, model);

        //asignar el adaptador al listView
        lv_todosHorarios.setAdapter(adapterListView);

        //una vez lleno el listView, se configura su estado a lleno
        lvLleno = true;

        //asignar un listener al listView
        lv_todosHorarios.setOnItemClickListener(lv_listener);
    }

    //listener que abre horarios y prepara para editarlos
    AdapterView.OnItemClickListener lv_listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ModelAdministrador modelAdministrador = (ModelAdministrador) adapterListView.getItem(position);
            //extraer el id del personal seleccionado
            String i = modelAdministrador.getId_personal();
            Intent intent = new Intent(TodosHorariosActivity.this, HorariosActivity.class);
            intent.putExtra("editar",true);
            intent.putExtra("i",i);
            //intent.putExtra("editar","");
            startActivityForResult(intent,0);
        }
    };

    //metodo privado para extraer los datos remotos de todos los horarios;
    private void extraerTodosHorarios(){
        //creacion de un hilo
        Thread tr = new Thread(){
            @Override
            public void run(){

                //casteo de una clase exterior
                MetodosJson metodosJson = new MetodosJson();

                //llenar la url
                urlHorarios = metodosJson.crearURL(linkTodosHorarios);

                //obtencion del json atraves de la clase exterior y la url
                final String jsonHorarios = metodosJson.obtenerJSON(urlHorarios);
                //creacion de un runOnUiThread para usar la clase llenaArreglo()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llenaArregloHorarios(jsonHorarios);

                    }
                });
            }
        };
        //ejecucion del hilo
        tr.run();
        almacenarDatosHorarios();
    }

    private void llenaArregloHorarios(String json){
        try{
            //preparar el objeto JSON que contendra los resultados de una consulta de base de datos
            jsonArrayHorarios = new JSONArray(json);
        }catch (JSONException e){
            mostrarToast("error: "+e.getMessage());
        }
        int registros = jsonArrayHorarios.length();
        //preparar el arreglo Android
        arraryHorarios = new String[8][registros];
        try{

            for (int i=0;i<registros;i++){
                //preparar un objeto JSON para la extraccion de datos
                JSONObject jsonObject = jsonArrayHorarios.getJSONObject(i);

                //almacenar los datos en el arreglo columna por columna
                arraryHorarios[0][i] = jsonObject.getString("id_horario");
                Log.e("arraryHorarios[0]["+i+"]",arraryHorarios[0][i]);
                arraryHorarios[1][i] = jsonObject.getString("hr_nombre");
                Log.e("arraryHorarios[1]["+i+"]",arraryHorarios[1][i]);
                arraryHorarios[2][i] = jsonObject.getString("hr_entrada");
                Log.e("arraryHorarios[2]["+i+"]",arraryHorarios[2][i]);
                arraryHorarios[3][i] = jsonObject.getString("hr_comida_i");
                Log.e("arraryHorarios[3]["+i+"]",arraryHorarios[3][i]);
                arraryHorarios[4][i] = jsonObject.getString("hr_comida_f");
                Log.e("arraryHorarios[4]["+i+"]",arraryHorarios[4][i]);
                arraryHorarios[5][i] = jsonObject.getString("hr_salida");
                Log.e("arraryHorarios[5]["+i+"]",arraryHorarios[5][i]);
                arraryHorarios[6][i] = jsonObject.getString("tolerancia");
                Log.e("arraryHorarios[6]["+i+"]",arraryHorarios[6][i]);
                arraryHorarios[7][i] = ""+i;
                Log.e("arraryHorarios[7]["+i+"]",arraryHorarios[7][i]);
            }
        }catch (JSONException e){
            mostrarToast("error: "+e.getMessage());
        }
    }

    private void almacenarDatosHorarios(){
        try {
            Context context = this;
            SharedPreferences sp_datosHorarios =
                    context.getSharedPreferences("sp_horariosTable",
                            Context.MODE_PRIVATE);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sp_datosHorarios.edit();
            int registros = arraryHorarios[0].length;
            Log.e("registros",""+registros);
            for (int i=0;i<registros;i++){
                editor.putString("id_horario"+i,arraryHorarios[0][i]);
                editor.putString("hr_nombre"+i,arraryHorarios[1][i]);
                editor.putString("hr_entrada"+i,arraryHorarios[2][i]);
                editor.putString("hr_comida_i"+i,arraryHorarios[3][i]);
                editor.putString("hr_comida_f"+i,arraryHorarios[4][i]);
                editor.putString("hr_salida"+i,arraryHorarios[5][i]);
                editor.putString("tolerancia"+i,arraryHorarios[6][i]);
                editor.putString("i"+i,arraryHorarios[7][i]);
            }
            editor.putInt("registros",registros);
            editor.apply();
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }

    }


    //metodo privado para traer el activity horarios y prepararlo para agregar uno nuevo
    private void abrirAgregarHorario(){
        Intent intent = new Intent(this, HorariosActivity.class);
        intent.putExtra("editar",false);
        startActivityForResult(intent,0);
        //finish();
    }

    //metodo privado para poblar el model para el ListView
    private void poblarModelAdministrador() {

        Context context = this.getApplicationContext();
        SharedPreferences sp_datosHorarios =
                context.getSharedPreferences("sp_horariosTable",
                        Context.MODE_PRIVATE);
        //Instanciar las clases
        ModelAdministrador modelAdministrador;

        int dimension = sp_datosHorarios.getInt("registros", 0);

        for (int i = 0; i < dimension; i++) {
            modelAdministrador = new ModelAdministrador();

            //asignar las variables
            modelAdministrador.setNombrePersonal(sp_datosHorarios.getString("hr_nombre" + i,"null"));
            modelAdministrador.setCupo(sp_datosHorarios.getString("tolerancia" + i,"null"));
            modelAdministrador.setFecha(sp_datosHorarios.getString("hr_entrada" + i,"null"));
            modelAdministrador.setHora(sp_datosHorarios.getString("hr_salida" + i,"null"));
            modelAdministrador.setId_personal(sp_datosHorarios.getString("i" + i,"0"));

            //una vez asignadas las variables, se añaden al model
            model.add(modelAdministrador);

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
