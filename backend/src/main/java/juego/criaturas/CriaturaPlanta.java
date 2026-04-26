package juego.criaturas;

import juego.enums.TipoElemento;
import juego.movimientos.Movimiento;

// Plant-type creatures passively regenerate HP through photosynthesis every 3 turns
public abstract class CriaturaPlanta extends Criatura {

    protected int fotosintesis;

    protected CriaturaPlanta(String nombre, int nivel, int baseHp, int baseAtaque,
                              int baseDefensa, int baseVelocidad, double tasaCaptura,
                              Movimiento[] movimientos) {
        super(nombre, nivel, baseHp, baseAtaque, baseDefensa, baseVelocidad,
              TipoElemento.PLANTA, tasaCaptura, movimientos);
        this.fotosintesis = 0;
    }

    // Heals 10% of max HP every 3 turns
    @Override
    public void habilidadEspecial() {
        fotosintesis++;
        if (fotosintesis >= 3) {
            curar((int)(hpMax * 0.1));
            fotosintesis = 0;
        }
    }
}
