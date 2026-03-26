package com.api.Scrims_Valorant.Model;

public class ReporteConducta {
    private int idReporteConducta;
    private Scrim scrim;
    private Usuario reportado;
    private String motivo;
    private String estado;
    private String sancion;

    // Constructor
    public ReporteConducta(int idReporteConducta, Scrim scrim, Usuario reportado,
                           String motivo, String estado, String sancion) {
        this.idReporteConducta = idReporteConducta;
        this.scrim = scrim;
        this.reportado = reportado;
        this.motivo = motivo;
        this.estado = estado;
        this.sancion = sancion;
    }

    // Constructor vacío
    public ReporteConducta() {
    }

    // Getters
    public int getIdReporteConducta() {
        return idReporteConducta;
    }

    public Scrim getScrim() {
        return scrim;
    }

    public Usuario getReportado() {
        return reportado;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getEstado() {
        return estado;
    }

    public String getSancion() {
        return sancion;
    }

    // Setters
    public void setIdReporteConducta(int idReporteConducta) {
        this.idReporteConducta = idReporteConducta;
    }

    public void setScrim(Scrim scrim) {
        this.scrim = scrim;
    }

    public void setReportado(Usuario reportado) {
        this.reportado = reportado;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setSancion(String sancion) {
        this.sancion = sancion;
    }

    // Métodos auxiliares
    public boolean estaPendiente() {
        return "PENDIENTE".equals(estado);
    }

    public boolean estaResuelto() {
        return "RESUELTO".equals(estado);
    }

    public boolean tieneSancion() {
        return sancion != null && !sancion.isEmpty();
    }
}