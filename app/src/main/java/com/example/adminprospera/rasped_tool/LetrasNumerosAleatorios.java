package com.example.adminprospera.rasped_tool;


import java.util.Random;

public class LetrasNumerosAleatorios {

    public String codigoAleatorio(int dimension){
        String codigo = "";
        Random random = new Random();
        for (int i = 0;i<dimension;i++){
            int numRandom = random.nextInt(90);
            if (numRandom>=65){
                codigo += (char) numRandom;
            }else{
                i--;
            }
        }
        return codigo;
    }

}
