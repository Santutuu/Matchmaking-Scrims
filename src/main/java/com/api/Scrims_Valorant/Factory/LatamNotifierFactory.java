package com.api.Scrims_Valorant.Factory;
import com.api.Scrims_Valorant.Notificaciones.Notificador;

public class LatamNotifierFactory extends FabricaNotificadores {
    public com.api.Scrims_Valorant.Notificaciones.Notificador crearEmail() {
        return null;
    }

    public com.api.Scrims_Valorant.Notificaciones.Notificador crearPush() {
        return null;
    }

    public com.api.Scrims_Valorant.Notificaciones.Notificador crearDiscord() {
        return null;
    }
}