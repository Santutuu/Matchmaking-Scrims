package com.api.Scrims_Valorant.Notificaciones;

public abstract class Notificador {
    public abstract String canalNombre();
    public abstract void enviar(Notificacion notificacion);
}