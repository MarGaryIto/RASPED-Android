package com.example.adminprospera.rasped_tool;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import org.apache.http.util.EntityUtils;

public class HttpHandler {

    public String postAsistencia(String tiempo,String cupo,String fecha,String hora){

        try {

            String posturl = "https://rasped.herokuapp.com/content/insertar_asistencia.php";
            HttpClient httpclient = new DefaultHttpClient();
            /*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
            HttpPost httppost = new HttpPost(posturl);
            /*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
            //AÑADIR PARAMETROS
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("tiempo",tiempo));
            params.add(new BasicNameValuePair("cupo",cupo));
            params.add(new BasicNameValuePair("fecha",fecha));
            params.add(new BasicNameValuePair("hora",hora));
		    /*Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir
		    en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor
		    envien los datos que hemos añadido*/
            httppost.setEntity(new UrlEncodedFormEntity(params));

            /*Finalmente ejecutamos enviando la info al server*/
            HttpResponse resp = httpclient.execute(httppost);
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/

            String text = EntityUtils.toString(ent);

            return text;

        }
        catch(Exception e) { return e.toString();}

    }

    public String postPersonal(String nombre_personal, String apellido_m, String apellido_p,
                               String contrasena, String lada, String telefono,String sede,
                               String cupo, String horario, String pusto,String tipo){

        try {

            String posturl = "https://rasped.herokuapp.com/content/insertar_personal.php";
            HttpClient httpclient = new DefaultHttpClient();
            /*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
            HttpPost httppost = new HttpPost(posturl);
            /*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
            //AÑADIR PARAMETROS
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("nombre_personal",nombre_personal));
            params.add(new BasicNameValuePair("apellido_m",apellido_m));
            params.add(new BasicNameValuePair("apellido_p",apellido_p));
            params.add(new BasicNameValuePair("contrasena",contrasena));
            params.add(new BasicNameValuePair("lada",lada));
            params.add(new BasicNameValuePair("telefono",telefono));
            params.add(new BasicNameValuePair("sede",sede));
            params.add(new BasicNameValuePair("cupo",cupo));
            params.add(new BasicNameValuePair("horario",horario));
            params.add(new BasicNameValuePair("puesto",pusto));
            params.add(new BasicNameValuePair("tipo",tipo));

		    /*Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir
		    en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor
		    envien los datos que hemos añadido*/
            httppost.setEntity(new UrlEncodedFormEntity(params));

            /*Finalmente ejecutamos enviando la info al server*/
            HttpResponse resp = httpclient.execute(httppost);
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/

            String text = EntityUtils.toString(ent);

            return text;

        }
        catch(Exception e) { return e.toString();}

    }

    public String postEditarPersonal(String nombre_personal, String apellido_m, String apellido_p,
                               String contrasena, String lada, String telefono,String sede,
                               String cupo){

        try {

            String posturl = "https://rasped.herokuapp.com/content/editar_usuari.php";
            HttpClient httpclient = new DefaultHttpClient();
            /*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
            HttpPost httppost = new HttpPost(posturl);
            /*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
            //AÑADIR PARAMETROS
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("nombre_personal",nombre_personal));
            params.add(new BasicNameValuePair("apellido_m",apellido_m));
            params.add(new BasicNameValuePair("apellido_p",apellido_p));
            params.add(new BasicNameValuePair("contrasena",contrasena));
            params.add(new BasicNameValuePair("lada",lada));
            params.add(new BasicNameValuePair("telefono",telefono));
            params.add(new BasicNameValuePair("sede",sede));
            params.add(new BasicNameValuePair("cupo",cupo));
            params.add(new BasicNameValuePair("tipo_usuario","4"));

		    /*Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir
		    en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor
		    envien los datos que hemos añadido*/
            httppost.setEntity(new UrlEncodedFormEntity(params));

            /*Finalmente ejecutamos enviando la info al server*/
            HttpResponse resp = httpclient.execute(httppost);
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/

            String text = EntityUtils.toString(ent);

            return text;

        }
        catch(Exception e) { return e.toString();}

    }

