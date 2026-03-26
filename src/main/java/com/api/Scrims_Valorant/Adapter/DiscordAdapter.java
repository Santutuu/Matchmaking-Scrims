package com.api.Scrims_Valorant.Adapter;


public class DiscordAdapter implements IDiscordAdapter {
    @Override
    public void enviarWebhook(String url, String contenido) {
        // Implementación real con Discord Webhook
        System.out.println("Enviando mensaje a Discord webhook: " + url);
        System.out.println("Contenido: " + contenido);
        // Lógica de integración con Discord Webhook aquí
    }
}