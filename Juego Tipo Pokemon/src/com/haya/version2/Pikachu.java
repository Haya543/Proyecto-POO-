package com.haya.version2;

public class Pikachu extends Pokemon{

    public Pikachu(String nombre, int nivel, int hpMaximo, int hpActual, int ataque, int defensa, int velocidad, Tipo tipoPrincipal, Tipo tipoSecundario, Movimiento movimiento) {
        super(nombre, nivel, hpMaximo, hpActual, ataque, defensa, velocidad, tipoPrincipal, tipoSecundario, movimiento);
        Tipo electrico = Tipo.ELECTRICO;
    }

    @Override
    public void gritoGuerra() {
        System.out.println("¡Pika!¡Pikachu!");
    }


}
