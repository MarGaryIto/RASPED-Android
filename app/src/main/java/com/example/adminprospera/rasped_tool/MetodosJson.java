package com.example.adminprospera.rasped_tool;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//esta clase contienen metodos publicos que son utilizados para transacciondes de JSON para
//cualquier Activity que lo requiera
class MetodosJson {

    //metodo que devuelve un JSON a traves de una url como variable
    //para usarse, se requiere un hilo
    String obtenerJSON(URL url){

        //Inicializacion de variables requeridas
        String linea;
        int respuesta;
        StringBuilder resul;

        //el tratado de url se codifica forzosamente dentro de un try-catch
        try{

            //conexion con la url y obtencion de un resultado
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            respuesta=connection.getResponseCode();
            resul=new StringBuilder();

            //si el resultado fue correcto...
            if (respuesta==HttpURLConnection.HTTP_OK){

                //crear un lector del resultado
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                //construccion de la cadena linea por linea
                while ((linea = reader.readLine())!=null){
                    resul.append(linea);
                }
            }
        }catch (Exception e){
            return e.toString();
        }

        //devolucion del resultado
        return resul.toString();
    }
}
