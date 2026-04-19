package ProyectoPOO.Entrenadores_Objetos_Batalla;

import ProyectoPOO.Ciraturas_Interfaces.Criatura;
import ProyectoPOO.Enums_LogicaCentral.Movimiento;
import ProyectoPOO.Enums_LogicaCentral.NivelDificultad;

public class EntrenadorNPC extends Entrenador{
    private String dialogo;
    private NivelDificultad dificultad;
    private int recompensa;

    public Movimiento elegirMovimiento(Criatura propia, Criatura rival){}
    public Criatura elegirCriatura(){}
    public String saludar(){}
}
