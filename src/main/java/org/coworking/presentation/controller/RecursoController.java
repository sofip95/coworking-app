package org.coworking.presentation.controller;

import java.util.List;
import org.coworking.domain.service.impl.RecursoServiceImpl;
import org.coworking.domain.dto.RecursoDTO;
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
@RequestMapping("/api/recursos")
@Tag(name = "Recursos", description = "API para la gestión de recursos")
public class RecursoController {

    private final RecursoServiceImpl recursoService;

    @Autowired
    public RecursoController(RecursoServiceImpl recursoService) {
        this.recursoService = recursoService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los recursos", description = "Devuelve una lista de todos los recursos disponibles en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de recursos obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<RecursoDTO>> getAllRecursos() {
        return null;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener recurso por ID", description = "Devuelve los detalles de un recurso específico por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recurso obtenido correctamente"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<RecursoDTO> getRecursoById(@PathVariable @Parameter (description = "ID del recurso a obtener") String idRecurso) {
        return null;
    }
    
    @PostMapping
    @Operation(summary = "Crear nuevo recurso", description = "Crea un nuevo recurso en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Recurso creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<RecursoDTO> createRecurso(@RequestBody @Parameter (description = "Datos del recurso a crear") RecursoDTO recursoDTO) {
        return null;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar recurso", description = "Actualiza los detalles de un recurso existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recurso actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<RecursoDTO> updateRecurso(@PathVariable String idRecurso, @RequestBody RecursoDTO recursoDTO) {
        return null;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar recurso", description = "Elimina un recurso existente del sistema por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Recurso eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> deleteRecurso(@PathVariable String idRecurso) {
        return null;
    }

}
