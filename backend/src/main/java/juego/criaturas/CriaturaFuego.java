package juego.criaturas;

import juego.enums.TipoElemento;
import juego.movimientos.Movimiento;

// Fire-type creatures build heat stacks each turn to power up their next fire attack
public abstract class CriaturaFuego extends Criatura {

    // Tracks how many turns the creature has been attacking without the bonus triggering
    protected int calor;

    protected CriaturaFuego(String nombre, int nivel, int baseHp, int baseAtaque,
                             int baseDefensa, int baseVelocidad, double tasaCaptura,
                             Movimiento[] movimientos) {
        super(nombre, nivel, baseHp, baseAtaque, baseDefensa, baseVelocidad,
              TipoElemento.FUEGO, tasaCaptura, movimientos);
        this.calor = 0;
    }

    // Accumulates heat each turn; at 3 stacks the next fire move deals +20% damage
    @Override
    public void habilidadEspecial() {
        calor++;
        if (calor >= 3) {
            bonusDanio = 1.2;
            calor = 0;
        }
    }
}
