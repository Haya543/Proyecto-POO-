package juego.criaturas.instancias;

import juego.criaturas.CriaturaAgua;
import juego.movimientos.Movimiento;

// Water starter. High defense, learns Hidrobomba at level 10+.
public class Aqua extends CriaturaAgua {

    public Aqua(int nivel) {
        super("Aqua", nivel, 44, 48, 65, 43, 0.3, buildMoves(nivel));
    }

    private static Movimiento[] buildMoves(int nivel) {
        Movimiento[] movs = new Movimiento[4];
        movs[0] = Movimiento.pistoladeagua();
        movs[1] = Movimiento.placaje();
        movs[2] = nivel >= 10 ? Movimiento.hidrobomba() : null;
        movs[3] = null;
        return movs;
    }
}
