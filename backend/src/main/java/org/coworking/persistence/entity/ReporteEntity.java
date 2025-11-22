package org.coworking.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad para la tabla reporte
 */
@Entity
@Table(name = "reporte")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Usuario (muchos reportes pertenecen a un usuario)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private String tipo; // USO_RECURSOS, INGRESOS, OCUPACION, USUARIOS

    @Column(name = "cantidad_registros", nullable = false)
    private Integer cantidadRegistros;

    @Column(columnDefinition = "TEXT")
    private String contenido; // JSON con los datos del reporte

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
