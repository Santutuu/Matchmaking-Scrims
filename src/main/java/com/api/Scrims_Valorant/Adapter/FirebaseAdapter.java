
package com.api.Scrims_Valorant.Adapter;

public class FirebaseAdapter implements IPushAdapter {
    @Override
    public void enviarPush(String token, String titulo, String cuerpo, String data) {
        // Implementación real con Firebase Cloud Messaging
        System.out.println("Enviando push con Firebase a token: " + token);
        System.out.println("Título: " + titulo);
        System.out.println("Cuerpo: " + cuerpo);
        System.out.println("Data: " + data);
        // Lógica de integración con FCM aquí
    }
}