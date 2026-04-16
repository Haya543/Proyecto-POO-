package com.haya.ejemplo;

public class Charmander extends Pokemon implements IFuego{

    public Charmander() {
    }

    @Override
    public void atacarPunioFuego() {
        System.out.println("Soy Charmander y estoy usando Punio Fuego");
    }

    @Override
    public void atacarLanzaLlamas() {
        System.out.println("Soy Charmander y estoy usando Lanza Llamas");
    }

    @Override
    public void atacarAcuas() {
        System.out.println("Soy Charmander y estoy usando Ascuas");
    }

    @Override
    protected void atacarPlacaje() {
        System.out.println("Soy Charmander y estoy usando Placaje");
    }

    @Override
    protected void atacarAraniazo() {
        System.out.println("Soy Charmander y estoy usando Araniazo");
    }

    @Override
    protected void atacarMordisco() {
        System.out.println("Soy Charmander y estoy usando Mordisco");
    }
}
