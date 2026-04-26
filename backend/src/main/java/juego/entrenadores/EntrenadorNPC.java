package juego.entrenadores;

import juego.criaturas.Criatura;
import juego.criaturas.instancias.*;
import juego.enums.NivelDificultad;
import juego.enums.TipoElemento;
import juego.movimientos.Movimiento;

// CPU-controlled trainer. Uses type-advantage AI at MEDIO difficulty and above.
public class EntrenadorNPC extends Entrenador {

    private final String dialogo;
    private final NivelDificultad dificultad;
    private final int recompensa;
    private boolean derrotado;

    public EntrenadorNPC(String nombre, String dialogo, NivelDificultad dificultad,
                         int recompensa) {
        super(nombre);
        this.dialogo = dialogo;
        this.dificultad = dificultad;
        this.recompensa = recompensa;
        this.derrotado = false;
    }

    // Picks the best available move: on FACIL selects randomly; otherwise picks by type advantage
    public Movimiento elegirMovimiento(Criatura propia, Criatura rival) {
        Movimiento[] movs = propia.getMovimientos();

        if (!dificultad.getIAActiva()) {
            // Random selection among moves with PP remaining
            for (int attempts = 0; attempts < 10; attempts++) {
                int idx = (int)(Math.random() * movs.length);
                if (movs[idx] != null && movs[idx].tienePP()) return movs[idx];
            }
        }

        // Score each move by expected damage considering type effectiveness
        Movimiento mejor = null;
        double mejorPuntaje = -1;

        for (Movimiento m : movs) {
            if (m == null || !m.tienePP()) continue;
            double multi = rival.getTipo().getMultiplicador(m.getTipo());
            double puntaje = m.getPoder() * multi;
            if (puntaje > mejorPuntaje) {
                mejorPuntaje = puntaje;
                mejor = m;
            }
        }

        // Fall back to the first available move if scoring found nothing
        if (mejor == null) {
            for (Movimiento m : movs) {
                if (m != null && m.tienePP()) return m;
            }
        }
        return mejor;
    }

    public String saludar() { return dialogo; }
    public int getRecompensa() { return recompensa; }
    public boolean isDerrotado() { return derrotado; }
    public void setDerrotado(boolean d) { this.derrotado = d; }
    public NivelDificultad getDificultad() { return dificultad; }

    // Factory methods for the 4 main story NPCs

    // Uses the counter-type starter against whatever the player chose
    public static EntrenadorNPC rival(TipoElemento starterTipo) {
        EntrenadorNPC npc = new EntrenadorNPC(
            "Chato",
            "Creias que podias ganar? Yo entrene mas que tu!",
            NivelDificultad.MEDIO,
            200
        );
        if (starterTipo == TipoElemento.FUEGO) {
            npc.agregarCriatura(new Aqua(6));
        } else if (starterTipo == TipoElemento.AGUA) {
            npc.agregarCriatura(new Herba(6));
        } else {
            npc.agregarCriatura(new Ignis(6));
        }
        return npc;
    }

    public static EntrenadorNPC liderMarina() {
        EntrenadorNPC npc = new EntrenadorNPC(
            "Marina",
            "El agua es poder. Preparate para hundirte.",
            NivelDificultad.MEDIO,
            500
        );
        npc.agregarCriatura(new Aqua(12));
        npc.agregarCriatura(new Glacius(10));
        return npc;
    }

    public static EntrenadorNPC liderBrenno() {
        EntrenadorNPC npc = new EntrenadorNPC(
            "Brenno",
            "El fuego consume todo. Incluido tu equipo.",
            NivelDificultad.DIFICIL,
            800
        );
        npc.agregarCriatura(new Ignis(16));
        npc.agregarCriatura(new Fumeo(14));
        return npc;
    }

    public static EntrenadorNPC campeonRex() {
        EntrenadorNPC npc = new EntrenadorNPC(
            "Rex",
            "Nadie ha llegado tan lejos. Pero aqui termina tu camino.",
            NivelDificultad.EXPERTO,
            2000
        );
        npc.agregarCriatura(new Gorgon(20));
        npc.agregarCriatura(new Voltix(18));
        npc.agregarCriatura(new Terron(17));
        return npc;
    }
}
