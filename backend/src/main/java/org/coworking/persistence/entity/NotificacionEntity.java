package org.coworking.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa la tabla notificaciones
 */
@Entity
@Table(name = "notificacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuarioEntity;

    @Enumerated(EnumType.STRING)
    private TipoNotificacion tipo;

    private String titulo;

    private String mensaje;

    @Enumerated(EnumType.STRING)
    private EstadoNotificacion estado;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum TipoNotificacion {
        CONFIRMACION,
        RECORDATORIO,
        CANCELACION
    }

    public enum EstadoNotificacion {
        PENDIENTE,
        ENVIADA,
        FALLIDA,
        LEIDA
    }

}
