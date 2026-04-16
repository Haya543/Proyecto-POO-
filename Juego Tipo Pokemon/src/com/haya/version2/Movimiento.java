package com.haya.version2;

public class Movimiento {
    private String nombre;
    Tipo tipo;
    private int poder, precision, ppMaximos, ppActuales;
    private boolean esFisico;


    public Movimiento(String nombre, Tipo tipo, int poder, int precision, int ppMaximos, int ppActuales, boolean esFisico) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.poder = poder;
        this.precision = precision;
        this.ppMaximos = ppMaximos;
        this.ppActuales = ppActuales;
        this.esFisico = esFisico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public int getPoder() {
        return poder;
    }

    public void setPoder(int poder) {
        this.poder = poder;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getPpMaximos() {
        return ppMaximos;
    }

    public void setPpMaximos(int ppMaximos) {
        this.ppMaximos = ppMaximos;
    }

    public int getPpActuales() {
        return ppActuales;
    }

    public void setPpActuales(int ppActuales) {
        this.ppActuales = ppActuales;
    }

    public boolean isEsFisico() {
        return esFisico;
    }

    public void setEsFisico(boolean esFisico) {
        this.esFisico = esFisico;
    }

    public boolean usarPP(){
        if(this.ppActuales > 0){
            this.ppActuales--;
            return true;
        }
        else {
            return false;
        }
    }
}
