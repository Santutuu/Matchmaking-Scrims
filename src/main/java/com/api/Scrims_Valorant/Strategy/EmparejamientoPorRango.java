package com.api.Scrims_Valorant.Strategy;
import com.api.Scrims_Valorant.Strategy.EstrategiaEmparejamiento;
import com.api.Scrims_Valorant.Model.*;


import java.util.List;

public class EmparejamientoPorRango implements EstrategiaEmparejamiento {

    // Constructores
    public EmparejamientoPorRango() {}

    // Métodos de la interfaz
    public boolean validarElegibilidad(Usuario usuario, Scrim scrim, String rolDeseado) { return false; }
    public List<Usuario> emparejar(List<Usuario> candidatos, Scrim scrim) { return null; }

    // Métodos específicos
    public boolean estaEnRango(String rangoJugador, String rangoMax, String rangoMin) { return false; }
}