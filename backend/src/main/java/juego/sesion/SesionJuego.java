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

     /* Guarda el estado actual de la sesión en la base de datos.
     * Asume que ya existe una fila en 'sesion_juego' (o la crea).
     */
    public void guardarSesion(int idSesion) {
    String sqlSesion = """
        INSERT INTO sesion_juego (id, jugador_id, fase, zona, siguiente_npc_index, puedes_curar)
        VALUES (?, ?, ?, ?, ?, ?)
        ON CONFLICT (id) DO UPDATE SET
            fase = EXCLUDED.fase,
            zona = EXCLUDED.zona,
            siguiente_npc_index = EXCLUDED.siguiente_npc_index,
            puedes_curar = EXCLUDED.puedes_curar
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlSesion)) {
    
            stmt.setInt(1, idSesion);
            stmt.setInt(2, jugador.getId());               // Asumiendo que Jugador tiene getId()
            stmt.setString(3, fase.name());
            stmt.setInt(4, zona);
            stmt.setInt(5, siguienteNpcIndex);
            stmt.setBoolean(6, puedesCurar);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al guardar la sesión: " + e.getMessage());
        }
    }

    /**
     * Carga el estado de una sesión desde la base de datos.
     * Reconstruye el jugador, sus criaturas, objetos, esferas, y la fase actual.
     */
    public void cargarSesion(int idSesion) {
        String sql = """
            SELECT sj.fase, sj.zona, sj.siguiente_npc_index, sj.puedes_curar,
                   j.nombre, j.dinero, j.medallas
            FROM sesion_juego sj
            JOIN jugador j ON sj.jugador_id = j.id_entrenador
            WHERE sj.id = ?
        """;
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    
            stmt.setInt(1, idSesion);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                this.fase = Fase.valueOf(rs.getString("fase"));
                this.zona = rs.getInt("zona");
                this.siguienteNpcIndex = rs.getInt("siguiente_npc_index");
                this.puedesCurar = rs.getBoolean("puedes_curar");
    
                // Reconstruir el jugador (necesitarás ajustar según tu lógica)
                String nombreJugador = rs.getString("nombre");
                int dinero = rs.getInt("dinero");
                int medallas = rs.getInt("medallas");
    
                this.jugador = new Jugador(nombreJugador, null);      // Starter se asigna después
                this.jugador.setDinero(dinero);
                this.jugador.setMedallas(medallas);
    
                // Cargar criaturas, objetos y esferas desde sus tablas (omitido por brevedad)
                cargarCriaturasDelJugador(conn);
                cargarObjetosDelJugador(conn);
                cargarEsferasDelJugador(conn);
            }
    
        } catch (SQLException e) {
            System.err.println("Error al cargar la sesión: " + e.getMessage());
        }
    }

    // Métodos auxiliares privados
    private void cargarCriaturasDelJugador(Connection conn) throws SQLException {
        String sql = """
            SELECT c.id, c.especie_id, c.nivel, c.hp_actual, c.hp_max,
                   c.ataque, c.defensa, c.velocidad, c.exp_total, c.exp_siguiente_nivel
            FROM criatura c
            WHERE c.entrenador_id = ? AND c.guardada_caja = FALSE
        """;
        // ... reconstruir objetos Criatura y agregarlos al equipo del jugador
    }
    
    private void cargarObjetosDelJugador(Connection conn) throws SQLException {}
    private void cargarEsferasDelJugador(Connection conn) throws SQLException {}
    public Jugador getJugador() { return jugador; }
    public Batalla getBatallaActual() { return batallaActual; }
    public Fase getFase() { return fase; }
    public List<EntrenadorNPC> getNpcs() { return npcs; }
    public int getSiguienteNpcIndex() { return siguienteNpcIndex; }
    public int getZona() { return zona; }
    public boolean isPuedesCurar() { return puedesCurar; }
}
