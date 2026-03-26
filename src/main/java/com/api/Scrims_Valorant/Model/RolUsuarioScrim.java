package com.api.Scrims_Valorant.Model;

public interface RolUsuarioScrim {
    void crearScrim(Usuario usuario);
    void postularse(Usuario usuario, Scrim scrim);
    void administrarScrim(Usuario usuario, Scrim scrim);
    void cancelarScrim(Usuario usuario, Scrim scrim);  // ← agregado
}