package com.haya.ejemplo;

public class TestPokemon {
    static void main(String[] args) {

        Squirtle squirtle = new Squirtle();
        Charmander charmander = new Charmander();
        Bulbasaur bulbasaur = new Bulbasaur();
        Pikachu pikachu = new Pikachu();

        squirtle.atacarAraniazo();
        squirtle.atacarHidroBomba();

        charmander.atacarAraniazo();
        charmander.atacarLanzaLlamas();

        bulbasaur.atacarAraniazo();
        bulbasaur.atacarDrenaje();

        pikachu.atacarAraniazo();
        pikachu.atacarImpacTrueno();


    }
}
