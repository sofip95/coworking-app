package org.coworking.domain.dto;

public class ReservaDTO {
    private String idReserva;
    private String idUsuario;
    private String idRecurso;
    private String fechaInicio;
    private String fechaFin;
    private String estado;

    public ReservaDTO() {
    }

    public ReservaDTO(String idReserva, String idUsuario, String idRecurso, String fechaInicio, String fechaFin, String estado) {
        this.idReserva = idReserva;
        this.idUsuario = idUsuario;
        this.idRecurso = idRecurso;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

    public String getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdRecurso() {
        return idRecurso;
    }

    public void setIdRecurso(String idRecurso) {
        this.idRecurso = idRecurso;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
