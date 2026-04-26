package juego.criaturas;

import juego.enums.TipoElemento;
import juego.movimientos.Movimiento;

// Normal-type creatures adapt to their opponent over time, reducing incoming damage
public abstract class CriaturaNormal extends Criatura {

    private int turnosEnBatalla;

    protected CriaturaNormal(String nombre, int nivel, int baseHp, int baseAtaque,
                              int baseDefensa, int baseVelocidad, double tasaCaptura,
                              Movimiento[] movimientos) {
        super(nombre, nivel, baseHp, baseAtaque, baseDefensa, baseVelocidad,
              TipoElemento.NORMAL, tasaCaptura, movimientos);
        this.turnosEnBatalla = 0;
    }

    // After 2 turns in battle the creature has adapted and takes 10% less damage
    @Override
    public void habilidadEspecial() {
        turnosEnBatalla++;
        if (turnosEnBatalla >= 2) {
            bonusDefensa = 0.9;
        }
    }
}
