package juego.criaturas;

import juego.enums.TipoElemento;
import juego.movimientos.Movimiento;

// Electric-type creatures can charge up to boost their next attack
public abstract class CriaturaElectrico extends Criatura {

    // Set to true when the creature builds a charge (e.g. after taking 0 damage)
    protected boolean cargado;

    protected CriaturaElectrico(String nombre, int nivel, int baseHp, int baseAtaque,
                                 int baseDefensa, int baseVelocidad, double tasaCaptura,
                                 Movimiento[] movimientos) {
        super(nombre, nivel, baseHp, baseAtaque, baseDefensa, baseVelocidad,
              TipoElemento.ELECTRICO, tasaCaptura, movimientos);
        this.cargado = false;
    }

    // Charges every other turn; a charged attack deals +15% damage then resets
    @Override
    public void habilidadEspecial() {
        if (!cargado) {
            cargado = true;
        } else {
            bonusDanio = 1.15;
            cargado = false;
        }
    }
}
