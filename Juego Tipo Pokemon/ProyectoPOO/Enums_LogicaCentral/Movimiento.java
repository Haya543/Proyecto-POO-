package ProyectoPOO.Enums_LogicaCentral;

public class Movimiento {
    private String Nombre;
    private TipoElemento tipo;
    private Categoria categoria;
    private int poder, precision, ppActual, ppMax;

    public boolean usar(){}
    public void restaurarPP(){}

    public int getPoder() {
        return poder;
    }

    public TipoElemento getTipo() {
        return tipo;
    }
}
