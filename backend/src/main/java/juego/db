package juego.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Administra la conexión JDBC a PostgreSQL.
 * Los parámetros coinciden con los definidos en database/Dockerfile.
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/PokeGame";
    private static final String USER = "PokeUser";
    private static final String PASSWORD = "PokePassword";
    private static Connection connection;

    // Constructor privado para evitar instanciación
    private DatabaseConnection() {}

    /**
     * Retorna la conexión activa. Si no existe o está cerrada, crea una nueva.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    /**
     * Cierra la conexión si está abierta.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
