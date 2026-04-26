package juego;

import com.google.gson.Gson;
import io.javalin.Javalin;
import juego.api.BattleController;
import juego.api.GameController;
import juego.sesion.SesionJuego;

// Entry point. Starts the Javalin REST API on port 7070.
public class Main {

    public static void main(String[] args) {
        SesionJuego sesion = new SesionJuego();
        Gson gson = new Gson();

        Javalin app = Javalin.create(config -> {
            // Allow the React dev server to call this API
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> {
                    rule.allowHost("http://localhost:5173");
                    rule.allowHost("http://127.0.0.1:5173");
                });
            });
        });

        new GameController(sesion, gson).registrar(app);
        new BattleController(sesion, gson).registrar(app);

        app.start(7070);
        System.out.println("Servidor iniciado en http://localhost:7070");
    }
}
