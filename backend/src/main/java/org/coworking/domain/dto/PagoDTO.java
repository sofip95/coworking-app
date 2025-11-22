package org.coworking.domain.dto;

public class PagoDTO {
    private String idPago;
    private double monto;
    private String estado;
    private String referencia;
    private String idUsuario;
    private String idReserva;

    public PagoDTO() {
    }

    public PagoDTO(String idPago, double monto, String estado, String referencia, String idUsuario, String idReserva) {
        this.idPago = idPago;
        this.monto = monto;
        this.estado = estado;
        this.referencia = referencia;
        this.idUsuario = idUsuario;
        this.idReserva = idReserva;
    }

    public String getIdPago() {
        return idPago;
    }

    public void setIdPago(String idPago) {
        this.idPago = idPago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }   
}
