package juego.criaturas;

import juego.enums.TipoElemento;
import juego.movimientos.Movimiento;

// Base class for all creatures. Implements both leveling and capture interfaces.
public abstract class Criatura implements Capturable, Nivelable {

    protected String nombre;
    protected int nivel;
    protected int hp;
    protected int hpMax;
    protected int ataque;
    protected int defensa;
    protected int velocidad;
    protected int expTotal;
    protected int expSiguienteNivel;
    protected TipoElemento tipo;
    protected Movimiento[] movimientos;
    protected double tasaCaptura;

    // Base stat values before level scaling, stored so subirNivel can compute growth
    protected int baseHp;
    protected int baseAtaque;
    protected int baseDefensa;
    protected int baseVelocidad;

    protected Criatura(String nombre, int nivel, int baseHp, int baseAtaque,
                       int baseDefensa, int baseVelocidad, TipoElemento tipo,
                       double tasaCaptura, Movimiento[] movimientos) {
        this.nombre = nombre;
        this.nivel = nivel;
        this.tipo = tipo;
        this.tasaCaptura = tasaCaptura;
        this.movimientos = movimientos;

        this.baseHp = baseHp;
        this.baseAtaque = baseAtaque;
        this.baseDefensa = baseDefensa;
        this.baseVelocidad = baseVelocidad;

        // Scale stats based on level so higher-level encounters feel stronger
        this.hpMax = baseHp + (nivel - 1) * 5;
        this.ataque = baseAtaque + (nivel - 1) * 3;
        this.defensa = baseDefensa + (nivel - 1) * 2;
        this.velocidad = baseVelocidad + (nivel - 1) * 2;
        this.hp = this.hpMax;

        this.expTotal = 0;
        this.expSiguienteNivel = nivel * nivel * 5;
    }

    // Spends the move's PP and returns the raw power. Batalla applies modifiers.
    public int atacar(Movimiento mov) {
        if (!mov.usar()) return 0;
        return mov.getPoder();
    }

    public void recibirDanio(int danio) {
        hp = Math.max(0, hp - danio);
    }

    public boolean estaViva() {
        return hp > 0;
    }

    // Adds experience and triggers level-up(s) if threshold is crossed
    public void ganarExp(int exp) {
        expTotal += exp;
        while (expTotal >= expSiguienteNivel) {
            subirNivel();
        }
    }

    // Raises stats and heals to full HP on level up
    public void subirNivel() {
        expTotal -= expSiguienteNivel;
        nivel++;
        expSiguienteNivel = nivel * nivel * 5;
        hpMax += 5;
        ataque += 3;
        defensa += 2;
        velocidad += 2;
        hp = hpMax;
    }

    // Higher capture probability when HP is lower. Capped at the base rate.
    @Override
    public double detTasaCaptura() {
        double hpRatio = (double) hp / hpMax;
        return tasaCaptura * (1.0 - hpRatio * 0.7);
    }

    public void curar(int cantidad) {
        hp = Math.min(hpMax, hp + cantidad);
    }

    // Restores HP to max and refreshes all move PP (used at healing center)
    public void curarCompleto() {
        hp = hpMax;
        for (Movimiento m : movimientos) {
            if (m != null) m.restaurarPP();
        }
    }

    // Each concrete type subclass defines its passive ability here
    public abstract void habilidadEspecial();

    @Override public int getNivel() { return nivel; }
    @Override public int getHpActual() { return hp; }
    @Override public int getHpMax() { return hpMax; }
    @Override public TipoElemento getTipo() { return tipo; }

    public String getNombre() { return nombre; }
    public int getAtaque() { return ataque; }
    public int getDefensa() { return defensa; }
    public int getVelocidad() { return velocidad; }
    public Movimiento[] getMovimientos() { return movimientos; }
    public int getExpTotal() { return expTotal; }
    public int getExpSiguienteNivel() { return expSiguienteNivel; }
    public double getTasaCaptura() { return tasaCaptura; }

    // Bonus multiplier injected by the type subclass when its passive is active
    protected double bonusDanio = 1.0;
    protected double bonusDefensa = 1.0;

    public double getBonusDanio() { return bonusDanio; }
    public double getBonusDefensa() { return bonusDefensa; }
    public void resetBonuses() {
        bonusDanio = 1.0;
        bonusDefensa = 1.0;
    }
}
