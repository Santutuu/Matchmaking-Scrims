package com.api.Scrims_Valorant.State;

public class EstadoBuscando implements EstadoScrim {
    @Override
    public void armarLobby(ContextoScrim ctx) {
        if (ctx.getScrim().cuposLlenos()) {
            System.out.println("✅ Lobby armado - Todos los cupos llenos");
            ctx.setEstado(new EstadoLobbyArmado());
        } else {
            System.out.println("❌ No se puede armar lobby - Cupos incompletos");
        }
    }

    @Override
    public void confirmarTodos(ContextoScrim ctx) {
        System.out.println("❌ No se puede confirmar - Primero armar lobby");
    }

    @Override
    public void iniciar(ContextoScrim ctx) {
        System.out.println("❌ No se puede iniciar - Scrim en búsqueda de jugadores");
    }

    @Override
    public void finalizar(ContextoScrim ctx) {
        System.out.println("❌ No se puede finalizar - Scrim no ha comenzado");
    }

    @Override
    public void cancelar(ContextoScrim ctx) {
        System.out.println("🛑 Scrim cancelada");
        ctx.setEstado(new EstadoCancelado());
    }

    @Override
    public String getNombre() {
        return "Buscando Jugadores";
    }
}








