package ProyectoPOO.Entrenadores_Objetos_Batalla;

import ProyectoPOO.Ciraturas_Interfaces.Criatura;

public abstract class Objeto {
    private String nombre, descripcion;
    private int cantidad;

    public void usar(Criatura objetivo){}

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }
}
