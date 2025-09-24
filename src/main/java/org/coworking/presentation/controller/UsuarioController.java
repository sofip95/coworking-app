package org.coworking.presentation.controller;

import java.util.List;

import org.coworking.domain.service.UsuarioService;
import org.coworking.domain.dto.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin/usuarios")
@Tag(name = "Usuarios", description = "API para la gestión de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios registrados en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        return null;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Devuelve los detalles de un usuario específico por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario obtenido correctamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable @Parameter (description = "ID del usuario a obtener") String idUsuario) {
        return null;
    }
    
    @PostMapping
    @Operation(summary = "Crear nuevo usuario", description = "Crea un nuevo usuario en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<UsuarioDTO> createUsuario(@RequestBody @Parameter (description = "Datos del usuario a crear") UsuarioDTO usuarioDTO) {
        return null;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza los detalles de un usuario existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable String idUsuario, @RequestBody UsuarioDTO usuarioDTO) {
        return null;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> deleteUsuario(@PathVariable String idUsuario) {
        return null;
    }
    
}
