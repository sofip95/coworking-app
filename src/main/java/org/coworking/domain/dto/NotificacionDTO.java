package org.coworking.domain.dto;

public class NotificacionDTO {
    private String idNotificacion;
    private String mensaje;
    private String tipo;
    private String fechaCreacion;
    private String estado;
    private String idUsuario;

    public NotificacionDTO() {
    }

    public NotificacionDTO(String idNotificacion, String mensaje, String tipo, String fechaCreacion, String estado, String idUsuario) {
        this.idNotificacion = idNotificacion;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.idUsuario = idUsuario;
    }

    public String getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(String idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
    
}
