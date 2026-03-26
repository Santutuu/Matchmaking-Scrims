package com.api.Scrims_Valorant.Model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class Rango {
    @Enumerated(EnumType.STRING)
    private Division division;

    public Rango(Division division) {
        this.division = division;
    }

    public Rango() {
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public int toNumerico() {
        if (division == null) {
            return 0;
        }

        return switch (division) {
            case HIERRO -> 1;
            case PLATA -> 2;
            case ORO -> 3;
            case PLATINO -> 4;
            case MAESTRO -> 5;
            case GRANMAESTRO -> 6;
        };
    }
}