package ProyectoPOO.PokeEsferas;

import ProyectoPOO.Ciraturas_Interfaces.Capturable;

public abstract class PokeEsfera {
    private String nombre;
    private double tasaBase;

    public double calcularTasaCaptura(Capturable C){}
    public boolean lanzar(Capturable c){}

    public String getNombre() {
        return nombre;
    }
}
