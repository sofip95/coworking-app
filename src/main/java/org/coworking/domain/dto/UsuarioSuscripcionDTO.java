package org.coworking.domain.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para asignar o actualizar la suscripción de un usuario
 * ¿POR QUÉ ESTE DTO?
 * - Se usa cuando ASIGNAMOS o ACTUALIZAMOS la suscripción de un usuario
 * - Contiene solo los campos necesarios para esta operación específica
 * - Evita exponer información innecesaria o sensible
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de la suscripción del usuario")
public class UsuarioSuscripcionDTO {

    @Schema(description = "ID único de la asignación de suscripción", example = "1", required = true)
    private Long id;

    @Schema(description = "ID del usuario al que se le asigna la suscripción", example = "1", required = true)
    private Long usuarioId;

    @Schema(description = "ID de la suscripción asignada", example = "2", required = true)
    private Long suscripcionId;

    @Schema(description = "Estado de la suscripción", example = "ACTIVA", required = true, allowableValues = {"ACTIVA", "VENCIDA", "CANCELADA"})
    private String estado;

    @Schema(description = "Fecha de inicio de la suscripción", example = "2025-01-01T00:00:00")
    private LocalDateTime fechaInicio;

    @Schema(description = "Fecha de fin de la suscripción", example = "2025-12-31T23:59:59")
    private LocalDateTime fechaFin;

    @Schema(description = "Fecha de creación de la asignación", example = "2025-01-01T00:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de última actualización de la asignación", example = "2025-06-01T12:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

}
