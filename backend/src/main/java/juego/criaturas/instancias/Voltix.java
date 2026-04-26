package juego.criaturas.instancias;

import juego.criaturas.CriaturaElectrico;
import juego.movimientos.Movimiento;

// Wild electric creature, zone 1. Can charge to boost damage.
public class Voltix extends CriaturaElectrico {

    public Voltix(int nivel) {
        super("Voltix", nivel, 35, 45, 30, 60, 0.45, buildMoves(nivel));
    }

    private static Movimiento[] buildMoves(int nivel) {
        Movimiento[] movs = new Movimiento[4];
        movs[0] = Movimiento.impactrueno();
        movs[1] = Movimiento.placaje();
        movs[2] = nivel >= 8 ? Movimiento.rayo() : null;
        movs[3] = null;
        return movs;
    }
}
