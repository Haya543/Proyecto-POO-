package juego.objetos;

import juego.criaturas.Criatura;

// Base class for all usable items in the player's bag
public abstract class Objeto {

    protected String nombre;
    protected String descripcion;
    protected int cantidad;

    protected Objeto(String nombre, String descripcion, int cantidad) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
    }

    // Subclasses implement the actual effect (heal, cure status, etc.)
    public abstract void usar(Criatura objetivo);

    public boolean tieneStock() {
        return cantidad > 0;
    }

    // Called by usar() implementations after the effect is applied
    protected void consumir() {
        if (cantidad > 0) cantidad--;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getCantidad() { return cantidad; }
}
