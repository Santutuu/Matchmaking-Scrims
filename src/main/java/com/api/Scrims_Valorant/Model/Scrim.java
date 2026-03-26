package com.api.Scrims_Valorant.Model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "scrims")
public class Scrim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idScrim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoScrimTipo estadoActual = EstadoScrimTipo.BUSCANDO;

    @Embedded
    private ConfiguracionScrim config;

    @ManyToOne(optional = false)
    @JoinColumn(name = "creador_id")
    private Usuario creador;

    @OneToMany(mappedBy = "scrim", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Postulacion> postulaciones = new ArrayList<>();

    @OneToMany(mappedBy = "scrim", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Confirmacion> confirmaciones = new ArrayList<>();

    public Scrim() {
    }

    public Scrim(ConfiguracionScrim config, Usuario creador) {
        this.config = config;
        this.creador = creador;
        this.estadoActual = EstadoScrimTipo.BUSCANDO;
    }

    // Compatibilidad con constructor del flujo legado
    public Scrim(int idScrim, String estadoNombre, ConfiguracionScrim config) {
        this.idScrim = (long) idScrim;
        this.estadoActual = mapEstado(estadoNombre);
        this.config = config;
    }

    public boolean cuposLlenos() {
        if (postulaciones == null || config == null) {
            return false;
        }
        return postulaciones.size() >= config.getMaxJugadores();
    }

    public int faltantes() {
        if (postulaciones == null || config == null) {
            return 0;
        }
        return Math.max(0, config.getMaxJugadores() - postulaciones.size());
    }

    public boolean todasConfirmaciones() {
        if (confirmaciones == null || config == null) {
            return false;
        }
        return confirmaciones.size() >= config.getMaxJugadores();
    }

    public boolean tieneCupoPara(String rol) {
        return !cuposLlenos();
    }

    public void agregarPostulacion(Postulacion postulacion) {
        if (postulacion == null) {
            return;
        }
        postulaciones.add(postulacion);
        postulacion.setScrim(this);
    }

    public int calcularJugadoresConfirmados() {
        if (confirmaciones == null) {
            return 0;
        }
        return confirmaciones.size();
    }

    public boolean puedeIniciar() {
        return estadoActual == EstadoScrimTipo.CONFIRMADO && todasConfirmaciones();
    }

    public int getIdScrim() {
        return idScrim != null ? idScrim.intValue() : 0;
    }

    public Long getIdScrimLong() {
        return idScrim;
    }

    public void setIdScrim(Long idScrim) {
        this.idScrim = idScrim;
    }

    // Compatibilidad con código previo
    public void setIdScrim(int idScrim) {
        this.idScrim = (long) idScrim;
    }

    public EstadoScrimTipo getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(EstadoScrimTipo estadoActual) {
        this.estadoActual = estadoActual;
    }

    // Compatibilidad con capa previa de String
    public String getEstadoNombre() {
        return estadoActual != null ? estadoActual.name() : null;
    }

    public void setEstadoNombre(String estadoNombre) {
        this.estadoActual = mapEstado(estadoNombre);
    }

    public ConfiguracionScrim getConfig() {
        return config;
    }

    public void setConfig(ConfiguracionScrim config) {
        this.config = config;
    }

    public Usuario getCreador() {
        return creador;
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
    }

    public List<Postulacion> getPostulaciones() {
        return postulaciones;
    }

    public void setPostulaciones(List<Postulacion> postulaciones) {
        this.postulaciones = postulaciones;
    }

    public List<Confirmacion> getConfirmaciones() {
        return confirmaciones;
    }

    public void setConfirmaciones(List<Confirmacion> confirmaciones) {
        this.confirmaciones = confirmaciones;
    }

    private EstadoScrimTipo mapEstado(String estadoNombre) {
        if (estadoNombre == null || estadoNombre.isBlank()) {
            return EstadoScrimTipo.BUSCANDO;
        }

        String normalizado = estadoNombre.trim().toUpperCase().replace(" ", "_");
        return switch (normalizado) {
            case "BUSCANDO", "BUSCANDOJUGADORES", "BUSCANDO_JUGADORES" -> EstadoScrimTipo.BUSCANDO;
            case "LOBBY_ARMADO", "LOBBYARMADO" -> EstadoScrimTipo.LOBBY_ARMADO;
            case "CONFIRMADO", "ESTADO_CONFIRMADO" -> EstadoScrimTipo.CONFIRMADO;
            case "EN_JUEGO", "ESTADO_EN_JUEGO" -> EstadoScrimTipo.EN_JUEGO;
            case "FINALIZADO", "ESTADO_FINALIZADO" -> EstadoScrimTipo.FINALIZADO;
            case "CANCELADO", "ESTADO_CANCELADO" -> EstadoScrimTipo.CANCELADO;
            default -> EstadoScrimTipo.BUSCANDO;
        };
    }
}