package com.haya.ejemplo;

public class Pikachu extends Pokemon implements IElectrico{

    public Pikachu() {
    }

    @Override
    public void atacarImpacTrueno() {
        System.out.println("Soy Pikachu y estoy usando Impac Trueno");
    }

    @Override
    public void atacarPunioTrueno() {
        System.out.println("Soy Pikachu y estoy usando Punio Trueno");
    }

    @Override
    public void atacarRayo() {
        System.out.println("Soy Pikachu y estoy usando Rayo");
    }

    @Override
    public void atacarRayoCarga() {
        System.out.println("Soy Pikachu y estoy usando Rayo Carga");
    }

    @Override
    protected void atacarPlacaje() {
        System.out.println("Soy Pikachu y estoy usando Placaje");
    }

    @Override
    protected void atacarAraniazo() {
        System.out.println("Soy Pikachu y estoy usando Araniazo");
    }

    @Override
    protected void atacarMordisco() {
        System.out.println("Soy Pikachu y estoy usando Mordisco");
    }
}
