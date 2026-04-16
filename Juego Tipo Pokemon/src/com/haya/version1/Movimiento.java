package com.haya.version1;

public abstract class Movimiento {
    protected String nombre;
    protected int poder, pp, ppMax;
    protected Elemento tipo;

    public abstract void usar(Pokemon usuario, Pokemon objetivo);

    public void restaurarPP(){
        this.pp = this.ppMax;
    }

    public boolean tienePP(){
        return this.pp > 0;
    }

}
