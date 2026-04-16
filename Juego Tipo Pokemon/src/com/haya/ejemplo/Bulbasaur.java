package com.haya.ejemplo;

public class Bulbasaur extends Pokemon implements IPlanta{

    public Bulbasaur() {
    }

    @Override
    public void atacarDrenaje() {
        System.out.println("Soy Bulbasaur y estoy usando Drenaje");
    }

    @Override
    public void atacarParalizar() {
        System.out.println("Soy Bulbasaur y estoy usando Paralizar");
    }

    @Override
    public void atacarHojaAfilada() {
        System.out.println("Soy Bulbasaur y estoy usando Hoja Afilada");
    }

    @Override
    public void atacarLatigoCepa() {
        System.out.println("Soy Bulbasaur y estoy usando Latigo Cepa");
    }

    @Override
    protected void atacarPlacaje() {
        System.out.println("Soy Bulbasaur y estoy usando Placaje");
    }

    @Override
    protected void atacarAraniazo() {
        System.out.println("Soy Bulbasaur y estoy usando Araniazo");
    }

    @Override
    protected void atacarMordisco() {
        System.out.println("Soy Bulbasaur y estoy usando Mordisco");
    }
}
