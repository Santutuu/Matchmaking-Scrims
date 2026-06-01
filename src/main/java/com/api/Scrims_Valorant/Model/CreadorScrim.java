package com.api.Scrims_Valorant.Model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CREADOR")
public class CreadorScrim extends Usuario {

    public CreadorScrim() {
        super();
    }

    public CreadorScrim(String username, String email, String passHash) {
        super((Long) null, username, email, passHash, null);
    }

    public CreadorScrim(int id, String username, String email, String passHash) {
        super(id, username, email, passHash, null);
    }

    @Override
    public boolean puedeCrearScrim() {
        return true;
    }
}