package com.api.Scrims_Valorant.Model;

public class EstadisticaJugador {
    private int idEstadisticaJugador;
    private int kills;
    private int deaths;
    private int assists;
    private int dashboard;
    private Usuario usuario;

    // Constructor
    public EstadisticaJugador(int idEstadisticaJugador, int kills, int deaths, int assists, int dashboard, Usuario usuario) {
        this.idEstadisticaJugador = idEstadisticaJugador;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.dashboard = dashboard;
        this.usuario = usuario;
    }

    // Constructor vacío
    public EstadisticaJugador() {
    }

    // Getters
    public int getIdEstadisticaJugador() {
        return idEstadisticaJugador;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getAssists() {
        return assists;
    }

    public int getDashboard() {
        return dashboard;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    // Setters
    public void setIdEstadisticaJugador(int idEstadisticaJugador) {
        this.idEstadisticaJugador = idEstadisticaJugador;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public void setDashboard(int dashboard) {
        this.dashboard = dashboard;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    // Método para calcular KDA
    public double calcularKDA() {
        return deaths == 0 ? (kills + assists) : (double) (kills + assists) / deaths;
    }
}