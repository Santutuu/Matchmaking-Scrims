package com.api.Scrims_Valorant.Strategy;

import java.util.List;

import com.api.Scrims_Valorant.Model.Scrim;
import com.api.Scrims_Valorant.Model.Usuario;

public class EmparejamientoPorRango implements EstrategiaEmparejamiento {

    public EmparejamientoPorRango() {
    }

    @Override
    public boolean validarElegibilidad(Usuario usuario, Scrim scrim) {
        if (usuario == null || scrim == null || scrim.getConfig() == null) {
            return false;
        }

        if (usuario.getRango() == null || usuario.getRango().getDivision() == null) {
            return false;
        }

        String rangoJugador = usuario.getRango().getDivision().name();
        return scrim.getConfig().esRangoValido(rangoJugador);
    }

    @Override
    public List<Usuario> emparejar(List<Usuario> candidatos, Scrim scrim) {
        return candidatos;
    }
}