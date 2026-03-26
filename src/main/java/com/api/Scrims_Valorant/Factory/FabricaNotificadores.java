package com.api.Scrims_Valorant.Factory;
import com.api.Scrims_Valorant.Notificaciones.Notificador;

public abstract class FabricaNotificadores {
        public abstract com.api.Scrims_Valorant.Notificaciones.Notificador crearEmail();
        public abstract com.api.Scrims_Valorant.Notificaciones.Notificador crearPush();
        public abstract com.api.Scrims_Valorant.Notificaciones.Notificador crearDiscord();
    }
