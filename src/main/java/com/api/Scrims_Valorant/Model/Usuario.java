package com.api.Scrims_Valorant.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_usuario")
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long idUsuario;

    @Column(nullable = false, unique = true)
    protected String username;

    @Column(nullable = false, unique = true)
    protected String email;

    @Column(nullable = false)
    protected String passwordHash;

    @Embedded
    protected Rango rango;

    @Column(nullable = false)
    protected LocalDateTime fechaCreacion;

    @Column(nullable = false)
    protected LocalDateTime fechaActualizacion;

    public Usuario(Long idUsuario, String username, String email, String passwordHash, Rango rango) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.rango = rango;
    }

    public Usuario(int idUsuario, String username, String email, String passwordHash, Rango rango) {
        this((long) idUsuario, username, email, passwordHash, rango);
    }

    public Usuario() {}

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public int getIdUsuario() { 
        return idUsuario != null ? idUsuario.intValue() : 0; 
    }

    public Long getIdUsuarioLong() { 
        return idUsuario; 
    }

    public String getUsername() { 
        return username; 
    }

    public String getEmail() { 
        return email; 
    }

    public String getPasswordHash() { 
        return passwordHash; 
    }

    public Rango getRango() {
        return rango;
    }

    public LocalDateTime getFechaCreacion() { 
        return fechaCreacion; 
    }

    public LocalDateTime getFechaActualizacion() { 
        return fechaActualizacion; 
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setRango(Rango rango) {
        this.rango = rango;
    }

    public abstract boolean puedeCrearScrim();
}