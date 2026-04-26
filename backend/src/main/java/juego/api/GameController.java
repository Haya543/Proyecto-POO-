package juego.api;

import com.google.gson.Gson;
import io.javalin.Javalin;
import juego.batalla.Batalla;
import juego.sesion.SesionJuego;

import java.util.Map;

// Handles all world-level API routes (/api/game/*)
public class GameController {

    private final SesionJuego sesion;
    private final Gson gson;

    public GameController(SesionJuego sesion, Gson gson) {
        this.sesion = sesion;
        this.gson = gson;
    }

    // Serializes the given object to JSON and sends it as the response body
    private void json(io.javalin.http.Context ctx, Object obj) {
        ctx.contentType("application/json").result(gson.toJson(obj));
    }

    public void registrar(Javalin app) {
        // Creates a new game session with the player's name and chosen starter
        app.post("/api/game/new", ctx -> {
            @SuppressWarnings("unchecked")
            Map<String, Object> body = gson.fromJson(ctx.body(), Map.class);
            String nombre = (String) body.get("nombre");
            Number starterRaw = (Number) body.getOrDefault("starterIndex", 0);
            int starterIndex = starterRaw != null ? starterRaw.intValue() : 0;
            sesion.iniciar(nombre, starterIndex);
            json(ctx, DtoMapper.toGameState(sesion, null));
        });

        // Returns the current full game state (used on page load / polling)
        app.get("/api/game/state", ctx -> {
            json(ctx, DtoMapper.toGameState(sesion, null));
        });

        // Attempts a wild encounter in the current zone (40% chance)
        app.post("/api/game/explore", ctx -> {
            Batalla batalla = sesion.explorar();
            String mensaje = batalla != null ? null : "No encontraste nada esta vez...";
            json(ctx, DtoMapper.toGameState(sesion, mensaje));
        });

        // Heals the player's team once per NPC defeat
        app.post("/api/game/heal", ctx -> {
            String mensaje;
            if (sesion.isPuedesCurar()) {
                sesion.curarEquipo();
                mensaje = "Tu equipo ha sido curado completamente!";
            } else {
                mensaje = "Ya curaste tu equipo. Derrota al siguiente entrenador primero.";
            }
            json(ctx, DtoMapper.toGameState(sesion, mensaje));
        });

        // Starts a battle against the next NPC in the story sequence
        app.post("/api/game/challenge-npc", ctx -> {
            Batalla batalla = sesion.retarNpc();
            String mensaje = batalla == null ? "No hay mas entrenadores por retar." : null;
            json(ctx, DtoMapper.toGameState(sesion, mensaje));
        });

        // Returns the list of NPCs with their defeated status and dialogue
        app.get("/api/game/npcs", ctx -> {
            json(ctx, DtoMapper.toNpcList(sesion));
        });
    }
}
