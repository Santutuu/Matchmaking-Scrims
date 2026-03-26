package com.api.Scrims_Valorant.Observer;

public interface IObservable {
    void agregar(IObserver observer);
    void eliminar(IObserver observer);
    void notificar();
}