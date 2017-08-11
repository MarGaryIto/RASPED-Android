package com.example.adminprospera.rasped_tool;

public class ObjetosPuestos {
    private int id_puesto;
    private String nombre_puesto;

    public ObjetosPuestos(int id_puesto, String nombre_puesto) {
        super();
        this.id_puesto = id_puesto;
        this.nombre_puesto = nombre_puesto;
    }

    @Override
    public String toString() {
        return nombre_puesto;
    }
    public int getId() {
        return id_puesto;
    }
}
