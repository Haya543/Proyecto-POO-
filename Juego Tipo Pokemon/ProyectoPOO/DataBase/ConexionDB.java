package ProyectoPOO.DataBase;

import java.sql.Connection;

public class ConexionDB {
    private static ConexionDB instancia;
    private Connection conexion;
    private String URL, USUARIO, PASSWORD;

    private ConexionDB(){}
    public ConexionDB static getInstance(){}
    public Connection getConexion(){}
    public void cerrar(){}

}
