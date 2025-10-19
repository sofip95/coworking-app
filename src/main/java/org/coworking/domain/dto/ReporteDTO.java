package org.coworking.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para representar los datos de un reporte.
 *
 * Se usa para comunicación entre el backend y el cliente (API REST).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información del reporte generado")
public class ReporteDTO {

    @Schema(description = "ID único del reporte", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID del usuario asociado al reporte", example = "2", required = true)
    private Long usuarioId;

    @Schema(description = "Título del reporte", example = "Reporte de Ingresos Mensuales", required = true, maxLength = 255)
    private String titulo;

    @Schema(description = "Descripción del reporte", example = "Detalle de ingresos por reservas y pagos realizados.")
    private String descripcion;

    @Schema(description = "Tipo de reporte", example = "INGRESOS", allowableValues = {"USO_RECURSOS", "INGRESOS", "OCUPACION", "USUARIOS"}, required = true)
    private String tipo;

    @Schema(description = "Cantidad de registros analizados en el reporte", example = "45", required = true)
    private Integer cantidadRegistros;

    @Schema(description = "Contenido del reporte en formato JSON", example = "{\"totalIngresos\":520000}", required = true)
    private String contenido;

    @Schema(description = "Fecha y hora de creación del reporte", example = "2025-01-15T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;
}
