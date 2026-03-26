package com.api.Scrims_Valorant.Model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "confirmaciones")
public class Confirmacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConfirmacion;

    @Column(nullable = false)
    private boolean confirmado;

    @Column(nullable = false)
    private LocalDate fecha;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "scrim_id")
    private Scrim scrim;

    public Confirmacion() {
    }

    public Confirmacion(boolean confirmado, LocalDate fecha, Usuario usuario, Scrim scrim) {
        this.confirmado = confirmado;
        this.fecha = fecha;
        this.usuario = usuario;
        this.scrim = scrim;
    }

    public void aceptar() {
        this.confirmado = true;
    }

    public void rechazar() {
        this.confirmado = false;
    }

    public Long getIdConfirmacion() {
        return idConfirmacion;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public void setConfirmado(boolean confirmado) {
        this.confirmado = confirmado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Scrim getScrim() {
        return scrim;
    }

    public void setScrim(Scrim scrim) {
        this.scrim = scrim;
    }
}