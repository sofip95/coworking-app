package org.coworking.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que representa la tabla suscripciones
 */
@Entity
@Table(name = "suscripcion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuscripcionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    private TipoSuscripcion tipo;

    @Enumerated(EnumType.STRING)
    private DuracionSuscripcion duracion;

    private BigDecimal precio;

    @Column(name = "cantidad_reservas_permitidas")
    private Integer cantidadReservasPermitidas;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum TipoSuscripcion {
        BASICA,
        PREMIUM
    }

    public enum DuracionSuscripcion {
        MENSUAL,
        TRIMESTRAL,
        SEMESTRAL,
        ANUAL
    }

}
