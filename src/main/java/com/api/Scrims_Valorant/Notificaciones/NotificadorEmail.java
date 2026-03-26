package com.api.Scrims_Valorant.Notificaciones;
import com.api.Scrims_Valorant.Adapter.IEmailAdapter;

public class NotificadorEmail extends Notificador {
    private String canal = "email";
    private IEmailAdapter adapter;

    public NotificadorEmail(String canal, IEmailAdapter adapter) {
        this.canal = canal;
        this.adapter = adapter;
    }

    @Override
    public String canalNombre() {
        return canal;
    }

    @Override
    public void enviar(Notificacion notificacion) {
        adapter.enviarEmail(
                notificacion.getDestinatario(),
                notificacion.getAsunto(),
                notificacion.getCuerpo()
        );
    }
}