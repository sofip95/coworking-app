package org.coworking.persistence.repository;

import org.coworking.persistence.entity.NotificacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para operaciones de base de datos con notificaciones
 */
@Repository
public interface NotificacionRepository extends JpaRepository<NotificacionEntity, Long> {

    /**
     * Buscar notificaciones por usuarioId
     */
    List<NotificacionEntity> findByUsuarioEntityId(Long usuarioId);

    /**
     * Buscar notificaciones por estado
     */
    List<NotificacionEntity> findByEstado(String estado);

    /**
     * Buscar notificaciones enviadas en una fecha específica
     */
    List<NotificacionEntity> findByFechaEnvio(LocalDateTime fechaEnvio);

    /**
     * Buscar notificaciones por tipo
     */
    List<NotificacionEntity> findByTipo(String tipo);

}
