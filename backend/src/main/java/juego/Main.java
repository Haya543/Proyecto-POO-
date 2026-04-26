package juego;

import com.google.gson.Gson;
import io.javalin.Javalin;
import juego.api.BattleController;
import juego.api.GameController;
import juego.sesion.SesionJuego;
import juego.db.DatabaseConnection;

// Entry point. Starts the Javalin REST API on port 7070. MAIN FILE
public class Main {

    public static void main(String[] args) {
        // Inicializar la conexión a la base de datos
        try {
            DatabaseConnection.getConnection();
            System.out.println("Conexión a PostgreSQL establecida.");
        } catch (Exception e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            e.printStackTrace();
            return;   // Detener el servidor si no hay BD
        }

        SesionJuego sesion = new SesionJuego();
        Gson gson = new Gson();

        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> {
                    rule.allowHost("http://localhost:5173");
                    rule.allowHost("http://127.0.0.1:5173");
                });
            });
        });

        new GameController(sesion, gson).registrar(app);
        new BattleController(sesion, gson).registrar(app);

        // Cerrar la conexión cuando la aplicación se detenga
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DatabaseConnection.closeConnection();
            System.out.println("Conexión a PostgreSQL cerrada.");
        }));

        app.start(7070);
        System.out.println("Servidor iniciado en http://localhost:7070");
    }
}
