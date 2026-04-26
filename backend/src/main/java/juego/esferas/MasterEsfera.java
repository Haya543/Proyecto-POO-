package juego.esferas;

import juego.criaturas.Capturable;

// Guaranteed capture. The lanzar override skips the random roll entirely.
public class MasterEsfera extends PokeEsfera {

    public MasterEsfera() {
        super("Master Esfera", 255.0);
    }

    @Override
    public boolean lanzar(Capturable c) {
        return true;
    }
}
