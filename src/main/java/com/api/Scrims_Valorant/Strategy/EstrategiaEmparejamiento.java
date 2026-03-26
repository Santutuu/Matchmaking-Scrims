package com.api.Scrims_Valorant.Strategy;
import com.api.Scrims_Valorant.Model.*;

import java.util.List;

public interface EstrategiaEmparejamiento {
    public boolean validarElegibilidad(Usuario usuario, Scrim scrim, String rolDeseado);
    public List<Usuario> emparejar(List<Usuario> candidatos, Scrim scrim);
}