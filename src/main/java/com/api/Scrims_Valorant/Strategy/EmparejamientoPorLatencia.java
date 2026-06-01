package com.api.Scrims_Valorant.Strategy;
import com.api.Scrims_Valorant.Model.*;
import java.util.List;

public class EmparejamientoPorLatencia implements EstrategiaEmparejamiento {

    // Constructores
    public EmparejamientoPorLatencia() {}

    // Métodos de la interfaz
    public boolean validarElegibilidad(Usuario usuario, Scrim scrim) { return false; }
    public List<Usuario> emparejar(List<Usuario> candidatos, Scrim scrim) { return null; }

    // Métodos específicos
    public boolean diagnosticarConexion(Usuario usuario, int latenciaMaxima) { return false; }
}