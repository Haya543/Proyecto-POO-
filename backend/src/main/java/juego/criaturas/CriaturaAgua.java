package juego.criaturas;

import juego.enums.TipoElemento;
import juego.movimientos.Movimiento;

// Water-type creatures build moisture stacks to reduce incoming damage
public abstract class CriaturaAgua extends Criatura {

    protected int humedad;

    protected CriaturaAgua(String nombre, int nivel, int baseHp, int baseAtaque,
                            int baseDefensa, int baseVelocidad, double tasaCaptura,
                            Movimiento[] movimientos) {
        super(nombre, nivel, baseHp, baseAtaque, baseDefensa, baseVelocidad,
              TipoElemento.AGUA, tasaCaptura, movimientos);
        this.humedad = 0;
    }

    // At 2 humidity stacks the creature absorbs some of the next hit
    @Override
    public void habilidadEspecial() {
        humedad++;
        if (humedad >= 2) {
            bonusDefensa = 0.9;
            humedad = 0;
        }
    }
}
