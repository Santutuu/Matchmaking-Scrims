
package com.api.Scrims_Valorant.Adapter;

public class SendGridAdapter implements IEmailAdapter {
    @Override
    public void enviarEmail(String to, String asunto, String cuerpo) {
        // Implementación real con SendGrid API
        System.out.println("Enviando email con SendGrid a: " + to);
        System.out.println("Asunto: " + asunto);
        System.out.println("Cuerpo: " + cuerpo);
        // Lógica de integración con SendGrid aquí
    }
}