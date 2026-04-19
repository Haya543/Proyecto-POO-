package ProyectoPOO.DataBase;

import ProyectoPOO.Ciraturas_Interfaces.Criatura;

import java.util.List;

public class RepositorioCriatura {
    private ConexionDB db;

    public Criatura buscarPorId(int id){}
    public int[] obtenerStatsPorNivel(int criaturaId, int nivel){}
    public void guardarProgreso(Criatura c, int jugadorId){}
    public List<Criatura> listarTodas(){}

}
