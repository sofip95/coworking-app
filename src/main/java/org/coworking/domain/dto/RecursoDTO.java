package org.coworking.domain.dto;

public class RecursoDTO {
    private String idRecurso;
    private String nombre;
    private String tipo;
    private String descripcion;
    private int capacidad;
    private String estado;

    public RecursoDTO() {
    }

    public RecursoDTO(String idRecurso, String nombre, String tipo, String descripcion, int capacidad, String estado) {
        this.idRecurso = idRecurso;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.capacidad = capacidad;
        this.estado = estado;
    }

    public String getIdRecurso() {
        return idRecurso;
    }

    public void setIdRecurso(String idRecurso) {
        this.idRecurso = idRecurso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }   
}
