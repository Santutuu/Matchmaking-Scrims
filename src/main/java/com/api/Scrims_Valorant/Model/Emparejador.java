package com.api.Scrims_Valorant.Model;

import com.api.Scrims_Valorant.Strategy.EstrategiaEmparejamiento;
import java.util.List;

public class Emparejador {
    private EstrategiaEmparejamiento estrategia;

    // Constructores
    public Emparejador() {}
    public Emparejador(EstrategiaEmparejamiento estrategia) {}

    // Getters y Setters
    public EstrategiaEmparejamiento getEstrategia() { return estrategia; }
    public void setEstrategia(EstrategiaEmparejamiento estrategia) { this.estrategia = estrategia; }

    // Métodos (solo declaraciones)
    public boolean esElegible(Postulacion postulacion, Scrim scrim) { return false; }
    public List<Postulacion> seleccionarPostulaciones(Scrim scrim) { return null; }
    public List<List<Usuario>> crearEquiposBalanceados(List<Postulacion> postulaciones) { return null; }
    public List<Usuario> emparejar(List<Usuario> candidatos, Scrim scrim) { return null; }

}