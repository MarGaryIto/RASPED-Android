package com.example.adminprospera.rasped_tool;

public class ModelAdministrador {
    private String id_personal;
    private String nombrePersonal;
    private String Cupo;
    private String fecha;
    private String hora;
    private int codigo;



    public String getNombrePersonal() {
        return nombrePersonal;
    }

    public void setNombrePersonal(String nombrePersonal) {
        this.nombrePersonal = nombrePersonal;
    }

    public String getCupo() {
        return Cupo;
    }

    public void setCupo(String cupo) {
        Cupo = cupo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }


    public String getId_personal() {
        return id_personal;
    }

    public void setId_personal(String id_personal) {
        this.id_personal = id_personal;
    }
}
