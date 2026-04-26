package juego.api;

import com.google.gson.Gson;
import io.javalin.Javalin;
import juego.batalla.Batalla;
import juego.criaturas.Criatura;
import juego.entrenadores.Jugador;
import juego.sesion.SesionJuego;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Handles all battle-phase API routes (/api/battle/*)
public class BattleController {

    private final SesionJuego sesion;
    private final Gson gson;

    public BattleController(SesionJuego sesion, Gson gson) {
        this.sesion = sesion;
        this.gson = gson;
    }

    private void json(io.javalin.http.Context ctx, Object obj) {
        ctx.contentType("application/json").result(gson.toJson(obj));
    }

    public void registrar(Javalin app) {
        // Returns the current battle state (used on battle screen load)
        app.get("/api/battle/state", ctx -> {
            Batalla batalla = sesion.getBatallaActual();
            if (batalla == null) {
                ctx.status(404).result("No hay batalla activa");
                return;
            }
            json(ctx, DtoMapper.toBattleState(batalla));
        });

        // Executes one turn. Action types: ATACAR, OBJETO, ESFERA, HUIR
        app.post("/api/battle/action", ctx -> {
            @SuppressWarnings("unchecked")
            Map<String, Object> body = gson.fromJson(ctx.body(), Map.class);
            String tipo = (String) body.get("tipo");
            Number indexRaw = (Number) body.getOrDefault("index", 0);
            int index = indexRaw != null ? indexRaw.intValue() : 0;

            Batalla batalla = sesion.getBatallaActual();
            if (batalla == null || batalla.haTerminado()) {
                ctx.status(400).result("No hay batalla activa");
                return;
            }

            List<String> log = new ArrayList<>();

            switch (tipo) {
                case "ATACAR":
                    log = batalla.ejecutarTurno(index);
                    break;

                case "OBJETO":
                    Jugador j = sesion.getJugador();
                    Criatura objetivo = j.getCriaturaActiva();
                    String msg = j.usarObjeto(index, objetivo);
                    log.add(msg);
                    // NPC still attacks after item use
                    if (!batalla.haTerminado()) {
                        juego.movimientos.Movimiento movRival = batalla.getNpc() != null
                            ? batalla.getNpc().elegirMovimiento(
                                batalla.getRivalActivo(), objetivo)
                            : null;
                        if (movRival != null) {
                            List<String> npcLog = new ArrayList<>();
                            int danio = batalla.calcularDanio(movRival, batalla.getRivalActivo(), objetivo);
                            objetivo.recibirDanio(danio);
                            movRival.usar();
                            npcLog.add(batalla.getRivalActivo().getNombre() + " usa " + movRival.getNombre() + "!");
                            npcLog.add(objetivo.getNombre() + " recibe " + danio + " de danio.");
                            log.addAll(npcLog);
                        }
                    }
                    break;

                case "ESFERA":
                    if (!batalla.isEncuentroSalvaje()) {
                        log.add("No puedes atrapar criaturas de entrenadores!");
                        break;
                    }
                    Criatura rival = batalla.getRivalActivo();
                    boolean capturado = sesion.getJugador().lanzarEsfera(index, rival);
                    if (capturado) {
                        log.add("Capturaste a " + rival.getNombre() + "!");
                        batalla = sesion.getBatallaActual();
                        // End the battle on successful capture
                        sesion.cerrarBatalla();
                    } else {
                        log.add(rival.getNombre() + " escapo de la esfera!");
                    }
                    break;

                case "HUIR":
                    if (!batalla.isEncuentroSalvaje()) {
                        log.add("No puedes huir de batallas contra entrenadores!");
                        break;
                    }
                    if (Math.random() > 0.5) {
                        log.add("Huiste exitosamente!");
                        sesion.cerrarBatalla();
                    } else {
                        log.add("No pudiste escapar!");
                    }
                    break;
            }

            // Close battle if it ended naturally this turn
            if (sesion.getBatallaActual() != null && sesion.getBatallaActual().haTerminado()) {
                sesion.cerrarBatalla();
            }

            json(ctx, DtoMapper.toGameState(sesion, String.join("\n", log)));
        });
    }
}
