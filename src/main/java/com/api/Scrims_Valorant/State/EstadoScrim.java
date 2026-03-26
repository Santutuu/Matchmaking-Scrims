package com.api.Scrims_Valorant.State;

public interface EstadoScrim {
    void armarLobby(ContextoScrim ctx);
    void confirmarTodos(ContextoScrim ctx);
    void iniciar(ContextoScrim ctx);
    void finalizar(ContextoScrim ctx);
    void cancelar(ContextoScrim ctx);
    String getNombre();
}