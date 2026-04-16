package com.haya.version1;

import java.util.ArrayList;
import java.util.List;

public class Entrenador {
    private String nombre;
    private List<Pokemon> equipo;
    private int pokemonActivo;

    public Entrenador(String nombre) {
        this.nombre = nombre;
        this.equipo = new ArrayList<>();
        this.pokemonActivo = 0;
    }

    public void añadirPokemon(Pokemon p){
        if(equipo.size() < 6){
            equipo.add(p);
        }
    }

    public Pokemon getPokemonActivo(){
        return equipo.get(pokemonActivo);
    }

    public void cambiarPokemon(int indice){
        if(indice >= 0 && indice < equipo.size() && !equipo.get(indice).estaDerrotado()){
            pokemonActivo = indice;
            System.out.println("¡Vamos " + equipo.get(indice).getNombre() + "!");
        }
    }

}
