package juego.criaturas;

// Marks a creature as capable of gaining experience and leveling up
public interface Nivelable {
    void ganarExp(int exp);
    void subirNivel();
    int getNivel();
}
