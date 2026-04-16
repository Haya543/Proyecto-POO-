package com.haya.version1;

import java.util.ArrayList;
import java.util.List;

public class Pokemon {
    private String nombre;
    private int nivel, hpActual, hpMax, ataque, defensa;
    private Elemento tipo;
    private List<Movimiento> movimientos;

    public Pokemon(String nombre, int nivel, int hpActual, int hpMax, int ataque, int defensa, Elemento tipo, List<Movimiento> movimientos) {
        this.nombre = nombre;
        this.nivel = nivel;
        this.hpActual = hpActual;
        this.hpMax = hpMax;
        this.ataque = ataque;
        this.defensa = defensa;
        this.tipo = tipo;
        this.movimientos = new ArrayList<>();
    }


    public void recibirDaño(int daño){
        this.hpActual -= daño;
        if(this.hpActual < 0) this.hpActual = 0;
    }

    public boolean estaDerrotado(){
        return this.hpActual <= 0;
    }

    public void atacar(Pokemon objetivo, Movimiento movimiento){
        movimiento.usar(this, objetivo);
    }
}
