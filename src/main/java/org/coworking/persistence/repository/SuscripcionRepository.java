package org.coworking.persistence.repository;

import org.coworking.persistence.entity.SuscripcionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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

}
