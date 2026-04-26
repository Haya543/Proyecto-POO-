package juego.criaturas.instancias;

import juego.criaturas.CriaturaNormal;
import juego.movimientos.Movimiento;

// Final boss. Uncapturable (tasaCaptura 0.0). Uses moves from multiple types.
public class Gorgon extends CriaturaNormal {

    public Gorgon(int nivel) {
        super("Gorgon", nivel, 95, 80, 70, 55, 0.0, buildMoves());
    }

    private static Movimiento[] buildMoves() {
        return new Movimiento[]{
            Movimiento.trituracion(),
            Movimiento.terremoto(),
            Movimiento.llamarada(),
            Movimiento.hidrobomba()
        };
    }
}
