package juego.entrenadores;

import juego.criaturas.Criatura;
import java.util.ArrayList;
import java.util.List;

// Base class for all trainers (both player and NPCs)
public abstract class Entrenador {

    protected String nombre;
    protected List<Criatura> equipo;
    protected int indiceCriaturaActiva;

    protected Entrenador(String nombre) {
        this.nombre = nombre;
        this.equipo = new ArrayList<>();
        this.indiceCriaturaActiva = 0;
    }

    public Criatura getCriaturaActiva() {
        if (equipo.isEmpty()) return null;
        return equipo.get(indiceCriaturaActiva);
    }

    public boolean tieneCriaturasVivas() {
        for (Criatura c : equipo) {
            if (c.estaViva()) return true;
        }
        return false;
    }

    // Returns the first living creature; subclasses can override for smarter selection
    public Criatura elegirCriatura() {
        for (Criatura c : equipo) {
            if (c.estaViva()) return c;
        }
        return null;
    }

    // Switches active creature only if the target index is alive
    public void cambiarCriatura(int indice) {
        if (indice >= 0 && indice < equipo.size() && equipo.get(indice).estaViva()) {
            indiceCriaturaActiva = indice;
        }
    }

    // Advances to the next living creature when the active one faints
    public void avanzarSiguienteViva() {
        for (int i = 0; i < equipo.size(); i++) {
            if (equipo.get(i).estaViva()) {
                indiceCriaturaActiva = i;
                return;
            }
        }
    }

    public void agregarCriatura(Criatura c) {
        if (equipo.size() < 6) equipo.add(c);
    }

    public String getNombre() { return nombre; }
    public List<Criatura> getEquipo() { return equipo; }
    public int getIndiceCriaturaActiva() { return indiceCriaturaActiva; }
}
