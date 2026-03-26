
package com.api.Scrims_Valorant.Notificaciones;

public class Notificacion {
    private String destinatario;
    private String asunto;
    private String cuerpo;
    private String dataAdicional;

    public Notificacion(String destinatario, String asunto, String cuerpo) {
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.cuerpo = cuerpo;
    }

    public Notificacion(String destinatario, String asunto, String cuerpo, String dataAdicional) {
        this(destinatario, asunto, cuerpo);
        this.dataAdicional = dataAdicional;
    }

    // Getters
    public String getDestinatario() { return destinatario; }
    public String getAsunto() { return asunto; }
    public String getCuerpo() { return cuerpo; }
    public String getDataAdicional() { return dataAdicional; }
}