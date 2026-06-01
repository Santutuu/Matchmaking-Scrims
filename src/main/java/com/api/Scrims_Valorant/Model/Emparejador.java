package com.api.Scrims_Valorant.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.api.Scrims_Valorant.Strategy.EstrategiaEmparejamiento;

public class Emparejador {

    private EstrategiaEmparejamiento estrategia;

    public Emparejador() {
    }

    public Emparejador(EstrategiaEmparejamiento estrategia) {
        this.estrategia = estrategia;
    }

    public EstrategiaEmparejamiento getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(EstrategiaEmparejamiento estrategia) {
        this.estrategia = estrategia;
    }

    public boolean esElegible(Postulacion postulacion, Scrim scrim) {
        if (postulacion == null || postulacion.getUsuario() == null || estrategia == null) {
            return false;
        }

        return estrategia.validarElegibilidad(postulacion.getUsuario(), scrim);
    }

    public List<Postulacion> seleccionarPostulaciones(Scrim scrim) {
        if (scrim == null || scrim.getPostulaciones() == null || estrategia == null) {
            return new ArrayList<>();
        }

        return scrim.getPostulaciones().stream()
                .filter(Postulacion::isAceptada)
                .filter(postulacion -> esElegible(postulacion, scrim))
                .collect(Collectors.toList());
    }

    public List<List<Usuario>> crearEquiposBalanceados(List<Postulacion> postulaciones) {
        return new ArrayList<>();
    }

    public List<Usuario> emparejar(List<Usuario> candidatos, Scrim scrim) {
        if (estrategia == null) {
            return new ArrayList<>();
        }

        return estrategia.emparejar(candidatos, scrim);
    }
}