package com.api.Scrims_Valorant.Model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

@Entity
@DiscriminatorValue("JUGADOR")
public class Jugador extends Usuario {

    @Transient
    private RolUsuarioScrim rolUsuarioScrim;

    public Jugador() {
        super();
    }

    public Jugador(String username, String email, String passHash, Rango rango) {
        super((Long) null, username, email, passHash, rango);
    }

    public Jugador(int id, String username, String email, String passHash,
                   RolUsuarioScrim rolUsuarioScrim,
                   Rango rango) {
        super(id, username, email, passHash, rango);
        this.rolUsuarioScrim = rolUsuarioScrim;
    }

    public RolUsuarioScrim getRolUsuarioScrim() {
        return rolUsuarioScrim;
    }

    public void setRolUsuarioScrim(RolUsuarioScrim rolUsuarioScrim) {
        this.rolUsuarioScrim = rolUsuarioScrim;
    }

    public RolUsuarioScrim getRolJugado() {
        return rolUsuarioScrim;
    }

    @Override
    public boolean puedeCrearScrim() {
        return false;
    }
}