package org.coworking.persistence.repository;

import org.coworking.persistence.entity.ReporteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para ReporteEntity.
 *
 * DESCRIPCIÓN:
 * - Gestiona el acceso a datos de la tabla "reporte".
 * - Proporciona consultas personalizadas usadas por ReporteDAO o ReporteService.
 *
 * NOTA:
 * - JpaRepository ya incluye los métodos básicos:
 *   save(), findById(), findAll(), deleteById(), count(), existsById(), etc.
 */
@Repository
public interface ReporteRepository extends JpaRepository<ReporteEntity, Long> {

    /**
     * Buscar reportes por tipo.
     * Ejemplo: tipo = "INGRESOS", "OCUPACION", "USUARIOS"
     */
    List<ReporteEntity> findByTipo(String tipo);

}
