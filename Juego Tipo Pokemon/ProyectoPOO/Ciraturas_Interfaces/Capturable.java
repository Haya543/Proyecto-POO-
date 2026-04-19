package ProyectoPOO.Ciraturas_Interfaces;

import ProyectoPOO.Enums_LogicaCentral.TipoElemento;

public interface Capturable {
    double detTasaCaptura();
    TipoElemento getTipo();
    int getHpActual();
    int getHpMax();
}
