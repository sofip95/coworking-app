package org.coworking.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO COMPLETO de suscripciones - Para LECTURA/RESPUESTAS
 *
 * ¿POR QUÉ ESTE DTO?
 * - Se usa cuando DEVOLVEMOS información completa al cliente
 * - Contiene campos auto-generados (ID, fechas) que el cliente necesita ver
 * - Optimiza la respuesta incluyendo datos relacionados en una sola consulta
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de la suscripción")
public class SuscripcionDTO {

    @Schema(description = "ID único de la suscripción", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nombre de la suscripción", example = "BASICA MENSUAL", required = true)
    private String nombre;

    @Schema(description = "Descripcion de la suscripción", example = "Suscripción Básica")
    private String descripcion;

    @Schema(description = "Tipo de suscripción", example = "BASICA", required = true, allowableValues = {"BASICA", "PREMIUM"})
    private String tipo;

    @Schema(description = "Duración de la suscripción", example = "MENSUAL", required = true, allowableValues = {"MENSUAL", "TRIMESTRAL", "SEMESTRAL", "ANUAL"})
    private String duracion;

    @Schema(description = "Precio de la suscripción", example = "100000.00", required = true, minimum = "0.00")
    private BigDecimal precio;

    @Schema(description = "Cantidad de reservas permitidas", example = "10", required = true, minimum = "0")
    private Integer cantidadReservasPermitidas;

    @Schema(description = "Fecha y hora de creación del registro", example = "2025-01-01T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de última actualización", example = "2025-01-15T15:45:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

}
