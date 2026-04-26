package juego.batalla;

import juego.criaturas.Criatura;
import juego.entrenadores.EntrenadorNPC;
import juego.entrenadores.Jugador;
import juego.movimientos.Movimiento;

import java.util.ArrayList;
import java.util.List;

// Manages one battle session. Has-a Jugador and either an NPC or a wild creature.
// Does NOT extend Entrenador because a battle is not a type of trainer.
public class Batalla {

    private final Jugador jugador;
    private final EntrenadorNPC npc;
    private final Criatura criaturaRival; // used only in wild encounters
    private final boolean esEncuentroSalvaje;
    private int turnoActual;
    private boolean activa;
    private String ganador;

    // Constructor for NPC battles
    public Batalla(Jugador jugador, EntrenadorNPC npc) {
        this.jugador = jugador;
        this.npc = npc;
        this.criaturaRival = null;
        this.esEncuentroSalvaje = false;
        this.turnoActual = 1;
        this.activa = true;
        this.ganador = null;
    }

    // Constructor for wild creature encounters
    public Batalla(Jugador jugador, Criatura salvaje) {
        this.jugador = jugador;
        this.npc = null;
        this.criaturaRival = salvaje;
        this.esEncuentroSalvaje = true;
        this.turnoActual = 1;
        this.activa = true;
        this.ganador = null;
    }

    // Executes one full turn given the player's chosen move index.
    // Returns a log of everything that happened this turn.
    public List<String> ejecutarTurno(int movIndexJugador) {
        List<String> log = new ArrayList<>();

        Criatura criaturaJugador = jugador.getCriaturaActiva();
        Criatura rival = getRivalActivo();

        if (criaturaJugador == null || rival == null || !activa) {
            log.add("La batalla ya termino.");
            return log;
        }

        Movimiento movJugador = criaturaJugador.getMovimientos()[movIndexJugador];
        if (movJugador == null || !movJugador.tienePP()) {
            log.add(criaturaJugador.getNombre() + " no puede usar ese movimiento!");
            return log;
        }

        // Pick the NPC move before turns execute so both choices are locked in
        Movimiento movRival = npc != null
            ? npc.elegirMovimiento(rival, criaturaJugador)
            : pickWildMove(rival);

        // Faster creature attacks first; ties go to the player
        if (criaturaJugador.getVelocidad() >= rival.getVelocidad()) {
            log.addAll(ejecutarAtaque(criaturaJugador, movJugador, rival));
            if (rival.estaViva()) {
                log.addAll(ejecutarAtaque(rival, movRival, criaturaJugador));
            }
        } else {
            log.addAll(ejecutarAtaque(rival, movRival, criaturaJugador));
            if (criaturaJugador.estaViva()) {
                log.addAll(ejecutarAtaque(criaturaJugador, movJugador, rival));
            }
        }

        // Passive abilities tick at end of turn
        criaturaJugador.habilidadEspecial();
        rival.habilidadEspecial();

        turnoActual++;
        verificarFin(log);

        return log;
    }

    // Builds the attack narrative and applies damage to the defender
    private List<String> ejecutarAtaque(Criatura atacante, Movimiento mov, Criatura defensor) {
        List<String> log = new ArrayList<>();

        if (mov == null) {
            log.add(atacante.getNombre() + " no tiene movimientos disponibles!");
            return log;
        }

        if (!mov.usar()) {
            log.add(atacante.getNombre() + ": sin PP para " + mov.getNombre() + "!");
            return log;
        }

        int danio = calcularDanio(mov, atacante, defensor);
        defensor.recibirDanio(danio);

        log.add(atacante.getNombre() + " usa " + mov.getNombre() + "!");

        double multi = defensor.getTipo().getMultiplicador(mov.getTipo());
        if (multi >= 2.0) log.add("Es super efectivo!");
        else if (multi <= 0.0) log.add("No afecta a " + defensor.getNombre() + "...");
        else if (multi < 1.0) log.add("No es muy efectivo...");

        log.add(defensor.getNombre() + " recibe " + danio + " de danio. HP: "
                + defensor.getHpActual() + "/" + defensor.getHpMax());

        // Reset the attacker's per-move bonus after it fires
        atacante.resetBonuses();

        return log;
    }

