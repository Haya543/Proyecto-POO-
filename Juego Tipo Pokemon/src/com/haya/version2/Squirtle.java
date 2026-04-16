package com.haya.version2;

public class Squirtle extends Pokemon{

    public Squirtle(String nombre, int nivel, int hpMaximo, int hpActual, int ataque, int defensa, int velocidad, Tipo tipoPrincipal, Tipo tipoSecundario, Movimiento movimiento) {
        super(nombre, nivel, hpMaximo, hpActual, ataque, defensa, velocidad, tipoPrincipal, tipoSecundario, movimiento);
        Tipo agua = Tipo.AGUA;
    }

    @Override
    public void gritoGuerra() {
        System.out.println("¡Squir! ¡Squirtle!");
    }
}
