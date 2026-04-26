package juego.enums;

// Controls NPC stat scaling and whether the NPC uses smart move selection
public enum NivelDificultad {
    FACIL, MEDIO, DIFICIL, EXPERTO;

    // Multiplier applied to NPC creature stats at creation time
    public double getMultiplicadorStats() {
        switch (this) {
            case FACIL: return 0.8;
            case MEDIO: return 1.0;
            case DIFICIL: return 1.2;
            case EXPERTO: return 1.5;
            default: return 1.0;
        }
    }

    // When true, the NPC picks moves based on type advantage instead of randomly
    public boolean getIAActiva() {
        return this != FACIL;
    }
}
