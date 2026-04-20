package ProyectoPOO.PokeEsferas;

import ProyectoPOO.Ciraturas_Interfaces.Capturable;

public class EsferaHP extends PokeEsfera {

    public EsferaHP() {
        super("Esfera HP", 1.0);
    }

    @Override
    public double calcularTasaCaptura(Capturable c) {
        double porcentajeHp = (double) c.getHpActual() / c.getHpMax();
        double tasa = super.calcularTasaCaptura(c);

        if (porcentajeHp <= 0.25) {
            tasa *= 3.0;
        } else if (porcentajeHp <= 0.50) {
            tasa *= 2.0;
        }

        return Math.min(tasa, 100.0);
    }
}
