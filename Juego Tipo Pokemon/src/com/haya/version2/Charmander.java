package com.haya.version2;

public class Charmander extends Pokemon{

    @Override
    public Charmander (int nivel) {
        super(nombre, nivel, hpMaximo, hpActual, ataque, defensa, velocidad, tipoPrincipal, tipoSecundario, movimiento);
    }

    @Override
    public void gritoGuerra() {
        System.out.println("¡Char! ¡Charmander!");
    }
}
