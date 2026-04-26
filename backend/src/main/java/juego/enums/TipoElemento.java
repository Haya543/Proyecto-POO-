package juego.enums;

// The 7 element types available in the game
public enum TipoElemento {
    FUEGO, AGUA, PLANTA, ELECTRICO, TIERRA, HIELO, NORMAL;

    // Returns the damage multiplier when THIS type receives an attack of the given type.
    // Called as: defender.getTipo().getMultiplicador(move.getTipo())
    public double getMultiplicador(TipoElemento atacante) {
        switch (this) {
            case FUEGO:
                if (atacante == AGUA) return 2.0;
                if (atacante == PLANTA || atacante == FUEGO) return 0.5;
                break;
            case AGUA:
                if (atacante == PLANTA || atacante == ELECTRICO) return 2.0;
                if (atacante == FUEGO || atacante == AGUA) return 0.5;
                break;
            case PLANTA:
                if (atacante == FUEGO || atacante == HIELO) return 2.0;
                if (atacante == AGUA || atacante == PLANTA) return 0.5;
                break;
            case ELECTRICO:
                if (atacante == TIERRA) return 0.0;
                if (atacante == ELECTRICO) return 0.5;
                break;
            case TIERRA:
                if (atacante == AGUA || atacante == PLANTA || atacante == HIELO) return 2.0;
                if (atacante == ELECTRICO || atacante == FUEGO) return 0.5;
                break;
            case HIELO:
                if (atacante == FUEGO) return 2.0;
                if (atacante == HIELO) return 0.5;
                break;
            case NORMAL:
                break;
        }
        return 1.0;
    }
}
