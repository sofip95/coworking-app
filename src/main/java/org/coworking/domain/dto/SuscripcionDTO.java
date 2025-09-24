package org.coworking.domain.dto;

public class SuscripcionDTO {
    private String idSuscripcion;
    private String tipo;
    private String estado;
    private String fechaInicio;
    private String fechaFin;
    private String idUsuario;

    public SuscripcionDTO() {
    }

    public SuscripcionDTO(String idSuscripcion, String tipo, String estado, String fechaInicio, String fechaFin, String idUsuario) {
        this.idSuscripcion = idSuscripcion;
        this.tipo = tipo;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.idUsuario = idUsuario;
    }

    public String getIdSuscripcion() {
        return idSuscripcion;
    }

    public void setIdSuscripcion(String idSuscripcion) {
        this.idSuscripcion = idSuscripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}
