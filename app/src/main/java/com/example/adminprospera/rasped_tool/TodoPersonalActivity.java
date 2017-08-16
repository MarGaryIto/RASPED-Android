package com.example.adminprospera.rasped_tool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class TodoPersonalActivity extends AppCompatActivity {

    private ListAdapter adapterListView;
    JSONArray jsonArrayTodoPersonal;
    ArrayList<ModelAdministrador> model = new ArrayList<>();
    private Boolean lvLleno = false;
    ListView lv_ad_todoPersonal;
    MetodosJson metodosJson = new MetodosJson();
    String linkTodoPersonal = "https://rasped.herokuapp.com/content/personal.php";
    URL urlTodoPersonal = null;
    String[][] arraryTodoPersonal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_personal);

        //establecer y mantener conexion externa
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        //instanciar elementos a usar
        lv_ad_todoPersonal = (ListView) findViewById(R.id.lv_ad_todoPersonal);

        extraerTodoPersonal();

        if (!lvLleno) {
            poblarModelAdministrador();
        }

        //crear el adaptador que contiene los datos del personal
        adapterListView = new AdapterAdministrador(TodoPersonalActivity.this, model);

        //asignar el adaptador al listView
        lv_ad_todoPersonal.setAdapter(adapterListView);

        //una vez lleno el listView, se configura su estado a lleno
        lvLleno = true;

        //asignar un listener al listView
        lv_ad_todoPersonal.setOnItemClickListener(lv_listener);
    }

    //listener que abre horarios y prepara para editarlos
    AdapterView.OnItemClickListener lv_listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //extraer el id del personal seleccionado
            Intent intent = new Intent(TodoPersonalActivity.this, PersonalActivity.class);
            ModelAdministrador modelAdministrador = (ModelAdministrador) adapterListView.getItem(position);
            //extraer el id del personal seleccionado
            String cupo = modelAdministrador.getCupo();
            intent.putExtra("cupo",cupo);
            intent.putExtra("editar",true);
            startActivityForResult(intent,0);
        }
    };

    //metodo privado para extraer los datos remotos de todos los horarios;
    private void extraerTodoPersonal(){
        //creacion de un hilo
        Thread tr = new Thread(){
            @Override
            public void run(){

                //casteo de una clase exterior
                MetodosJson metodosJson = new MetodosJson();

                //llenar la url
                urlTodoPersonal = metodosJson.crearURL(linkTodoPersonal);

                //obtencion del json atraves de la clase exterior y la url
                final String jsonTodoPersonal = metodosJson.obtenerJSON(urlTodoPersonal);
                //creacion de un runOnUiThread para usar la clase llenaArreglo()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llenaArregloTodoPersonal(jsonTodoPersonal);

                    }
                });
            }
        };
        //ejecucion del hilo
        tr.run();
        almacenarDatosHorarios();
    }

    private void llenaArregloTodoPersonal(String json){
        try{
            //preparar el objeto JSON que contendra los resultados de una consulta de base de datos
            jsonArrayTodoPersonal = new JSONArray(json);
        }catch (JSONException e){
            mostrarToast("error: "+e.getMessage());
        }
        int registros = jsonArrayTodoPersonal.length();
        //preparar el arreglo Android
        arraryTodoPersonal = new String[13][registros];
        try{

            for (int i=0;i<registros;i++){
                //preparar un objeto JSON para la extraccion de datos
                JSONObject jsonObject = jsonArrayTodoPersonal.getJSONObject(i);

                //almacenar los datos en el arreglo columna por columna
                arraryTodoPersonal[0][i] = jsonObject.getString("id_personal");
                arraryTodoPersonal[1][i] = jsonObject.getString("sede");
                arraryTodoPersonal[2][i] = jsonObject.getString("cupo");
                arraryTodoPersonal[3][i] = jsonObject.getString("nombre_personal");
                arraryTodoPersonal[4][i] = jsonObject.getString("apellido_p");
                arraryTodoPersonal[5][i] = jsonObject.getString("apellido_m");
                arraryTodoPersonal[6][i] = jsonObject.getString("lada");
                arraryTodoPersonal[7][i] = jsonObject.getString("telefono");
                arraryTodoPersonal[8][i] = jsonObject.getString("contrasena");
                arraryTodoPersonal[9][i] = jsonObject.getString("horario");
                arraryTodoPersonal[10][i] = jsonObject.getString("puesto");
                arraryTodoPersonal[11][i] = jsonObject.getString("usuario");
                arraryTodoPersonal[12][i] = ""+i;
            }
        }catch (JSONException e){
            mostrarToast("error: "+e.getMessage());
        }
    }

    private void almacenarDatosHorarios(){
        try {
            Context context = this;
            SharedPreferences sp_datosTodoPersonal =
                    context.getSharedPreferences("sp_datosTodoPersonal",
                            Context.MODE_PRIVATE);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sp_datosTodoPersonal.edit();
            int registros = arraryTodoPersonal[0].length;
            for (int i=0;i<registros;i++){
                editor.putString("id_personal"+i,arraryTodoPersonal[0][i]);
                editor.putString("sede"+i,arraryTodoPersonal[1][i]);
                editor.putString("cupo"+i,arraryTodoPersonal[2][i]);
                editor.putString("nombre_personal"+i,arraryTodoPersonal[3][i]);
                editor.putString("apellido_p"+i,arraryTodoPersonal[4][i]);
                editor.putString("apellido_m"+i,arraryTodoPersonal[5][i]);
                editor.putString("lada"+i,arraryTodoPersonal[6][i]);
                editor.putString("telefono"+i,arraryTodoPersonal[7][i]);
                editor.putString("contrasena"+i,arraryTodoPersonal[8][i]);
                editor.putString("horario"+i,arraryTodoPersonal[9][i]);
                editor.putString("puesto"+i,arraryTodoPersonal[10][i]);
                editor.putString("usuario"+i,arraryTodoPersonal[11][i]);
                editor.putString("i"+i,arraryTodoPersonal[12][i]);
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

        Context context = this;
        SharedPreferences sp_datosTodoPersonal =
                context.getSharedPreferences("sp_datosTodoPersonal",
                        Context.MODE_PRIVATE);
        //Instanciar las clases
        ModelAdministrador modelAdministrador;

        int dimension = sp_datosTodoPersonal.getInt("registros", 0);

        for (int i = 0; i < dimension; i++) {
            modelAdministrador = new ModelAdministrador();

            String personal = sp_datosTodoPersonal.getString("nombre_personal" + i,"null")+" "+sp_datosTodoPersonal.getString("apellido_p" + i,"null");
            String cupo = sp_datosTodoPersonal.getString("sede" + i,"null")+sp_datosTodoPersonal.getString("cupo" + i,"null");



            //asignar las variables
            modelAdministrador.setNombrePersonal(personal);
            modelAdministrador.setCupo(cupo);
            modelAdministrador.setFecha("");
            modelAdministrador.setHora("");
            modelAdministrador.setId_personal(sp_datosTodoPersonal.getString("i" + i,"0"));

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