    public String postContrasena(String telefono, String mensContrasena){

        try {

            String posturl = "http://dexub.com/smsmexico/index.php";
            HttpClient httpclient = new DefaultHttpClient();
            /*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
            HttpPost httppost = new HttpPost(posturl);
            /*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
            //AÑADIR PARAMETROS
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("country",""));
            params.add(new BasicNameValuePair("tel",telefono));
            params.add(new BasicNameValuePair("msg",mensContrasena));
            params.add(new BasicNameValuePair("enviar",""));

		    /*Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir
		    en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor
		    envien los datos que hemos añadido*/
            httppost.setEntity(new UrlEncodedFormEntity(params));

            /*Finalmente ejecutamos enviando la info al server*/
            HttpResponse resp = httpclient.execute(httppost);
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/

            String text = EntityUtils.toString(ent);

            return text;

        }
        catch(Exception e) { return e.toString();}

    }

    public String agregarPuesto(String puesto){

        try {

            String posturl = "https://rasped.herokuapp.com/content/insertar_puesto.php";
            HttpClient httpclient = new DefaultHttpClient();
            /*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
            HttpPost httppost = new HttpPost(posturl);
            /*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
            //AÑADIR PARAMETROS
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("puesto",puesto));

		    /*Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir
		    en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor
		    envien los datos que hemos añadido*/
            httppost.setEntity(new UrlEncodedFormEntity(params));

            /*Finalmente ejecutamos enviando la info al server*/
            HttpResponse resp = httpclient.execute(httppost);
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/

            String text = EntityUtils.toString(ent);

            return text;

        }
        catch(Exception e) { return e.toString();}

    }

    public String agregarHorario(String hr_nombre,String hr_entrada,String hr_comida_i,String hr_comida_f,String hr_salida,String tolerancia){

        try {

            String posturl = "https://rasped.herokuapp.com/content/insertar_horario.php";
            HttpClient httpclient = new DefaultHttpClient();
            /*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
            HttpPost httppost = new HttpPost(posturl);
            /*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
            //AÑADIR PARAMETROS
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("hr_nombre",hr_nombre));
            params.add(new BasicNameValuePair("hr_entrada",hr_entrada));
            params.add(new BasicNameValuePair("hr_comida_i",hr_comida_i));
            params.add(new BasicNameValuePair("hr_comida_f",hr_comida_f));
            params.add(new BasicNameValuePair("hr_salida",hr_salida));
            params.add(new BasicNameValuePair("tolerancia",tolerancia));

		    /*Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir
		    en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor
		    envien los datos que hemos añadido*/
            httppost.setEntity(new UrlEncodedFormEntity(params));

            /*Finalmente ejecutamos enviando la info al server*/
            HttpResponse resp = httpclient.execute(httppost);
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/

            String text = EntityUtils.toString(ent);

            return text;

        }
        catch(Exception e) { return e.toString();}

    }

    public String editarContrasena(String idPersonal, String nvaContrasena){

        try {

            String posturl = "https://rasped.herokuapp.com/content/editar_contrasena.php";
            HttpClient httpclient = new DefaultHttpClient();
            /*Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http*/
            HttpPost httppost = new HttpPost(posturl);
            /*El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada*/
            //AÑADIR PARAMETROS
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("id_personal",idPersonal));
            params.add(new BasicNameValuePair("contrasena",nvaContrasena));

		    /*Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir
		    en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor
		    envien los datos que hemos añadido*/
            httppost.setEntity(new UrlEncodedFormEntity(params));

            /*Finalmente ejecutamos enviando la info al server*/
            HttpResponse resp = httpclient.execute(httppost);
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/

            String text = EntityUtils.toString(ent);

            return text;

        }
        catch(Exception e) { return e.toString();}

    }


}
