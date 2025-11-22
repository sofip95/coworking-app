package org.coworking.presentation.controller;

import org.coworking.domain.dto.RecursoDTO;
import org.coworking.domain.service.RecursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controlador REST para operaciones de recursos.
 */
@RestController
@RequestMapping("/api/v1/recursos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Recursos", description = "Operaciones CRUD y filtrado de recursos disponibles")
@CrossOrigin(origins = "*")
public class RecursoController {

    private final RecursoService recursoService;

    // -------------------------- CREATE --------------------------
    @PostMapping
    @Operation(summary = "Crear nuevo recurso", description = "Crea un nuevo recurso en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recurso creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecursoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o conflicto de nombre"),
            @ApiResponse(responseCode = "404", description = "No aplica (solo para búsquedas)"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> createRecurso(@RequestBody RecursoDTO recursoDTO) {
        try {
            if (recursoDTO == null || recursoDTO.getNombre() == null || recursoDTO.getNombre().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre del recurso es obligatorio");
            }
            RecursoDTO created = recursoService.createRecurso(recursoDTO);
            return ResponseEntity.status(HttpStatus.OK).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error interno al crear recurso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    // -------------------------- SEARCH BY ID --------------------------
    @GetMapping("/{id}")
    @Operation(summary = "Buscar recurso por ID", description = "Obtiene la información completa de un recurso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recurso encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RecursoDTO.class))),
            @ApiResponse(responseCode = "400", description = "ID inválido"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> getRecursoById(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID proporcionado no es válido");
            }
            RecursoDTO recurso = recursoService.getRecursoById(id);
            if (recurso == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recurso no encontrado con ID: " + id);
            }
            return ResponseEntity.ok(recurso);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recurso no encontrado con ID: " + id);
        } catch (Exception e) {
            log.error("Error interno al obtener recurso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    // -------------------------- GENERAL FILTER --------------------------
    @GetMapping
    @Operation(summary = "Filtrar y listar recursos", description = """
        Obtiene la lista de recursos filtrando por uno o varios parámetros opcionales:
        - estado (DISPONIBLE, OCUPADO, MANTENIMIENTO)
        - tipo
        - ubicación
        - capacidad mínima
        - precio máximo por hora
        Si no se especifican filtros, devuelve todos los recursos.
    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de recursos obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros de búsqueda inválidos"),
            @ApiResponse(responseCode = "404", description = "No se encontraron recursos con los filtros especificados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> getFilteredRecursos(
            @Parameter(description = "Estado del recurso (DISPONIBLE, OCUPADO, MANTENIMIENTO)")
            @RequestParam(name = "estado", required = false) String estado,
            @Parameter(description = "Tipo de recurso (por ejemplo: sala, oficina, escritorio)")
            @RequestParam(name = "tipo", required = false) String tipo,
            @Parameter(description = "Ubicación del recurso")
            @RequestParam(name = "ubicacion", required = false) String ubicacion,
            @Parameter(description = "Capacidad mínima requerida")
            @RequestParam(name = "capacidadMinima", required = false) Integer capacidadMinima,
            @Parameter(description = "Precio máximo por hora")
            @RequestParam(name = "precioMaximo", required = false) BigDecimal precioMaximo) {

        try {
            if (capacidadMinima != null && capacidadMinima < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La capacidad mínima no puede ser negativa");
            }
            List<RecursoDTO> recursos = recursoService.getFilteredRecursos(estado, tipo, ubicacion, capacidadMinima, precioMaximo);
            if (recursos == null || recursos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron recursos con los filtros especificados");
            }
            return ResponseEntity.ok(recursos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parámetros de búsqueda inválidos: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error interno al obtener recursos filtrados", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    // -------------------------- UPDATE --------------------------
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar recurso", description = "Actualiza la información de un recurso existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recurso actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> updateRecurso(@PathVariable Long id, @RequestBody RecursoDTO recursoDTO) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID proporcionado no es válido");
            }
            RecursoDTO updated = recursoService.updateRecurso(id, recursoDTO);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recurso no encontrado con ID: " + id);
        } catch (Exception e) {
            log.error("Error interno al actualizar recurso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    // -------------------------- DELETE --------------------------
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar recurso", description = "Elimina un recurso del sistema por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recurso eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID inválido"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> deleteRecurso(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID proporcionado no es válido");
            }
            recursoService.deleteRecurso(id);
            return ResponseEntity.ok("Recurso eliminado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recurso no encontrado con ID: " + id);
        } catch (Exception e) {
            log.error("Error interno al eliminar recurso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }
}