    // Simplified Gen-1 damage formula with STAB and type effectiveness
    public int calcularDanio(Movimiento mov, Criatura atacante, Criatura defensor) {
        double base = (2.0 * atacante.getNivel() / 5.0 + 2.0)
                      * mov.getPoder()
                      * ((double) atacante.getAtaque() / defensor.getDefensa())
                      / 50.0 + 2.0;

        // Same Type Attack Bonus: +50% when move type matches attacker type
        double stab = mov.getTipo() == atacante.getTipo() ? 1.5 : 1.0;

        // Type effectiveness from the defender's perspective
        double tipo = defensor.getTipo().getMultiplicador(mov.getTipo());

        // Passive bonus damage from type abilities (e.g. fire calor stacks)
        double bonusAtk = atacante.getBonusDanio();

        // Passive damage reduction from type abilities (e.g. water humedad)
        double bonusDef = defensor.getBonusDefensa();

        // Random variance 85%-100%
        double rand = 0.85 + Math.random() * 0.15;

        return Math.max(1, (int)(base * stab * tipo * bonusAtk * bonusDef * rand));
    }

    // Checks if either side has run out of living creatures and records the winner
    private void verificarFin(List<String> log) {
        Criatura rival = getRivalActivo();
        Criatura criaturaJugador = jugador.getCriaturaActiva();

        // Switch NPC to its next living creature if the active one fainted
        if (npc != null && (rival == null || !rival.estaViva())) {
            if (npc.tieneCriaturasVivas()) {
                npc.avanzarSiguienteViva();
                log.add(npc.getNombre() + " envia a " + npc.getCriaturaActiva().getNombre() + "!");
                return;
            }
        }

        if (criaturaJugador == null || !criaturaJugador.estaViva()) {
            if (!jugador.tieneCriaturasVivas()) {
                activa = false;
                ganador = npc != null ? npc.getNombre() : "salvaje";
                log.add("Todos tus criaturas han caido. Perdiste la batalla!");
            } else {
                jugador.avanzarSiguienteViva();
                log.add("Enviando a " + jugador.getCriaturaActiva().getNombre() + "!");
            }
            return;
        }

        if (rival == null || !rival.estaViva()) {
            if (npc == null || !npc.tieneCriaturasVivas()) {
                activa = false;
                ganador = jugador.getNombre();
                distribuirExp(log);
                if (npc != null) {
                    jugador.ganarDinero(npc.getRecompensa());
                    log.add("Ganaste $" + npc.getRecompensa() + "!");
                    npc.setDerrotado(true);
                }
            }
        }
    }

    // Wild creature picks a random available move
    private Movimiento pickWildMove(Criatura c) {
        Movimiento[] movs = c.getMovimientos();
        for (int i = 0; i < 10; i++) {
            int idx = (int)(Math.random() * movs.length);
            if (movs[idx] != null && movs[idx].tienePP()) return movs[idx];
        }
        for (Movimiento m : movs) {
            if (m != null && m.tienePP()) return m;
        }
        return null;
    }

    // Spreads experience evenly across all living party members
    public void distribuirExp(List<String> log) {
        Criatura rival = getRivalActivo();
        if (rival == null) return;
        int expBase = rival.getNivel() * 50;

        int vivas = 0;
        for (Criatura c : jugador.getEquipo()) {
            if (c.estaViva()) vivas++;
        }
        if (vivas == 0) return;

        int expPorCriatura = expBase / vivas;
        for (Criatura c : jugador.getEquipo()) {
            if (c.estaViva()) {
                int nivelAntes = c.getNivel();
                c.ganarExp(expPorCriatura);
                if (c.getNivel() > nivelAntes) {
                    log.add(c.getNombre() + " subio al nivel " + c.getNivel() + "!");
                }
            }
        }
    }

    // Returns the current opponent creature regardless of battle type
    public Criatura getRivalActivo() {
        if (esEncuentroSalvaje) return criaturaRival;
        if (npc == null) return null;
        return npc.getCriaturaActiva();
    }

    public boolean haTerminado() { return !activa; }
    public String getGanador() { return ganador; }
    public int getTurnoActual() { return turnoActual; }
    public boolean isEncuentroSalvaje() { return esEncuentroSalvaje; }
    public Jugador getJugador() { return jugador; }
    public EntrenadorNPC getNpc() { return npc; }
}
