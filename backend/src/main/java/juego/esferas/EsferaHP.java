package juego.esferas;

import juego.criaturas.Capturable;

// Capture rate scales with how low the target's HP is
public class EsferaHP extends PokeEsfera {

    public EsferaHP() {
        super("Esfera HP", 1.0);
    }

    @Override
    public double calcularTasaCaptura(Capturable c) {
        double base = super.calcularTasaCaptura(c);
        double porcentajeHp = (double) c.getHpActual() / c.getHpMax();

        if (porcentajeHp <= 0.25) {
            base *= 3.0;
        } else if (porcentajeHp <= 0.50) {
            base *= 2.0;
        }
        return Math.min(100.0, base);
    }
}
