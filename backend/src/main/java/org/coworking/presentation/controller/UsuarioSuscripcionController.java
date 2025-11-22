package org.coworking.presentation.controller;

import java.util.List;
import org.coworking.domain.service.UsuarioSuscripcionService;
import org.coworking.domain.dto.UsuarioSuscripcionDTO;
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
 * Controlador REST para la gestión de usuario-suscripción.
 */

@RestController
@RequestMapping("/api/v1/usuario_suscripciones")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Usuario_Suscripciones", description = "API para la gestión de usuario-suscripciones")
@CrossOrigin(origins = "*")
public class UsuarioSuscripcionController {

    private final UsuarioSuscripcionService usuarioSuscripcionService;

    /**
     * CREATE - Crear una nueva usuario-suscripción.
     *
     * BODY: UsuarioSuscripcionDTO con los datos de la usuario-suscripción a crear.
     * RESPUESTA: UsuarioSuscripcionDTO con los datos de la usuario-suscripción creada.
     */
    @PostMapping
    @Operation(summary = "Crear una nueva usuario-suscripción", description = "Crea una nueva usuario-suscripción en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario-suscripción creada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioSuscripcionDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos proporcionados"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario-suscripción no encontrada"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<UsuarioSuscripcionDTO> createUsuarioSuscripcion(
            @Parameter(description = "Datos de la usuario-suscripción a crear", required = true)
            @RequestBody UsuarioSuscripcionDTO usuarioSuscripcionDTO
    ) {
        log.info("POST /api/usuario-suscripciones - Crear nueva usuario-suscripción: {}", usuarioSuscripcionDTO);

        try {
            UsuarioSuscripcionDTO createdUsuarioSuscripcion = usuarioSuscripcionService.createUsuarioSuscripcion(usuarioSuscripcionDTO);
            log.info("Usuario-suscripción creada exitosamente con ID: {}", createdUsuarioSuscripcion.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUsuarioSuscripcion);
        } catch (IllegalArgumentException e) {
            log.warn("Error al crear la usuario-suscripción: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.error("Error al crear la usuario-suscripción: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * READ - Obtener usuario-suscripción por ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario-suscripción por ID", description = "Devuelve los detalles de una usuario-suscripción específica por su ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario-suscripción obtenida correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioSuscripcionDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario-suscripción no encontrada"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<UsuarioSuscripcionDTO> getUsuarioSuscripcionById(
            @Parameter(description = "ID de la usuario-suscripción a obtener", required = true, example = "1")
            @PathVariable Long id
    ) {
        log.debug("GET /api/usuario-suscripciones/{} - Obtener usuario-suscripción por ID", id);

        try {
            UsuarioSuscripcionDTO usuarioSuscripcionDTO = usuarioSuscripcionService.getUsuarioSuscripcionById(id);
            return ResponseEntity.ok(usuarioSuscripcionDTO);
        } catch (RuntimeException e) {
            log.warn("Usuario-suscripción no encontrada con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * READ ALL - Obtener todas las usuario-suscripciones.
     */
    @GetMapping
    @Operation(summary = "Obtener todas las usuario-suscripciones", description = "Devuelve una lista de todas las usuario-suscripciones registradas en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de usuario-suscripciones obtenida correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UsuarioSuscripcionDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontraron usuario-suscripciones"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<List<UsuarioSuscripcionDTO>> getAllUsuarioSuscripciones() {
        log.debug("GET /api/usuario-suscripciones - Obtener todas las usuario-suscripciones");

        List<UsuarioSuscripcionDTO> usuarioSuscripciones = usuarioSuscripcionService.getAllUsuarioSuscripciones();
        log.debug("Número de usuario-suscripciones obtenidas: {}", usuarioSuscripciones.size());
        return ResponseEntity.ok(usuarioSuscripciones);
    }

    /**
     * UPDATE - Actualizar usuario-suscripción por ID.
     *
     * BODY: UsuarioSuscripcionDTO con los datos actualizados de la usuario-suscripción.
     * RESPUESTA: UsuarioSuscripcionDTO con los datos de la usuario-suscripción actualizada.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario-suscripción", description = "Actualiza los detalles de una usuario-suscripción existente.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario-suscripción actualizada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioSuscripcionDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos proporcionados"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario-suscripción no encontrada"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<UsuarioSuscripcionDTO> updateUsuarioSuscripcion(
            @Parameter(description = "ID de la usuario-suscripción a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos de la usuario-suscripción a actualizar", required = true)
            @RequestBody UsuarioSuscripcionDTO usuarioSuscripcionDTO
    ) {
        log.info("PUT /api/usuario-suscripciones/{} - Actualizar usuario-suscripción: {}", id);

        try {
            UsuarioSuscripcionDTO updatedUsuarioSuscripcion = usuarioSuscripcionService.updateUsuarioSuscripcion(id, usuarioSuscripcionDTO);
            log.info("Usuario-suscripción actualizada exitosamente con ID: {}", id);
            return ResponseEntity.ok(updatedUsuarioSuscripcion);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                log.warn("Usuario-suscripción no encontrada con ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            log.warn("Error al actualizar la usuario-suscripción con ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * DELETE - Eliminar usuario-suscripción por ID.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario-suscripción", description = "Elimina una usuario-suscripción existente del sistema por su ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuario-suscripción eliminada correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario-suscripción no encontrada"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<Void> deleteUsuarioSuscripcion(
            @Parameter(description = "ID de la usuario-suscripción a eliminar", required = true, example = "1")
            @PathVariable Long id
    ) {
        log.info("DELETE /api/usuario-suscripciones/{} - Eliminar usuario-suscripción", id);

        try {
            usuarioSuscripcionService.deleteUsuarioSuscripcion(id);
            log.info("Usuario-suscripción eliminada exitosamente con ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.warn("Error al eliminar la usuario-suscripción con ID {}", id);
            return ResponseEntity.notFound().build();
        }
    }

}
