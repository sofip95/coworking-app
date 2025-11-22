package org.coworking.presentation.controller;

import org.coworking.domain.service.SuscripcionService;
import org.coworking.domain.dto.SuscripcionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Controlador REST para la gestión de suscripciones.
 */
@RestController
@RequestMapping("/api/v1/suscripciones")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Suscripciones", description = "API para la gestión de suscripciones")
@CrossOrigin(origins = "*")
public class SuscripcionController {

    private final SuscripcionService suscripcionService;

    /**
     * CREATE - Crear una nueva suscripción.
     *
     * BODY: SuscripcionDTO con los datos de la suscripción a crear.
     * RESPUESTA: SuscripcionDTO con los datos de la suscripción creada.
     */
    @PostMapping
    @Operation(summary = "Crear una nueva suscripción", description = "Crea una nueva suscripción en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Suscripción creada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuscripcionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados"),
            @ApiResponse(responseCode = "404", description = "Suscripción no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<SuscripcionDTO> createSuscripcion(
            @Parameter(description = "Datos de la suscripción a crear", required = true)
            @RequestBody SuscripcionDTO suscripcionDTO
    ) {
        try {
            SuscripcionDTO createdSuscripcion = suscripcionService.createSuscripcion(suscripcionDTO);
            log.info("Suscripción creada exitosamente con ID: {}", createdSuscripcion.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSuscripcion);
        } catch (IllegalArgumentException e) {
            log.warn("Error al crear la suscripción: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.error("Error al crear la suscripción: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * READ - Obtener suscripción por ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener suscripción por ID", description = "Devuelve los detalles de una suscripción específica por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suscripción obtenida correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuscripcionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Suscripción no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<SuscripcionDTO> getSuscripcionById(
            @Parameter(description = "ID de la suscripción a obtener", required = true, example = "1")
            @PathVariable Long id
    ) {
        try {
            SuscripcionDTO suscripcionDTO = suscripcionService.getSuscripcionById(id);
            return ResponseEntity.ok(suscripcionDTO);
        } catch (RuntimeException e) {
            log.warn("Suscripción no encontrada con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * READ ALL - Obtener todas las suscripciones.
     */
    @GetMapping
    @Operation(summary = "Obtener todas las suscripciones", description = "Devuelve una lista de todas las suscripciones registradas en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de suscripciones obtenida correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "No se encontraron suscripciones"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor"
            )
    })
    public ResponseEntity<List<SuscripcionDTO>> getAllSuscripciones() {
        List<SuscripcionDTO> suscripciones = suscripcionService.getAllSuscripciones();
        return ResponseEntity.ok(suscripciones);
    }

    /**
     * UPDATE - Actualizar suscripción por ID.
     *
     * BODY: SuscripcionDTO con los datos actualizados de la suscripción.
     * RESPUESTA: SuscripcionDTO con los datos de la suscripción actualizada.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar suscripción", description = "Actualiza los detalles de una suscripción existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suscripción actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados"),
            @ApiResponse(responseCode = "404", description = "Suscripción no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<SuscripcionDTO> updateSuscripcion(
            @Parameter(description = "ID de la suscripción a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos de la suscripción a actualizar", required = true)
            @RequestBody SuscripcionDTO suscripcionDTO
    ) {
        try {
            SuscripcionDTO updatedSuscripcion = suscripcionService.updateSuscripcion(id, suscripcionDTO);
            log.info("Suscripción actualizada exitosamente con ID: {}", id);
            return ResponseEntity.ok(updatedSuscripcion);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                log.warn("Suscripción no encontrada con ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            log.warn("Error al actualizar la suscripción con ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * DELETE - Eliminar suscripción por ID.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar suscripción", description = "Elimina una suscripción existente del sistema por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Suscripción eliminada correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Suscripción no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> deleteSuscripcion(
            @Parameter(description = "ID de la suscripción a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        try {
            suscripcionService.deleteSuscripcion(id);
            log.info("Suscripción eliminada exitosamente con ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.warn("Error al eliminar la suscripción con ID {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Buscar suscripciones por tipo
     */
    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Buscar suscripciones por tipo", description = "Devuelve una lista de suscripciones que coinciden con el tipo dado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suscripciones obtenidas correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "No se encontraron suscripciones con el tipo dado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<SuscripcionDTO>> findSuscripcionesByTipo(
            @Parameter(description = "Tipo de suscripción a buscar", required = true, example = "BASICA")
            @PathVariable String tipo
    ) {
        try {
            List<SuscripcionDTO> suscripciones = suscripcionService.findSuscripcionesByTipo(tipo);
            return ResponseEntity.ok(suscripciones);
        } catch (IllegalArgumentException e) {
            log.warn("Error en la solicitud para buscar suscripciones por tipo: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.warn("No se encontraron suscripciones con tipo: {}", tipo);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Buscar suscripciones por duración
     */
    @GetMapping("/duracion/{duracion}")
    @Operation(summary = "Buscar suscripciones por duración", description = "Devuelve una lista de suscripciones que coinciden con la duración dada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suscripciones obtenidas correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "No se encontraron suscripciones con la duración dada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<SuscripcionDTO>> findSuscripcionesByDuracion(
            @Parameter(description = "Duración de suscripción a buscar", required = true, example = "ANUAL")
            @PathVariable String duracion
    ) {
        try {
            List<SuscripcionDTO> suscripciones = suscripcionService.findSuscripcionesByDuracion(duracion);
            return ResponseEntity.ok(suscripciones);
        } catch (IllegalArgumentException e) {
            log.warn("Error en la solicitud para buscar suscripciones por duración: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.warn("No se encontraron suscripciones con duración: {}", duracion);
            return ResponseEntity.notFound().build();
        }
    }

}
