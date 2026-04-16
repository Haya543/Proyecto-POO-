package com.haya.ejemplo;

public class Squirtle extends Pokemon implements IAgua{

    public Squirtle() {
    }

    @Override
    public void atacarHidroBomba() {
        System.out.println("Soy Squirtle y estoy usando Hidro Bomba");
    }

    @Override
    public void atacarBurbuja() {
        System.out.println("Soy Squirtle y estoy usando Burbuja");
    }

    @Override
    public void atacarPistolaAgua() {
        System.out.println("Soy Squirtle y estoy usando Pistola Agua");
    }

    @Override
    public void atacarHidroPulso() {
        System.out.println("Soy Squirtle y estoy usando Hidro Pulso");
    }

    @Override
    protected void atacarPlacaje() {
        System.out.println("Soy Squirtle y estoy usando Placaje");
    }

    @Override
    protected void atacarAraniazo() {
        System.out.println("Soy Squirtle y estoy usando Araniazo");
    }

    @Override
    protected void atacarMordisco() {
        System.out.println("Soy Squirtle y estoy usando Mordisco");
    }
}
