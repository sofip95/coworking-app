package org.coworking.persistence.repository;

import java.math.BigDecimal;
import java.util.List;

import org.coworking.persistence.entity.SuscripcionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para operaciones de base de datos con suscripciones
 */
@Repository
public interface SuscripcionRepository extends JpaRepository<SuscripcionEntity, Long> {

    /**
     * Buscar suscripciones por tipo
     */
    List<SuscripcionEntity> findByTipo(String tipo);

    /**
     * Buscar suscripciones por duración
     */
    List<SuscripcionEntity> findByDuracion(String duracion);

    /**
     * Consulta general combinada para múltiples filtros dinámicos.
     *
     * Si alguno de los parámetros es null, no se aplica ese filtro.
     */
    @Query("""
    SELECT s FROM SuscripcionEntity s
    WHERE (:tipo IS NULL OR s.tipo = :tipo)
      AND (:duracion IS NULL OR s.duracion = :duracion)
      AND (:precioMinimo IS NULL OR s.precio >= :precioMinimo)
      AND (:precioMaximo IS NULL OR s.precio <= :precioMaximo)
      AND (:cantidadReservasPermitidasMinima IS NULL OR s.cantidadReservasPermitidas >= :cantidadReservasPermitidasMinima)
""")
    List<SuscripcionEntity> findByFilters(
            String tipo,
            String duracion,
            BigDecimal precioMinimo,
            BigDecimal precioMaximo,
            Integer cantidadReservasPermitidasMinima
    );

}
