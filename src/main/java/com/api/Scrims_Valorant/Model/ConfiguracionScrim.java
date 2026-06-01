package com.api.Scrims_Valorant.Model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.api.Scrims_Valorant.Strategy.EmparejamientoPorHistorial;
import com.api.Scrims_Valorant.Strategy.EmparejamientoPorLatencia;
import com.api.Scrims_Valorant.Strategy.EmparejamientoPorRango;
import com.api.Scrims_Valorant.Strategy.EstrategiaEmparejamiento;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;

@Embeddable
public class ConfiguracionScrim {
    private String region;
    private String rangeMin;
    private String rangeMax;
    private int latenciaMax;
    private LocalDateTime fechaHoraInicio;
    private int duration;
    private int maxJugadores;

    @Enumerated(EnumType.STRING)
    private TipoEstrategiaEmparejamiento tipoEstrategia;

    @Transient
    private EstrategiaEmparejamiento estrategiaEmparejamiento;

    private static final List<String> ORDEN_RANGOS = Arrays.asList(
            "HIERRO", "PLATA", "ORO", "PLATINO", "MAESTRO", "GRANMAESTRO"
    );

    public ConfiguracionScrim(String region, String rangeMin, String rangeMax,
                              int latenciaMax, LocalDateTime fechaHoraInicio,
                              int duration, int maxJugadores,
                              TipoEstrategiaEmparejamiento tipoEstrategia) {
        this.region = region;
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
        this.latenciaMax = latenciaMax;
        this.fechaHoraInicio = fechaHoraInicio;
        this.duration = duration;
        this.maxJugadores = maxJugadores;
        this.tipoEstrategia = tipoEstrategia;
    }

    public ConfiguracionScrim() {
    }

    public LocalDateTime calcularHoraFin() {
        return fechaHoraInicio.plusMinutes(duration);
    }

    public boolean esRangoValido(String rangoJugador) {
        if (rangeMin == null || rangeMax == null || rangoJugador == null) {
            return false;
        }

        int indexJugador = ORDEN_RANGOS.indexOf(rangoJugador.toUpperCase());
        int indexMin = ORDEN_RANGOS.indexOf(rangeMin.toUpperCase());
        int indexMax = ORDEN_RANGOS.indexOf(rangeMax.toUpperCase());

        if (indexJugador == -1 || indexMin == -1 || indexMax == -1) {
            return false;
        }

        return indexJugador >= indexMin && indexJugador <= indexMax;
    }

    public boolean esConfiguracionValida() {
        return region != null && !region.isEmpty()
                && fechaHoraInicio != null
                && fechaHoraInicio.isAfter(LocalDateTime.now())
                && maxJugadores > 0
                && duration > 0
                && latenciaMax > 0
                && tipoEstrategia != null;
    }

    public int getCuposMaximos() {
        return maxJugadores;
    }

    public int getMinimoJugadores() {
        return Math.max(2, maxJugadores / 2);
    }

    public EstrategiaEmparejamiento getEstrategiaEmparejamiento() {
        if (estrategiaEmparejamiento == null) {
            estrategiaEmparejamiento = reconstruirEstrategia();
        }
        return estrategiaEmparejamiento;
    }

    public EstrategiaEmparejamiento reconstruirEstrategia() {
        if (tipoEstrategia == null) {
            return null;
        }

        return switch (tipoEstrategia) {
            case RANGO -> new EmparejamientoPorRango();
            case LATENCIA -> new EmparejamientoPorLatencia();
            case HISTORIAL -> new EmparejamientoPorHistorial();
        };
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRangeMin() {
        return rangeMin;
    }

    public void setRangeMin(String rangeMin) {
        this.rangeMin = rangeMin;
    }

    public String getRangeMax() {
        return rangeMax;
    }

    public void setRangeMax(String rangeMax) {
        this.rangeMax = rangeMax;
    }

    public int getLatenciaMax() {
        return latenciaMax;
    }

    public void setLatenciaMax(int latenciaMax) {
        this.latenciaMax = latenciaMax;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getMaxJugadores() {
        return maxJugadores;
    }

    public void setMaxJugadores(int maxJugadores) {
        this.maxJugadores = maxJugadores;
    }

    public TipoEstrategiaEmparejamiento getTipoEstrategia() {
        return tipoEstrategia;
    }

    public void setTipoEstrategia(TipoEstrategiaEmparejamiento tipoEstrategia) {
        this.tipoEstrategia = tipoEstrategia;
        this.estrategiaEmparejamiento = null;
    }

    public void setEstrategiaEmparejamiento(EstrategiaEmparejamiento estrategiaEmparejamiento) {
        this.estrategiaEmparejamiento = estrategiaEmparejamiento;
    }
}