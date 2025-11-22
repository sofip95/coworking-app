package org.coworking.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO COMPLETO de notificaciones - Para LECTURA/RESPUESTAS
 *
 * ¿POR QUÉ ESTE DTO?
 * - Se usa cuando DEVOLVEMOS información completa al cliente
 * - Contiene campos auto-generados (ID, fechas) que el cliente necesita ver
 * - Optimiza la respuesta incluyendo datos relacionados en una sola consulta
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de la notificación")
public class NotificacionDTO {

    @Schema(description = "ID único de la notificación", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID del usuario al que se envía la notificación", example = "1", required = true)
    private Long usuarioId;

    @Schema(description = "Tipo de notificación", example = "CONFIRMACIÓN", required = true, allowableValues = {"CONFIRMACION", "RECORDATORIO", "CANCELACION"})
    private String tipo;

    @Schema(description = "Título de la notificación", example = "Reserva Confirmada", required = true, maxLength = 150)
    private String titulo;

    @Schema(description = "Mensaje de la notificación", example = "Su reserva para la Sala de Reuniones A ha sido confirmada para el 2025-01-15 de 09:00 a 17:00.", required = true)
    private String mensaje;

    @Schema(description = "Estado de la notificación", example = "PENDIENTE", required = true, allowableValues = {"PENDIENTE", "ENVIADA", "FALLIDA", "LEIDA"})
    private String estado;

    @Schema(description = "Fecha y hora de envío de la notificación", example = "2025-01-15T08:00:00")
    private LocalDateTime fechaEnvio;

    @Schema(description = "Fecha y hora de creación del registro", example = "2025-01-15T07:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de última actualización", example = "2025-01-15T08:15:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

}
