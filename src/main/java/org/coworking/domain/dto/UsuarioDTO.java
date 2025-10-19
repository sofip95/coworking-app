package org.coworking.domain.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO COMPLETO de usuarios - Para LECTURA/RESPUESTAS
 *
 * ¿POR QUÉ ESTE DTO?
 * - Se usa cuando DEVOLVEMOS información completa al cliente
 * - Contiene campos auto-generados (ID, fechas) que el cliente necesita ver
 * - Optimiza la respuesta incluyendo datos relacionados en una sola consulta
 *
 * USO: GET /usuarios/{id}, GET /usuarios, respuestas de POST/PUT
 * NOTA: Este DTO incluye campos READ_ONLY que el cliente no puede modificar
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información del usuario")
public class UsuarioDTO {

    @Schema(description = "ID único del usuario", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nombre del usuario", example = "Juan Pérez", required = true, maxLength = 100)
    private String nombre;

    @Schema(description = "Email del usuario", example = "juan.perez@email.com", required = true, maxLength = 150)
    private String email;

    @Schema(description = "Teléfono del usuario", example = "+57 300 123 4567", maxLength = 20)
    private String telefono;

    @Schema(description = "Rol del usuario", example = "ADMIN", required = true, allowableValues = {"ADMIN", "MIEMBRO", "VISITANTE"})
    private String rol;

    @Schema(description = "Estado activo del usuario", example = "true", required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean estado;

    @Schema(description = "Fecha y hora de creación del registro", example = "2025-01-15T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de última actualización", example = "2025-01-15T15:45:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
}
