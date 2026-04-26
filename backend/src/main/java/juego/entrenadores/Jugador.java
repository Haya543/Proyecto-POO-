package juego.entrenadores;

import juego.criaturas.Criatura;
import juego.esferas.EsferaNormal;
import juego.esferas.PokeEsfera;
import juego.objetos.ObjetoCuracion;
import juego.objetos.Objeto;

import java.util.ArrayList;
import java.util.List;

// The human player. Manages bag, spheres, money, medals, and captured creatures.
public class Jugador extends Entrenador {

    private List<Objeto> bolsa;
    private List<PokeEsfera> esferas;
    private int dinero;
    private int medallas;
    private List<Criatura> capturadas;

    public Jugador(String nombre, Criatura starter) {
        super(nombre);
        this.bolsa = new ArrayList<>();
        this.esferas = new ArrayList<>();
        this.capturadas = new ArrayList<>();
        this.dinero = 500;
        this.medallas = 0;

        agregarCriatura(starter);

        // Starting items
        bolsa.add(ObjetoCuracion.pocion());
        bolsa.add(ObjetoCuracion.superPocion());

        for (int i = 0; i < 5; i++) {
            esferas.add(new EsferaNormal());
        }
    }

    // Uses an item from the bag on a creature; item tracks its own quantity
    public String usarObjeto(int indiceObjeto, Criatura c) {
        if (indiceObjeto < 0 || indiceObjeto >= bolsa.size()) return "Objeto invalido";
        Objeto obj = bolsa.get(indiceObjeto);
        if (!obj.tieneStock()) return obj.getNombre() + " sin stock";
        obj.usar(c);
        return "Usaste " + obj.getNombre() + " en " + c.getNombre();
    }

    // Throws a sphere at a wild creature; removes one sphere on use regardless of outcome
    public boolean lanzarEsfera(int indiceEsfera, Criatura c) {
        if (indiceEsfera < 0 || indiceEsfera >= esferas.size()) return false;
        PokeEsfera esfera = esferas.get(indiceEsfera);
        boolean exito = esfera.lanzar(c);
        esferas.remove(indiceEsfera);
        if (exito) {
            capturadas.add(c);
            if (equipo.size() < 6) equipo.add(c);
        }
        return exito;
    }

    public void ganarMedalla() { medallas++; }
    public void ganarDinero(int cantidad) { dinero += cantidad; }

    // Heals every creature in the team to full HP and restores all move PP
    public void curarEquipo() {
        for (Criatura c : equipo) {
            c.curarCompleto();
        }
    }

    public List<Objeto> getBolsa() { return bolsa; }
    public List<PokeEsfera> getEsferas() { return esferas; }
    public int getDinero() { return dinero; }
    public int getMedallas() { return medallas; }
    public List<Criatura> getCapturadas() { return capturadas; }
}
