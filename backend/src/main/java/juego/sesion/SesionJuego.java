package juego.sesion;

import juego.batalla.Batalla;
import juego.criaturas.Criatura;
import juego.criaturas.instancias.*;
import juego.entrenadores.EntrenadorNPC;
import juego.entrenadores.Jugador;
import juego.enums.TipoElemento;

import java.util.ArrayList;
import java.util.List;

// Holds all mutable state for one game session. Single instance per server process.
public class SesionJuego {

    public enum Fase { INICIO, MAPA, BATALLA, JUEGO_TERMINADO }

    private Jugador jugador;
    private Batalla batallaActual;
    private Fase fase;
    private List<EntrenadorNPC> npcs;
    private int siguienteNpcIndex;
    private int zona; // 1 = before Marina, 2 = after Marina
    private boolean puedesCurar; // reset to true after each NPC defeat

    public SesionJuego() {
        this.fase = Fase.INICIO;
    }

    // Initializes the session with the player's chosen name and starter index (0=Ignis,1=Aqua,2=Herba)
    public void iniciar(String nombreJugador, int starterIndex) {
        Criatura starter;
        switch (starterIndex) {
            case 1: starter = new Aqua(5); break;
            case 2: starter = new Herba(5); break;
            default: starter = new Ignis(5); break;
        }

        this.jugador = new Jugador(nombreJugador, starter);
        this.fase = Fase.MAPA;
        this.zona = 1;
        this.siguienteNpcIndex = 0;
        this.puedesCurar = true;

        // Build the NPC sequence with knowledge of the player's starter type
        TipoElemento starterTipo = starter.getTipo();
        this.npcs = new ArrayList<>();
        npcs.add(EntrenadorNPC.rival(starterTipo));
        npcs.add(EntrenadorNPC.liderMarina());
        npcs.add(EntrenadorNPC.liderBrenno());
        npcs.add(EntrenadorNPC.campeonRex());
    }

    // Attempts a wild encounter (40% chance). Returns the created batalla or null.
    public Batalla explorar() {
        if (Math.random() < 0.4) {
            Criatura salvaje = generarCriaturaSalvaje();
            batallaActual = new Batalla(jugador, salvaje);
            fase = Fase.BATALLA;
            return batallaActual;
        }
        return null;
    }

    // Starts a battle against the next undefeated NPC
    public Batalla retarNpc() {
        if (siguienteNpcIndex >= npcs.size()) return null;
        EntrenadorNPC npc = npcs.get(siguienteNpcIndex);
        if (npc.isDerrotado()) {
            siguienteNpcIndex++;
            return retarNpc();
        }
        batallaActual = new Batalla(jugador, npc);
        fase = Fase.BATALLA;
        return batallaActual;
    }

    // Called when the active battle ends to update session state
    public void cerrarBatalla() {
        if (batallaActual == null) return;
        if (batallaActual.getGanador() != null &&
            batallaActual.getGanador().equals(jugador.getNombre())) {

            // Advance NPC index and unlock healing after each NPC win
            if (!batallaActual.isEncuentroSalvaje()) {
                siguienteNpcIndex++;
                puedesCurar = true;

                if (siguienteNpcIndex == 2) zona = 2; // unlock zone 2 after Marina

                if (siguienteNpcIndex >= npcs.size()) {
                    jugador.ganarMedalla();
                    fase = Fase.JUEGO_TERMINADO;
                    return;
                }
            }
        }
        batallaActual = null;
        fase = Fase.MAPA;
    }

    public void curarEquipo() {
        jugador.curarEquipo();
        puedesCurar = false;
    }

    // Picks a random wild creature appropriate to the current zone
    private Criatura generarCriaturaSalvaje() {
        if (zona == 1) {
            int r = (int)(Math.random() * 2);
            int nivel = 5 + (int)(Math.random() * 4);
            return r == 0 ? new Voltix(nivel) : new Fumeo(nivel);
        } else {
            int r = (int)(Math.random() * 4);
            int nivel = 8 + (int)(Math.random() * 5);
            switch (r) {
                case 0: return new Voltix(nivel);
                case 1: return new Fumeo(nivel);
                case 2: return new Glacius(nivel);
                default: return new Terron(nivel);
            }
        }
    }

    public Jugador getJugador() { return jugador; }
    public Batalla getBatallaActual() { return batallaActual; }
    public Fase getFase() { return fase; }
    public List<EntrenadorNPC> getNpcs() { return npcs; }
    public int getSiguienteNpcIndex() { return siguienteNpcIndex; }
    public int getZona() { return zona; }
    public boolean isPuedesCurar() { return puedesCurar; }
}
