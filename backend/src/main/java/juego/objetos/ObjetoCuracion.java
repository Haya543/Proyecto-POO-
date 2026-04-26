package juego.objetos;

import juego.criaturas.Criatura;

// Restores a fixed amount of HP to a creature
public class ObjetoCuracion extends Objeto {

    private final int cantidadCura;

    public ObjetoCuracion(String nombre, String descripcion, int cantidad, int cantidadCura) {
        super(nombre, descripcion, cantidad);
        this.cantidadCura = cantidadCura;
    }

    @Override
    public void usar(Criatura objetivo) {
        if (!tieneStock()) return;
        objetivo.curar(cantidadCura);
        consumir();
    }

    public int getCantidadCura() { return cantidadCura; }

    // Common healing items as factory methods

    public static ObjetoCuracion pocion() {
        return new ObjetoCuracion("Pocion", "Restaura 20 HP", 3, 20);
    }

    public static ObjetoCuracion superPocion() {
        return new ObjetoCuracion("Super Pocion", "Restaura 50 HP", 1, 50);
    }

    public static ObjetoCuracion pocimaCura() {
        return new ObjetoCuracion("Pocima Cura", "Restaura 200 HP", 1, 200);
    }
}
