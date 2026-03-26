package com.api.Scrims_Valorant.Model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

@Entity
@DiscriminatorValue("JUGADOR")
public class Jugador extends Usuario {

    @Embedded
    private Rango rango;

    @Transient
    private RolUsuarioScrim rolUsuarioScrim;

    public Jugador() {
    }

    public Jugador(String username, String email, String passHash, Rango rango) {
        super(null, username, email, passHash);
        this.rango = rango;
    }

    public Jugador(int id, String username, String email, String passHash,
                   RolUsuarioScrim rolUsuarioScrim,
                   Rango rango) {

        super(id, username, email, passHash);
        this.rolUsuarioScrim = rolUsuarioScrim;
        this.rango = rango;
    }

    public Rango getRango() { return rango; }

    public void setRango(Rango rango) {
        this.rango = rango;
    }

    public RolUsuarioScrim getRolUsuarioScrim() {
        return rolUsuarioScrim;
    }

    public void setRolUsuarioScrim(RolUsuarioScrim rolUsuarioScrim) {
        this.rolUsuarioScrim = rolUsuarioScrim;
    }

    // Compatibilidad con código previo
    public RolUsuarioScrim getRolJugado() {
        return rolUsuarioScrim;
    }

    @Override
    public boolean puedeCrearScrim() {
        return false;
    }
}
