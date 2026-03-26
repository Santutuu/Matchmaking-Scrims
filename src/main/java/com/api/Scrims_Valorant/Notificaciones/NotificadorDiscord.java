package com.api.Scrims_Valorant.Notificaciones;
import com.api.Scrims_Valorant.Adapter.IDiscordAdapter;

public class NotificadorDiscord extends Notificador {
    private String canal = "discord";
    private IDiscordAdapter adapter;
    private String webhookUrl;

    public NotificadorDiscord(IDiscordAdapter adapter, String webhookUrl) {
        this.adapter = adapter;
        this.webhookUrl = webhookUrl;
    }

    @Override
    public String canalNombre() {
        return canal;
    }

    @Override
    public void enviar(Notificacion notificacion) {
        String mensaje = String.format("**%s**\n%s",
                notificacion.getAsunto(),
                notificacion.getCuerpo());

        adapter.enviarWebhook(webhookUrl, mensaje);
    }
}