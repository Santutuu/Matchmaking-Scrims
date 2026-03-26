package com.api.Scrims_Valorant.Model;

import java.util.List;
import java.util.ArrayList;

public class Estadistica {
    private int idEstadistica;
    private Scrim scrim;
    private int MVP;
    private String KDA;
    private String observaciones;
    private List<EstadisticaJugador> estadisticasJugadores;

    // Constructor
    public Estadistica(int idEstadistica, Scrim scrim, int MVP, String KDA, String observaciones) {
        this.idEstadistica = idEstadistica;
        this.scrim = scrim;
        this.MVP = MVP;
        this.KDA = KDA;
        this.observaciones = observaciones;
        this.estadisticasJugadores = new ArrayList<>();
    }

    // Constructor vacío
    public Estadistica() {
        this.estadisticasJugadores = new ArrayList<>();
    }

    // Getters
    public int getIdEstadistica() {
        return idEstadistica;
    }

    public Scrim getScrim() {
        return scrim;
    }

    public int isMVP() {
        return MVP;
    }

    public String getKDA() {
        return KDA;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public List<EstadisticaJugador> getEstadisticasJugadores() {
        return estadisticasJugadores;
    }

    // Setters
    public void setIdEstadistica(int idEstadistica) {
        this.idEstadistica = idEstadistica;
    }

    public void setScrim(Scrim scrim) {
        this.scrim = scrim;
    }

    public void setMVP(int MVP) {
        this.MVP = MVP;
    }

    public void setKDA(String KDA) {
        this.KDA = KDA;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setEstadisticasJugadores(List<EstadisticaJugador> estadisticasJugadores) {
        this.estadisticasJugadores = estadisticasJugadores;
    }

    // Métodos auxiliares
    public void agregarEstadisticaJugador(EstadisticaJugador estadisticaJugador) {
        this.estadisticasJugadores.add(estadisticaJugador);
    }
}