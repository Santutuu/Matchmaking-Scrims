package com.api.Scrims_Valorant.State;

public class EstadoFinalizado implements EstadoScrim {
    @Override
    public void armarLobby(ContextoScrim ctx) {
    }

    @Override
    public void confirmarTodos(ContextoScrim ctx) {
    }

    @Override
    public void iniciar(ContextoScrim ctx) {
    }

    @Override
    public void finalizar(ContextoScrim ctx) {
    }

    @Override
    public void cancelar(ContextoScrim ctx) {
    }

    @Override
    public String getNombre() {
        return "Estado finalizado";
    }
}