package juego.esferas;

import juego.criaturas.Capturable;
import juego.enums.TipoElemento;

// Doubles capture rate against a specific element type
public class EsferaTipo extends PokeEsfera {

    private final TipoElemento tipoObjetivo;

    public EsferaTipo(TipoElemento tipoObjetivo) {
        super("Esfera Tipo", 1.5);
        this.tipoObjetivo = tipoObjetivo;
    }

    @Override
    public double calcularTasaCaptura(Capturable c) {
        double base = super.calcularTasaCaptura(c);
        if (c.getTipo() == tipoObjetivo) {
            base *= 2.0;
        }
        return Math.min(100.0, base);
    }
}
