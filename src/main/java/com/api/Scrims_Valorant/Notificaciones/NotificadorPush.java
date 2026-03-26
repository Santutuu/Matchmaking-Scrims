// NotificadorPush.java
package com.api.Scrims_Valorant.Notificaciones;
import com.api.Scrims_Valorant.Adapter.IPushAdapter;

public class NotificadorPush extends Notificador {
    private String canal = "push";
    private IPushAdapter adapter;

    public NotificadorPush(IPushAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public String canalNombre() {
        return canal;
    }

    @Override
    public void enviar(Notificacion notificacion) {
        adapter.enviarPush(
                notificacion.getDestinatario(), // token en este caso
                notificacion.getAsunto(),       // título
                notificacion.getCuerpo(),       // cuerpo
                notificacion.getDataAdicional() // data adicional
        );
    }
}