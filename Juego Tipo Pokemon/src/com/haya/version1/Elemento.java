package com.haya.version1;

public enum Elemento {
    FUEGO,
    AGUA,
    PLANTA,
    ELECTRICO,
    NORMAL;

    public double getMultiplicador(Elemento atacante){
        if(this == PLANTA && atacante == FUEGO) return 2.0;
        if(this == FUEGO && atacante == AGUA) return 2.0;
        if(this == AGUA && atacante == PLANTA) return 2.0;
        if(this == AGUA && atacante == ELECTRICO) return 2.0;

        return 1.0;
    }
}
