package org.coworking.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad para la tabla recurso
 */
@Entity
@Table(name = "recurso")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecursoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private String tipo;

    private String ubicacion;

    private Integer capacidad;

    @Column(name = "precio_por_hora", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioPorHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoRecurso estado = EstadoRecurso.DISPONIBLE;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        if (this.estado == null) this.estado = EstadoRecurso.DISPONIBLE;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Enumeración para los posibles estados del recurso.
     */
    public enum EstadoRecurso {
        DISPONIBLE,
        OCUPADO,
        MANTENIMIENTO
    }
}
