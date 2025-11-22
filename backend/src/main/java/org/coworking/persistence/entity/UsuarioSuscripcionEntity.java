package org.coworking.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa la tabla usuarios_suscripciones
 */
@Entity
@Table(name = "usuario_suscripcion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioSuscripcionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuarioEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suscripcion_id")
    private SuscripcionEntity suscripcionEntity;

    @Enumerated(EnumType.STRING)
    private EstadoSuscripcion estado;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum EstadoSuscripcion {
        ACTIVA,
        VENCIDA,
        CANCELADA
    }

}
