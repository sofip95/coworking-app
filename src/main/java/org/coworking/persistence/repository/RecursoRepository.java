package org.coworking.persistence.repository;

import org.coworking.persistence.entity.RecursoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repositorio JPA para RecursoEntity.
 *
 * DESCRIPCIÓN:
 * - Gestiona el acceso a datos de la tabla "recurso".
 * - Define consultas personalizadas utilizadas por RecursoDAO y RecursoService.
 *
 * NOTA:
 * - JpaRepository ya incluye los métodos básicos:
 *   save(), findById(), findAll(), deleteById(), count(), existsById(), etc.
 */
@Repository
public interface RecursoRepository extends JpaRepository<RecursoEntity, Long> {

    /**
     * Buscar recursos por estado exacto.
     * Ejemplo: "DISPONIBLE", "OCUPADO", "MANTENIMIENTO".
     *
     * Usado en: GET /api/v1/recursos?estado=DISPONIBLE
     */
    List<RecursoEntity> findByEstado(String estado);

    /**
     * Buscar recursos por tipo (ej. "SALA", "OFICINA", "ESCRITORIO").
     *
     * Usado en: GET /api/v1/recursos?tipo=SALA
     */
    List<RecursoEntity> findByTipo(String tipo);

    /**
     * Buscar recursos por ubicación exacta o parcial.
     * Usa LIKE para coincidencias flexibles.
     *
     * Usado en: GET /api/v1/recursos?ubicacion=Centro
     */
    @Query("SELECT r FROM RecursoEntity r WHERE LOWER(r.ubicacion) LIKE LOWER(CONCAT('%', :ubicacion, '%'))")
    List<RecursoEntity> findByUbicacionContainingIgnoreCase(String ubicacion);

    /**
     * Buscar recursos con capacidad mayor o igual a la indicada.
     *
     * Usado en: GET /api/v1/recursos?capacidadMinima=10
     */
    @Query("SELECT r FROM RecursoEntity r WHERE r.capacidad >= :capacidadMinima")
    List<RecursoEntity> findByCapacidadGreaterThanEqual(Integer capacidadMinima);

    /**
     * Buscar recursos con precio por hora menor o igual al indicado.
     *
     * Usado en: GET /api/v1/recursos?precioMaximo=50000
     */
    @Query("SELECT r FROM RecursoEntity r WHERE r.precioPorHora <= :precioMaximo")
    List<RecursoEntity> findByPrecioPorHoraLessThanEqual(BigDecimal precioMaximo);

    /**
     * Consulta general combinada para múltiples filtros dinámicos.
     *
     * Si alguno de los parámetros es null, no se aplica ese filtro.
     * Usado en: GET /api/v1/recursos con varios parámetros opcionales.
     */
    @Query("""
        SELECT r FROM RecursoEntity r
        WHERE (:estado IS NULL OR r.estado = :estado)
          AND (:tipo IS NULL OR r.tipo = :tipo)
          AND (:ubicacion IS NULL OR LOWER(r.ubicacion) LIKE LOWER(CONCAT('%', :ubicacion, '%')))
          AND (:capacidadMinima IS NULL OR r.capacidad >= :capacidadMinima)
          AND (:precioMaximo IS NULL OR r.precioPorHora <= :precioMaximo)
    """)
    List<RecursoEntity> findByFilters(
            String estado,
            String tipo,
            String ubicacion,
            Integer capacidadMinima,
            BigDecimal precioMaximo
    );
}
