package com.example.holahola;

public class ParameterDataModel {
    private int vida;
    private int movimientos;
    private String nombre;

    public ParameterDataModel(int vida, int movimientos, String nombre) {
        this.vida = vida;
        this.movimientos = movimientos;
        this.nombre = nombre;
    }
    public int getVida() {
        return vida;
    }
    public void setVida(int vida) {
        this.vida = vida;
    }
    public int getMovimientos() {
        return movimientos;
    }
    public void setMovimientos(int movimientos) {
        this.movimientos = movimientos;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}