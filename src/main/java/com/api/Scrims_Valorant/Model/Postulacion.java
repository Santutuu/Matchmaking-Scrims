package com.api.Scrims_Valorant.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "postulaciones")
public class Postulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPostulacion;

    @Column(nullable = false)
    private String rolDeseado;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private String estadoNombre;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "scrim_id")
    private Scrim scrim;

    public Postulacion() {
    }

    public Postulacion(String rolDeseado, LocalDateTime fecha, String estadoNombre, Usuario usuario, Scrim scrim) {
        this.rolDeseado = rolDeseado;
        this.fecha = fecha;
        this.estadoNombre = estadoNombre;
        this.usuario = usuario;
        this.scrim = scrim;
    }

    // Compatibilidad con código legado
    public Postulacion(int idPostulacion, String rolDeseado, LocalDateTime fecha, String estadoNombre, Usuario usuario, Scrim scrim) {
        this.idPostulacion = (long) idPostulacion;
        this.rolDeseado = rolDeseado;
        this.fecha = fecha;
        this.estadoNombre = estadoNombre;
        this.usuario = usuario;
        this.scrim = scrim;
    }

    public int getIdPostulacion() {
        return idPostulacion != null ? idPostulacion.intValue() : 0;
    }

    public Long getIdPostulacionLong() {
        return idPostulacion;
    }

    public void setIdPostulacion(Long idPostulacion) {
        this.idPostulacion = idPostulacion;
    }

    // Compatibilidad con código legado
    public void setIdPostulacion(int idPostulacion) {
        this.idPostulacion = (long) idPostulacion;
    }

    public String getRolDeseado() { return rolDeseado; }
    public void setRolDeseado(String rolDeseado) { this.rolDeseado = rolDeseado; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getEstadoNombre() { return estadoNombre; }
    public void setEstadoNombre(String estadoNombre) { this.estadoNombre = estadoNombre; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Scrim getScrim() { return scrim; }
    public void setScrim(Scrim scrim) { this.scrim = scrim; }

    public void aceptada() {
        this.estadoNombre = "ACEPTADA";
    }

    public void rechazada() {
        this.estadoNombre = "RECHAZADA";
    }

    public boolean isActiva() {
        return "ACTIVA".equalsIgnoreCase(estadoNombre) || "PENDIENTE".equalsIgnoreCase(estadoNombre);
    }

    public boolean isAceptada() {
        return "ACEPTADA".equalsIgnoreCase(estadoNombre);
    }

    public boolean puedeSerProcesada() {
        return isActiva();
    }
}