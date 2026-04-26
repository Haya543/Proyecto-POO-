package juego.criaturas;

import juego.enums.TipoElemento;

// Marks a creature as catchable by the player using a PokeEsfera
public interface Capturable {
    // Returns the current capture probability (0.0 to 1.0), factoring in remaining HP
    double detTasaCaptura();
    TipoElemento getTipo();
    int getHpActual();
    int getHpMax();
}
