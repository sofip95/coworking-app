package org.coworking.presentation.controller;

import java.util.List;
import org.coworking.domain.service.NotificacionService;
import org.coworking.domain.dto.NotificacionDTO;
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

/**
 * Controlador REST para la gestión de notificaciones.
 */

@RestController
@RequestMapping("/api/v1/notificaciones")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Notificaciones", description = "API para la gestión de notificaciones")
@CrossOrigin(origins = "*")
public class NotificacionController {

    private final NotificacionService notificacionService;

    /**
     * CREATE - Crear una nueva notificación.
     *
     * BODY: NotificacionDTO con los datos de la notificación a crear.
     * RESPUESTA: NotificacionDTO con los datos de la notificación creada.
     */
    @PostMapping
    @Operation(summary = "Crear una nueva notificación", description = "Crea una nueva notificación en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notificación creada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificacionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<NotificacionDTO> createNotificacion(
            @Parameter(description = "Datos de la notificación a crear", required = true)
            @RequestBody NotificacionDTO notificacionDTO
    ) {
        try {
            NotificacionDTO createdNotificacion = notificacionService.createNotificacion(notificacionDTO);
            log.info("Notificación creada exitosamente con ID: {}", createdNotificacion.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNotificacion);
        } catch (IllegalArgumentException e) {
            log.warn("Error al crear la notificación: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.error("Error al crear la notificación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * READ - Obtener notificación por ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener notificación por ID", description = "Devuelve los detalles de una notificación específica por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación obtenida correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<NotificacionDTO> getNotificacionById(
            @Parameter(description = "ID de la notificación a obtener", required = true, example = "1")
            @PathVariable Long id
    ) {
        try {
            NotificacionDTO notificacionDTO = notificacionService.getNotificacionById(id);
            return ResponseEntity.ok(notificacionDTO);
        } catch (RuntimeException e) {
            log.warn("Notificación no encontrada con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * READ ALL - Obtener todas las notificaciones.
     */
    @GetMapping
    @Operation(summary = "Obtener todas las notificaciones", description = "Devuelve una lista de todas las notificaciones registradas en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de notificaciones obtenida correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "No se encontraron notificaciones"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<NotificacionDTO>> getAllNotificaciones() {
        List<NotificacionDTO> notificaciones = notificacionService.getAllNotificaciones();
        log.debug("Número de notificaciones obtenidas: {}", notificaciones.size());
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * UPDATE - Actualizar notificación por ID.
     *
     * BODY: NotificacionDTO con los datos actualizados de la notificación.
     * RESPUESTA: NotificacionDTO con los datos de la notificación actualizada.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar notificación", description = "Actualiza los detalles de una notificación existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificación actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<NotificacionDTO> updateNotificacion(
            @Parameter(description = "ID de la notificación a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos de la notificación a actualizar", required = true)
            @RequestBody NotificacionDTO notificacionDTO
    ) {
        try {
            NotificacionDTO updatedNotificacion = notificacionService.updateNotificacion(id, notificacionDTO);
            log.info("Notificación actualizada exitosamente con ID: {}", id);
            return ResponseEntity.ok(updatedNotificacion);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                log.warn("Notificación no encontrada con ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            log.warn("Error al actualizar la notificación con ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * DELETE - Eliminar notificación por ID.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar notificación", description = "Elimina una notificación existente del sistema por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notificación eliminada correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> deleteNotificacion(
            @Parameter(description = "ID de la notificación a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        try {
            notificacionService.deleteNotificacion(id);
            log.info("Notificación eliminada exitosamente con ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.warn("Error al eliminar la notificación con ID {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Buscar notificaciones por usuarioId
     */
    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Buscar notificaciones por usuario", description = "Devuelve una lista de notificaciones del usuario especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificaciones obtenidas correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "No se encontraron notificaciones para el usuario"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<NotificacionDTO>> findNotificacionesByUsuarioId(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @PathVariable Long usuarioId
    ) {
        try {
            List<NotificacionDTO> notificaciones = notificacionService.findNotificacionesByUsuarioId(usuarioId);
            return ResponseEntity.ok(notificaciones);
        } catch (IllegalArgumentException e) {
            log.warn("Error en la solicitud para buscar notificaciones por usuario: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.warn("No se encontraron notificaciones para el usuario: {}", usuarioId);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Buscar notificaciones por tipo
     */
    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Buscar notificaciones por tipo", description = "Devuelve una lista de notificaciones del tipo especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificaciones obtenidas correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "No se encontraron notificaciones del tipo dado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<NotificacionDTO>> findNotificacionesByTipo(
            @Parameter(description = "Tipo de notificación", required = true, example = "CONFIRMACION")
            @PathVariable String tipo
    ) {
        try {
            List<NotificacionDTO> notificaciones = notificacionService.findNotificacionesByTipo(tipo);
            return ResponseEntity.ok(notificaciones);
        } catch (IllegalArgumentException e) {
            log.warn("Error en la solicitud para buscar notificaciones por tipo: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.warn("No se encontraron notificaciones del tipo: {}", tipo);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Buscar notificaciones por estado
     */
    @GetMapping("/estado/{estado}")
    @Operation(summary = "Buscar notificaciones por estado", description = "Devuelve una lista de notificaciones con el estado especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificaciones obtenidas correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "No se encontraron notificaciones con el estado dado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<NotificacionDTO>> findNotificacionesByEstado(
            @Parameter(description = "Estado de la notificación", required = true, example = "PENDIENTE")
            @PathVariable String estado
    ) {
        try {
            List<NotificacionDTO> notificaciones = notificacionService.findNotificacionesByEstado(estado);
            return ResponseEntity.ok(notificaciones);
        } catch (IllegalArgumentException e) {
            log.warn("Error en la solicitud para buscar notificaciones por estado: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.warn("No se encontraron notificaciones con estado: {}", estado);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Buscar notificaciones por fecha de envío
     */
    @GetMapping("/fecha/{fechaEnvio}")
    @Operation(summary = "Buscar notificaciones por fecha de envío", description = "Devuelve una lista de notificaciones enviadas en la fecha especificada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificaciones obtenidas correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "No se encontraron notificaciones para la fecha dada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<NotificacionDTO>> findNotificacionesByFechaEnvio(
            @Parameter(description = "Fecha de envío en formato ISO", required = true, example = "2023-10-19T10:30:00")
            @PathVariable String fechaEnvio
    ) {
        try {
            List<NotificacionDTO> notificaciones = notificacionService.findNotificacionesByFechaEnvio(fechaEnvio);
            return ResponseEntity.ok(notificaciones);
        } catch (IllegalArgumentException e) {
            log.warn("Error en la solicitud para buscar notificaciones por fecha: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.warn("No se encontraron notificaciones para la fecha: {}", fechaEnvio);
            return ResponseEntity.notFound().build();
        }
    }

}
