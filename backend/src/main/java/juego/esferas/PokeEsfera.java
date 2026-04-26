package juego.esferas;

import juego.criaturas.Capturable;

// Base class for all capture spheres. Subclasses differ only in tasaBase (and special overrides).
public abstract class PokeEsfera {

    private final String nombre;
    protected final double tasaBase;

    protected PokeEsfera(String nombre, double tasaBase) {
        this.nombre = nombre;
        this.tasaBase = tasaBase;
    }

    // Returns a 0-100 capture percentage based on remaining HP and the creature's base rate
    public double calcularTasaCaptura(Capturable c) {
        double hpFactor = (3.0 * c.getHpMax() - 2.0 * c.getHpActual()) / (3.0 * c.getHpMax());
        double raw = hpFactor * c.detTasaCaptura() * tasaBase * 100.0;
        return Math.min(100.0, raw);
    }

    // Returns true if the capture succeeds (random roll vs calculated percentage)
    public boolean lanzar(Capturable c) {
        double chance = calcularTasaCaptura(c);
        return Math.random() * 100.0 < chance;
    }

    public String getNombre() { return nombre; }
}
