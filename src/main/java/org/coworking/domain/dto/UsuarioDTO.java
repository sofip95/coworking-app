package org.coworking.domain.dto;

public class UsuarioDTO {
    private String idUsuario;
    private String nombre;
    private String email;
    private String rol;
    private String estado;
    private String telefono;

    public UsuarioDTO() {}

    public UsuarioDTO(String idUsuario, String nombre, String email, String rol, String estado, String telefono) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.estado = estado;
        this.telefono = telefono;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }   

}
