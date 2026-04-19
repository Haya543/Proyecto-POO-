package ProyectoPOO.Entrenadores_Objetos_Batalla;

import ProyectoPOO.Ciraturas_Interfaces.Criatura;

public abstract class Entrenador {
    protected String nombre;
    protected Criatura[] equipo;

    public Criatura elegirCriatura(){}
    public boolean tieneCriaturasVivas(){}

    public String getNombre() {
        return nombre;
    }

}
