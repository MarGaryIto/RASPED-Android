package com.example.adminprospera.rasped_tool;

public class ObjetosHorarios {
    private int id_horario;
    private String hr_nombre;

    public ObjetosHorarios(int id_horario, String hr_nombre) {
        super();
        this.id_horario = id_horario;
        this.hr_nombre = hr_nombre;
    }

    @Override
    public String toString() {
        return hr_nombre;
    }
    public int getId() {
        return id_horario;
    }
}
