package juego.criaturas.instancias;

import juego.criaturas.CriaturaFuego;
import juego.movimientos.Movimiento;

// Wild fire creature, zone 1. High attack, low defense.
public class Fumeo extends CriaturaFuego {

    public Fumeo(int nivel) {
        super("Fumeo", nivel, 36, 50, 30, 50, 0.40, buildMoves(nivel));
    }

    private static Movimiento[] buildMoves(int nivel) {
        Movimiento[] movs = new Movimiento[4];
        movs[0] = Movimiento.ascua();
        movs[1] = Movimiento.placaje();
        movs[2] = null;
        movs[3] = null;
        return movs;
    }
}
