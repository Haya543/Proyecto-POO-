package juego.criaturas.instancias;

import juego.criaturas.CriaturaPlanta;
import juego.movimientos.Movimiento;

// Plant starter. Balanced stats, learns Drenaje at level 10+.
public class Herba extends CriaturaPlanta {

    public Herba(int nivel) {
        super("Herba", nivel, 45, 49, 49, 45, 0.3, buildMoves(nivel));
    }

    private static Movimiento[] buildMoves(int nivel) {
        Movimiento[] movs = new Movimiento[4];
        movs[0] = Movimiento.hojaafilada();
        movs[1] = Movimiento.placaje();
        movs[2] = nivel >= 10 ? Movimiento.drenaje() : null;
        movs[3] = null;
        return movs;
    }
}
