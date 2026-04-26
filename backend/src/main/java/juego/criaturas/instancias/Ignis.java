package juego.criaturas.instancias;

import juego.criaturas.CriaturaFuego;
import juego.movimientos.Movimiento;

// Fire starter. Learns Llamarada at level 10+.
public class Ignis extends CriaturaFuego {

    public Ignis(int nivel) {
        super("Ignis", nivel, 39, 52, 43, 45, 0.3, buildMoves(nivel));
    }

    private static Movimiento[] buildMoves(int nivel) {
        Movimiento[] movs = new Movimiento[4];
        movs[0] = Movimiento.ascua();
        movs[1] = Movimiento.placaje();
        movs[2] = nivel >= 10 ? Movimiento.llamarada() : null;
        movs[3] = null;
        return movs;
    }
}
