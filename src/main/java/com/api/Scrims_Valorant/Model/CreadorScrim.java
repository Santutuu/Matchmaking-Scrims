package com.api.Scrims_Valorant.Model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CREADOR")
public class CreadorScrim extends Usuario {

    public CreadorScrim() {
    }

    public CreadorScrim(int id, String username, String email, String passHash) {
        super(id, username, email, passHash);
    }

    public CreadorScrim(String username, String email, String passHash) {
        super(null, username, email, passHash);
    }

    @Override
    public boolean puedeCrearScrim() {
        return true;
    }
}
