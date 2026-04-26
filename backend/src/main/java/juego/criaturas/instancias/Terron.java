package juego.criaturas.instancias;

import juego.criaturas.CriaturaNormal;
import juego.enums.TipoElemento;
import juego.movimientos.Movimiento;

// Wild ground creature, zone 2. Bulky and hard-hitting.
public class Terron extends CriaturaNormal {

    public Terron(int nivel) {
        super("Terron", nivel, 55, 60, 50, 35, 0.40, buildMoves(nivel));
        // Override to TIERRA type; CriaturaNormal defaults to NORMAL
        this.tipo = TipoElemento.TIERRA;
    }

    private static Movimiento[] buildMoves(int nivel) {
        Movimiento[] movs = new Movimiento[4];
        movs[0] = Movimiento.terremoto();
        movs[1] = Movimiento.placaje();
        movs[2] = null;
        movs[3] = null;
        return movs;
    }
}
