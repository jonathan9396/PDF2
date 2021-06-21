package com.example.pdf2.Inconsistencia;

public class Inconsistencia {

    private String llave;
    private String datos;

    public String getLlave() {
        return llave;
    }

    public void setLlave(String llave) {
        this.llave = llave;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public Inconsistencia(String llave, String datos) {
        this.llave = llave;
        this.datos = datos;
    }
}
