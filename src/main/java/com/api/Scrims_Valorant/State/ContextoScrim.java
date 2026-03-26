package com.api.Scrims_Valorant.State;

import com.api.Scrims_Valorant.Model.Scrim;

public class ContextoScrim {
    private EstadoScrim estadoActual;
    private Scrim scrim;

    public ContextoScrim(Scrim scrim) {
        this.scrim = scrim;
        this.estadoActual = new EstadoBuscando(); // Estado inicial
    }

    public void setEstado(EstadoScrim estado) {
        EstadoScrim estadoAnterior = this.estadoActual;
        this.estadoActual = estado;

        // Actualizar estado en la scrim
        if (scrim != null) {
            scrim.setEstadoNombre(estado.getNombre());
        }

        System.out.println("🔄 Scrim " + scrim.getIdScrim() +
                " cambió de " + estadoAnterior.getNombre() +
                " a " + estado.getNombre());
    }

    public void armarLobby() {
        estadoActual.armarLobby(this);
    }

    public void confirmarTodos() {
        estadoActual.confirmarTodos(this);
    }

    public void iniciar() {
        estadoActual.iniciar(this);
    }

    public void finalizar() {
        estadoActual.finalizar(this);
    }

    public void cancelar() {
        estadoActual.cancelar(this);
    }

    // Getters
    public Scrim getScrim() { return scrim; }
    public EstadoScrim getEstadoActual() { return estadoActual; }
}