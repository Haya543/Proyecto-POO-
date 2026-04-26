package juego.api;

import juego.batalla.Batalla;
import juego.criaturas.Criatura;
import juego.entrenadores.EntrenadorNPC;
import juego.entrenadores.Jugador;
import juego.movimientos.Movimiento;
import juego.objetos.Objeto;
import juego.esferas.PokeEsfera;
import juego.sesion.SesionJuego;

import java.util.*;

// Converts domain objects to plain maps that Gson serializes to JSON responses
public class DtoMapper {

    // Builds the top-level game state object sent after every action
    public static Map<String, Object> toGameState(SesionJuego sesion, String mensaje) {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("fase", sesion.getFase().name());
        state.put("mensaje", mensaje);

        if (sesion.getJugador() != null) {
            state.put("jugador", toPlayerInfo(sesion.getJugador()));
        }

        if (sesion.getBatallaActual() != null) {
            state.put("batalla", toBattleState(sesion.getBatallaActual()));
        }

        state.put("puedesCurar", sesion.isPuedesCurar());
        state.put("zona", sesion.getZona());
        state.put("siguienteNpcIndex", sesion.getSiguienteNpcIndex());

        return state;
    }

    public static Map<String, Object> toPlayerInfo(Jugador j) {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("nombre", j.getNombre());
        info.put("dinero", j.getDinero());
        info.put("medallas", j.getMedallas());

        List<Map<String, Object>> equipo = new ArrayList<>();
        for (Criatura c : j.getEquipo()) equipo.add(toCriaturaInfo(c));
        info.put("equipo", equipo);

        List<Map<String, Object>> capturadas = new ArrayList<>();
        for (Criatura c : j.getCapturadas()) capturadas.add(toCriaturaInfo(c));
        info.put("capturadas", capturadas);

        List<Map<String, Object>> bolsa = new ArrayList<>();
        for (Objeto o : j.getBolsa()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("nombre", o.getNombre());
            item.put("descripcion", o.getDescripcion());
            item.put("cantidad", o.getCantidad());
            bolsa.add(item);
        }
        info.put("bolsa", bolsa);

        List<Map<String, Object>> esferas = new ArrayList<>();
        for (int i = 0; i < j.getEsferas().size(); i++) {
            PokeEsfera e = j.getEsferas().get(i);
            Map<String, Object> esfera = new LinkedHashMap<>();
            esfera.put("index", i);
            esfera.put("nombre", e.getNombre());
            esferas.add(esfera);
        }
        info.put("esferas", esferas);

        return info;
    }

    public static Map<String, Object> toBattleState(Batalla b) {
        Map<String, Object> battle = new LinkedHashMap<>();
        battle.put("turno", b.getTurnoActual());
        battle.put("esEncuentroSalvaje", b.isEncuentroSalvaje());
        battle.put("activa", !b.haTerminado());
        battle.put("ganador", b.getGanador());

        Criatura criaturaJugador = b.getJugador().getCriaturaActiva();
        Criatura rival = b.getRivalActivo();

        battle.put("criaturaJugador", criaturaJugador != null ? toCriaturaInfo(criaturaJugador) : null);
        battle.put("criaturaRival", rival != null ? toCriaturaInfo(rival) : null);

        if (b.getNpc() != null) {
            battle.put("nombreNpc", b.getNpc().getNombre());
        }

        return battle;
    }

    public static Map<String, Object> toCriaturaInfo(Criatura c) {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("nombre", c.getNombre());
        info.put("tipo", c.getTipo().name());
        info.put("nivel", c.getNivel());
        info.put("hp", c.getHpActual());
        info.put("hpMax", c.getHpMax());
        info.put("ataque", c.getAtaque());
        info.put("defensa", c.getDefensa());
        info.put("velocidad", c.getVelocidad());
        info.put("estaViva", c.estaViva());
        info.put("expTotal", c.getExpTotal());
        info.put("expSiguienteNivel", c.getExpSiguienteNivel());

        List<Map<String, Object>> movs = new ArrayList<>();
        for (Movimiento m : c.getMovimientos()) {
            if (m == null) {
                movs.add(null);
                continue;
            }
            Map<String, Object> mov = new LinkedHashMap<>();
            mov.put("nombre", m.getNombre());
            mov.put("tipo", m.getTipo().name());
            mov.put("categoria", m.getCategoria().name());
            mov.put("poder", m.getPoder());
            mov.put("precision", m.getPrecision());
            mov.put("ppActual", m.getPpActual());
            mov.put("ppMax", m.getPpMax());
            movs.add(mov);
        }
        info.put("movimientos", movs);

        return info;
    }

    public static List<Map<String, Object>> toNpcList(SesionJuego sesion) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (EntrenadorNPC npc : sesion.getNpcs()) {
            Map<String, Object> n = new LinkedHashMap<>();
            n.put("nombre", npc.getNombre());
            n.put("dialogo", npc.saludar());
            n.put("dificultad", npc.getDificultad().name());
            n.put("recompensa", npc.getRecompensa());
            n.put("derrotado", npc.isDerrotado());
            list.add(n);
        }
        return list;
    }
}
