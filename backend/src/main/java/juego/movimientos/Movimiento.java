package juego.movimientos;

import juego.enums.Categoria;
import juego.enums.TipoElemento;

// A single battle move with power, accuracy, and limited uses (PP)
public class Movimiento {

    private final String nombre;
    private final TipoElemento tipo;
    private final Categoria categoria;
    private final int poder;
    private final int precision;
    private int ppActual;
    private final int ppMax;

    public Movimiento(String nombre, TipoElemento tipo, Categoria categoria,
                      int poder, int precision, int ppMax) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.categoria = categoria;
        this.poder = poder;
        this.precision = precision;
        this.ppMax = ppMax;
        this.ppActual = ppMax;
    }

    // Spends one PP. Returns false if the move had no PP left to use.
    public boolean usar() {
        if (ppActual <= 0) return false;
        ppActual--;
        return true;
    }

    // Refills PP to maximum (used at end of battle or healing center)
    public void restaurarPP() {
        ppActual = ppMax;
    }

    public boolean tienePP() {
        return ppActual > 0;
    }

    public String getNombre() { return nombre; }
    public TipoElemento getTipo() { return tipo; }
    public Categoria getCategoria() { return categoria; }
    public int getPoder() { return poder; }
    public int getPrecision() { return precision; }
    public int getPpActual() { return ppActual; }
    public int getPpMax() { return ppMax; }

    // Static factories so callers never have to remember constructor argument order

    public static Movimiento placaje() {
        return new Movimiento("Placaje", TipoElemento.NORMAL, Categoria.FISICO, 35, 95, 35);
    }

    public static Movimiento ascua() {
        return new Movimiento("Ascua", TipoElemento.FUEGO, Categoria.ESPECIAL, 40, 100, 25);
    }

    public static Movimiento llamarada() {
        return new Movimiento("Llamarada", TipoElemento.FUEGO, Categoria.ESPECIAL, 90, 85, 5);
    }

    public static Movimiento pistoladeagua() {
        return new Movimiento("Pistola Agua", TipoElemento.AGUA, Categoria.ESPECIAL, 40, 100, 25);
    }

    public static Movimiento hidrobomba() {
        return new Movimiento("Hidrobomba", TipoElemento.AGUA, Categoria.ESPECIAL, 90, 80, 5);
    }

    public static Movimiento hojaafilada() {
        return new Movimiento("Hoja Afilada", TipoElemento.PLANTA, Categoria.FISICO, 55, 95, 25);
    }

    public static Movimiento drenaje() {
        return new Movimiento("Drenaje", TipoElemento.PLANTA, Categoria.ESPECIAL, 75, 100, 10);
    }

    public static Movimiento impactrueno() {
        return new Movimiento("Impactrueno", TipoElemento.ELECTRICO, Categoria.ESPECIAL, 40, 100, 30);
    }

    public static Movimiento rayo() {
        return new Movimiento("Rayo", TipoElemento.ELECTRICO, Categoria.ESPECIAL, 90, 100, 10);
    }

    public static Movimiento ventisca() {
        return new Movimiento("Ventisca", TipoElemento.HIELO, Categoria.ESPECIAL, 65, 90, 10);
    }

    public static Movimiento terremoto() {
        return new Movimiento("Terremoto", TipoElemento.TIERRA, Categoria.FISICO, 80, 100, 10);
    }

    public static Movimiento trituracion() {
        return new Movimiento("Trituracion", TipoElemento.NORMAL, Categoria.FISICO, 80, 100, 15);
    }
}
