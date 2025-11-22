package org.coworking.persistence.repository;

import org.coworking.persistence.entity.UsuarioSuscripcionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;

/**
 * Repositorio para operaciones de base de datos con usuarios_suscripciones
 */
@Repository
public interface UsuarioSuscripcionRepository extends JpaRepository<UsuarioSuscripcionEntity, Long> {

    /**
     * Buscar asignaciones de suscripciones por ID de usuario
     */
    List<UsuarioSuscripcionEntity> findByUsuarioEntityId(Long usuarioId);

    /**
     * Buscar asignaciones de suscripciones por ID de suscripción
     */
    List<UsuarioSuscripcionEntity> findBySuscripcionEntityId(Long suscripcionId);

    /**
     * Buscar asignaciones de suscripciones por estado
     */
    List<UsuarioSuscripcionEntity> findByEstado(String estado);

    /**
     * Buscar asignaciones de suscripciones por fecha de inicio
     */
    List<UsuarioSuscripcionEntity> findByFechaInicio(LocalDateTime fechaInicio);

    /**
     * Verificar si existe alguna asignación de suscripción para el usuario dado
     */
    boolean existsByUsuarioEntityId(Long usuarioId);

    /**
     * Verificar si existe alguna asignación de suscripción para la suscripción dada
     */
    boolean existsBySuscripcionEntityId(Long suscripcionId);

}
