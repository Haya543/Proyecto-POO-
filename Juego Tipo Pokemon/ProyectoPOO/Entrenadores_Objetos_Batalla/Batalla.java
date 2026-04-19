package ProyectoPOO.Entrenadores_Objetos_Batalla;

import ProyectoPOO.Ciraturas_Interfaces.Criatura;
import ProyectoPOO.Enums_LogicaCentral.Movimiento;
import ProyectoPOO.Enums_LogicaCentral.NivelDificultad;
import ProyectoPOO.Enums_LogicaCentral.TipoElemento;

public class Batalla extends Entrenador{
    private Entrenador atacante;
    private Entrenador defensor;
    private NivelDificultad dificultad;
    private int turnoActual;
    private boolean esActiva;

    public void iniciar(){}
    public void ejecutarTurno(Movimiento mov){}
    public int calcularDanio(Movimiento m, Criatura at, Criatura def){}
    public double calcularVentajaTipo(TipoElemento ar, TipoElemento def){}
    public boolean haTerminado(){}
    public void distribuirExp(){}
}
