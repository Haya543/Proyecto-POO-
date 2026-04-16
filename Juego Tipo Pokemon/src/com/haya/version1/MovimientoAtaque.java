package com.haya.version1;

public class MovimientoAtaque extends Movimiento{
   protected double precision;

    @Override
    public void usar(Pokemon usuario, Pokemon objetivo) {
        if(!tienePP()){
            System.out.println("No quedan PP de " + nombre);
            return;
        }
        pp--;

        //calculo de daño

        double efectividad = objetivo.getTipo().getMultiplicador(this.tipo);
        int daño = (int) ( ( (usuario.getAtaque() * this.poder) / objetivo.getDefensa() * efectividad ) );

        System.out.println(usuario.getNombre() + " usa " + nombre + " y hace " + daño + " de daño!");
    }
}
