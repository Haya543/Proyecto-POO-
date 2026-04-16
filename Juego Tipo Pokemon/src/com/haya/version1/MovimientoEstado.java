package com.haya.version1;

public class MovimientoEstado extends Movimiento{
    protected String efecto;

    public MovimientoEstado(String efecto) {
        this.efecto = efecto;
    }

    @Override
    public void usar(Pokemon usuario, Pokemon objetivo) {

    }
}
