package com.api.Scrims_Valorant.Strategy;

import java.util.List;

import com.api.Scrims_Valorant.Model.Scrim;
import com.api.Scrims_Valorant.Model.Usuario;

public interface EstrategiaEmparejamiento {
    boolean validarElegibilidad(Usuario usuario, Scrim scrim);
    List<Usuario> emparejar(List<Usuario> candidatos, Scrim scrim);
}