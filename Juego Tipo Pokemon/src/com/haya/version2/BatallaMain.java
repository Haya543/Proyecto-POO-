package com.haya.version2;

public class BatallaMain {
    static void main(String[] args) {
        //Creamos entrenadores
        Entrenador jugador1 = new Entrenador("Haya");
        Entrenador jugador2 = new Entrenador("Jaz");

        //Creamos los pokemonos
        Pokemon p1 = new Charmander(5);
        Pokemon p2 = new Squirtle(5);

        //Asignamos los equipos
        jugador1.agregarPokemon(p1);
        jugador2.agregarPokemon(p2);

        jugador1.cambiarPokemonActivo(0);
        jugador2.cambiarPokemonActivo(0);

    }
}
