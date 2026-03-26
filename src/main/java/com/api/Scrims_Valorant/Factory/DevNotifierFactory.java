package com.api.Scrims_Valorant.Factory;
import com.api.Scrims_Valorant.Notificaciones.Notificador;
import com.api.Scrims_Valorant.Factory.FabricaNotificadores;

public class DevNotifierFactory extends FabricaNotificadores {
    public Notificador crearEmail() {
        return null;
    }

    public Notificador crearPush() {
        return null;
    }

    public Notificador crearDiscord() {
        return null;
    }
}