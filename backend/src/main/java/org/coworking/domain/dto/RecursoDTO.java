package org.coworking.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO COMPLETO de recursos - Para LECTURA y RESPUESTAS
 *
 * ¿POR QUÉ ESTE DTO?
 * - Representa los datos que se envían y reciben del cliente.
 * - Se usa para operaciones CRUD en los endpoints de recursos.
 * - Incluye campos auto-generados (ID, fechas) solo de lectura.
 *
 * USO:
 * - GET /api/v1/recursos
 * - GET /api/v1/recursos/{id}
 * - POST /api/v1/recursos
 * - PUT /api/v1/recursos/{id}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información del recurso disponible en el coworking")
public class RecursoDTO {

    @Schema(description = "ID único del recurso", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nombre del recurso", example = "Sala de reuniones principal", required = true, maxLength = 100)
    private String nombre;

    @Schema(description = "Descripción detallada del recurso", example = "Sala equipada con proyector, pizarra y aire acondicionado", maxLength = 1000)
    private String descripcion;

    @Schema(description = "Tipo de recurso", example = "SALA", required = true)
    private String tipo;

    @Schema(description = "Ubicación física del recurso", example = "Piso 2, zona norte", required = true)
    private String ubicacion;

    @Schema(description = "Capacidad máxima de personas", example = "10", required = true)
    private Integer capacidad;

    @Schema(description = "Precio por hora en pesos colombianos", example = "50000.00", required = true)
    private BigDecimal precioPorHora;

    @Schema(description = "Estado actual del recurso", example = "DISPONIBLE", required = true, allowableValues = {"DISPONIBLE", "OCUPADO", "MANTENIMIENTO"})
    private String estado;

    @Schema(description = "Fecha y hora de creación del recurso", example = "2025-01-15T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de última actualización del recurso", example = "2025-01-15T15:45:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
}
