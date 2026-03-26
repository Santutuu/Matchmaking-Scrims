package com.api.Scrims_Valorant.State;

public class EstadoLobbyArmado implements EstadoScrim {
    @Override
    public void armarLobby(ContextoScrim ctx) {
        System.out.println("ℹ️  Lobby ya está armado");
    }

    @Override
    public void confirmarTodos(ContextoScrim ctx) {
        if (ctx.getScrim().todasConfirmaciones()) {
            System.out.println("✅ Todos confirmados - Scrim lista para iniciar");
            ctx.setEstado(new EstadoConfirmado());
        } else {
            System.out.println("❌ Faltan confirmaciones - " + ctx.getScrim().faltantes() + " pendientes");
        }
    }

    @Override
    public void iniciar(ContextoScrim ctx) {
        System.out.println("❌ No se puede iniciar - Esperando confirmaciones");
    }

    @Override
    public void finalizar(ContextoScrim ctx) {
        System.out.println("❌ No se puede finalizar - Scrim no ha comenzado");
    }

    @Override
    public void cancelar(ContextoScrim ctx) {
        System.out.println("🛑 Scrim cancelada desde lobby armado");
        ctx.setEstado(new EstadoCancelado());
    }

    @Override
    public String getNombre() {
        return "Lobby Armado";
    }
}