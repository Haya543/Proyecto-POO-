package juego.criaturas.instancias;

import juego.criaturas.CriaturaAgua;
import juego.enums.TipoElemento;
import juego.movimientos.Movimiento;

// Wild ice/water creature, zone 2. Uses HIELO type moves despite being a water class.
public class Glacius extends CriaturaAgua {

    public Glacius(int nivel) {
        super("Glacius", nivel, 48, 44, 55, 40, 0.35, buildMoves(nivel));
        // Override the default AGUA type assigned by CriaturaAgua
        this.tipo = TipoElemento.HIELO;
    }

    private static Movimiento[] buildMoves(int nivel) {
        Movimiento[] movs = new Movimiento[4];
        movs[0] = Movimiento.ventisca();
        movs[1] = Movimiento.pistoladeagua();
        movs[2] = nivel >= 12 ? Movimiento.hidrobomba() : null;
        movs[3] = null;
        return movs;
    }
}
