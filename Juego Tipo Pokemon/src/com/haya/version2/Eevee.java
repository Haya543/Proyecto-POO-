package com.haya.version2;

public class Eevee extends Pokemon{

    public Eevee(String nombre, int nivel, int hpMaximo, int hpActual, int ataque, int defensa, int velocidad, Tipo tipoPrincipal, Tipo tipoSecundario, Movimiento movimiento) {
        super(nombre, nivel, hpMaximo, hpActual, ataque, defensa, velocidad, tipoPrincipal, tipoSecundario, movimiento);
        Tipo normal = Tipo.NORMAL;
    }

    @Override
    public void gritoGuerra() {
        System.out.println("¡Ev!¡Eevee");
    }


}
