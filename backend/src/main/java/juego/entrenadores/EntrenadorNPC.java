package juego.entrenadores;

import juego.criaturas.Criatura;
import juego.criaturas.instancias.*;
import juego.enums.NivelDificultad;
import juego.enums.TipoElemento;
import juego.movimientos.Movimiento;
import java.util.ArrayList;
import java.util.List;

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
        List<Criatura> equipoJugador = new ArrayList<>();
        equipoJugador.add(crearCriaturaPorTipo(starterTipo, 6));
        return rivalNormal(equipoJugador);
    }

    // 3 tipos de rival para ajustar la composicion del equipo al nivel de dificultad
    public static EntrenadorNPC rivalFacil(List<Criatura> equipoJugador) {
        EntrenadorNPC npc = new EntrenadorNPC(
            "Chato (Facil)",
            "Hoy no vine en mi mejor dia... a ver que pasa.",
            NivelDificultad.FACIL,
            120
        );
        TipoElemento tipoJugador = tipoPrincipal(equipoJugador);
        TipoElemento debilContraJugador = tipoDebilA(tipoJugador);
        TipoElemento fuerteContraJugador = tipoFuerteContra(tipoJugador);

        // Facil: mas criaturas vulnerables al tipo principal del jugador
        npc.agregarCriatura(crearCriaturaPorTipo(debilContraJugador, 5));
        npc.agregarCriatura(crearCriaturaPorTipo(tipoJugador, 4));
        npc.agregarCriatura(crearCriaturaPorTipo(fuerteContraJugador, 4));
        return npc;
    }

    public static EntrenadorNPC rivalNormal(List<Criatura> equipoJugador) {
        EntrenadorNPC npc = new EntrenadorNPC(
            "Chato",
            "Creias que podias ganar? Yo entrene mas que tu!",
            NivelDificultad.MEDIO,
            200
        );
        TipoElemento tipoJugador = tipoPrincipal(equipoJugador);
        TipoElemento debilContraJugador = tipoDebilA(tipoJugador);
        TipoElemento fuerteContraJugador = tipoFuerteContra(tipoJugador);

        // Normal: equipo balanceado entre vulnerable, neutral y counter
        npc.agregarCriatura(crearCriaturaPorTipo(debilContraJugador, 6));
        npc.agregarCriatura(crearCriaturaPorTipo(tipoJugador, 6));
        npc.agregarCriatura(crearCriaturaPorTipo(fuerteContraJugador, 6));
        return npc;
    }

    public static EntrenadorNPC rivalDificil(List<Criatura> equipoJugador) {
        EntrenadorNPC npc = new EntrenadorNPC(
            "Chato (Dificil)",
            "Ya analice tu equipo. No tienes escapatoria.",
            NivelDificultad.DIFICIL,
            320
        );
        TipoElemento tipoJugador = tipoPrincipal(equipoJugador);
        TipoElemento debilContraJugador = tipoDebilA(tipoJugador);
        TipoElemento fuerteContraJugador = tipoFuerteContra(tipoJugador);

        // Dificil: predominan counters al tipo principal del jugador
        npc.agregarCriatura(crearCriaturaPorTipo(fuerteContraJugador, 8));
        npc.agregarCriatura(crearCriaturaPorTipo(fuerteContraJugador, 7));
        npc.agregarCriatura(crearCriaturaPorTipo(tipoJugador, 7));
        npc.agregarCriatura(crearCriaturaPorTipo(debilContraJugador, 6));
        return npc;
    }

    public static EntrenadorNPC rivalPorDificultad(List<Criatura> equipoJugador, NivelDificultad dificultad) {
        if (dificultad == NivelDificultad.FACIL) return rivalFacil(equipoJugador);
        if (dificultad == NivelDificultad.DIFICIL || dificultad == NivelDificultad.EXPERTO) {
            return rivalDificil(equipoJugador);
        }
        return rivalNormal(equipoJugador);
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

    private static TipoElemento tipoPrincipal(List<Criatura> equipoJugador) {
        if (equipoJugador == null || equipoJugador.isEmpty() || equipoJugador.get(0) == null) {
            return TipoElemento.FUEGO;
        }
        return equipoJugador.get(0).getTipo();
    }

    // Tipo de criatura NPC que recibe dano super efectivo del tipo del jugador
    private static TipoElemento tipoDebilA(TipoElemento tipoJugador) {
        for (TipoElemento candidato : TipoElemento.values()) {
            if (candidato.getMultiplicador(tipoJugador) > 1.0) {
                return candidato;
            }
        }
        return TipoElemento.PLANTA;
    }

    // Tipo de criatura NPC que golpea super efectivo al tipo del jugador
    private static TipoElemento tipoFuerteContra(TipoElemento tipoJugador) {
        for (TipoElemento candidato : TipoElemento.values()) {
            if (tipoJugador.getMultiplicador(candidato) > 1.0) {
                return candidato;
            }
        }
        return TipoElemento.AGUA;
    }

    private static Criatura crearCriaturaPorTipo(TipoElemento tipo, int nivel) {
        switch (tipo) {
            case FUEGO: return new Ignis(nivel);
            case AGUA: return new Aqua(nivel);
            case PLANTA: return new Herba(nivel);
            case ELECTRICO: return new Voltix(nivel);
            case TIERRA: return new Terron(nivel);
            case HIELO: return new Glacius(nivel);
            case NORMAL:
            default:
                return new Fumeo(Math.max(1, nivel));
        }
    }
}
