package com.haya.version2;

import java.util.List;

public enum Tipo {
    FUEGO,
    AGUA,
    PLANTA,
    ELECTRICO,
    NORMAL;

    List<Tipo> debilidades; //hacen doble de daño
    List<Tipo> fortalezas;  //a los que les hace el doble de daño
    List<Tipo> inmunidades; //no le hacen daño


    Tipo(List<Tipo> debilidades, List<Tipo> fortalezas, List<Tipo> inmunidades) {
        this.debilidades = debilidades;
        this.fortalezas = fortalezas;
        this.inmunidades = inmunidades;
    }

    Tipo() {

    }

    double obtenerMultiplicador(Tipo tipoDefensor){

    }
}
