package org.coworking.domain.dto;;

public class ReporteDTO {
    private String idReporte;
    private String tipo;
    private String descripcion;
    private String fechaGeneracion;
    private String contenido;
    private String idUsuario;

    public ReporteDTO() {
    }

    public ReporteDTO(String idReporte, String tipo, String descripcion, String fechaGeneracion, String contenido, String idUsuario) {
        this.idReporte = idReporte;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fechaGeneracion = fechaGeneracion;
        this.contenido = contenido;
        this.idUsuario = idUsuario;
    }

    public String getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(String idReporte) {
        this.idReporte = idReporte;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(String fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}
