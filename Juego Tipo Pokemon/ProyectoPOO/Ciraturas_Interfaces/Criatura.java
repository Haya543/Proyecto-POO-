package ProyectoPOO.Ciraturas_Interfaces;

import ProyectoPOO.Enums_LogicaCentral.Movimiento;
import ProyectoPOO.Enums_LogicaCentral.TipoElemento;

public abstract class Criatura {
    private String nombre;
    private int nivel, hp, hpMax, ataque, defensa, velocidad, expTotal;
    private TipoElemento tipo;
    private Movimiento[] movimientos;

    public int atacar(Movimiento mov){}
    public void recibirDanio(int danio){}
    public boolean estaViva(){}
    public void ganarExp(int exp){}
    public void subirNivel(){}
    public double getTasaCaptura(){}
    public int[] getStats(){}

}
