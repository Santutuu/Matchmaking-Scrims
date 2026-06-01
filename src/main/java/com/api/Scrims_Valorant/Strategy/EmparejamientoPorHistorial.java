package com.api.Scrims_Valorant.Strategy;
import com.api.Scrims_Valorant.Model.*;
import java.util.List;
import com.api.Scrims_Valorant.Strategy.EstrategiaEmparejamiento;

public class EmparejamientoPorHistorial implements EstrategiaEmparejamiento {

    // Constructores
    public EmparejamientoPorHistorial() {}

    // Métodos de la interfaz
    public boolean validarElegibilidad(Usuario usuario, Scrim scrim) { return false; }

    public List<Usuario> emparejar(List<Usuario> candidatos, Scrim scrim) { return null; }

    // Métodos específicos
    public boolean calcularConfianza(Usuario usuario, int partidosRequeridos) { return false; }
    public boolean evaluarConducta(Usuario usuario) { return false; }
}