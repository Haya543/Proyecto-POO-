package com.haya.version2;

public abstract class Pokemon {
    protected String nombre;
    protected int nivel, hpMaximo, hpActual, ataque, defensa, velocidad;
    Tipo tipoPrincipal, tipoSecundario;
    Movimiento movimiento;


    public Pokemon(String nombre, int nivel, int hpMaximo, int hpActual, int ataque, int defensa, int velocidad, Tipo tipoPrincipal, Tipo tipoSecundario, Movimiento movimiento) {
        this.nombre = nombre;
        this.nivel = nivel;
        this.hpMaximo = hpMaximo;
        this.hpActual = hpActual;
        this.ataque = ataque;
        this.defensa = defensa;
        this.velocidad = velocidad;
        this.tipoPrincipal = tipoPrincipal;
        this.tipoSecundario = tipoSecundario;
        this.movimiento = movimiento;
    }

    public void atacar(Movimiento mov, Pokemon objetivo){

    }

    public void recibirDaño(int cantidad){
        if(hpActual <= hpMaximo){
            hpActual--;
        }
        if(hpActual <= 0){
            hpActual = 0;
        }
    }

    public boolean estaDebilitado(){
        if(hpActual == 0){
            return true;
        }
        return false;
    }

    public void aprenderMovimiento(Movimiento mov, int slot){

    }

    public abstract void gritoGuerra();
}
