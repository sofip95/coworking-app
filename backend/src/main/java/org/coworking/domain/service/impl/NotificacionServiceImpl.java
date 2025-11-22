package org.coworking.domain.service.impl;

import org.coworking.domain.dto.NotificacionDTO;
import org.coworking.domain.service.NotificacionService;
import org.coworking.persistence.dao.NotificacionDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionDAO notificacionDAO;

    /**
     * Create - crear una nueva notificación
     *
     * FLUJO:
     * 1. Validar datos de entrada (mensaje no vacío, datos obligatorios)
     * 2. Establecer timestamps y estado inicial
     * 3. Usar DAO.save() para persistir
     * 4. Retornar DTO con ID y timestamps
     */
    @Override
    public NotificacionDTO createNotificacion(NotificacionDTO notificacionDTO) {
        log.info("Creando nueva notificación: {}", notificacionDTO);

        validateNotificacionData(notificacionDTO);

        notificacionDTO.setEstado("PENDIENTE");
        notificacionDTO.setFechaEnvio(LocalDateTime.now());

        NotificacionDTO result = notificacionDAO.save(notificacionDTO);
        log.info("Notificación creada con ID: {}", result.getId());
        return result;
    }

    /**
     * Read - obtener una notificación por su ID
     */
    @Override
    @Transactional(readOnly = true)
    public NotificacionDTO getNotificacionById(Long id) {
        log.debug("Obteniendo notificación con ID: {}", id);
        return notificacionDAO.findById(id)
                .orElseThrow(() -> {
                    log.warn("Notificación no encontrada con ID: {}", id);
                    return new RuntimeException("Notificación no encontrada con ID: " + id);
                });
    }

    /**
     * List - listar todas las notificaciones
     */
    @Override
    @Transactional(readOnly = true)
    public List<NotificacionDTO> getAllNotificaciones() {
        log.debug("Obteniendo todas las notificaciones");
        return notificacionDAO.findAll();
    }

    /**
     * Update - actualizar una notificación existente
     *
     * FLUJO:
     * 1. Validar existencia de la notificación
     * 2. Validar datos de entrada (mensaje no vacío si se actualiza)
     * 3. Actualizar timestamp
     * 4. Usar DAO.update() para persistir cambios
     * 5. Retornar DTO actualizado
     */
    @Override
    public NotificacionDTO updateNotificacion(Long id, NotificacionDTO notificacionDTO) {
        log.info("Actualizando notificación con ID: {}", id);

        getNotificacionById(id);

        validateNotificacionUpdateData(notificacionDTO);

        notificacionDTO.setUpdatedAt(LocalDateTime.now());

        NotificacionDTO result = notificacionDAO.update(id, notificacionDTO)
                .orElseThrow(() -> {
                    log.error("Error al actualizar la notificación con ID: {}", id);
                    return new RuntimeException("Error al actualizar la notificación con ID: " + id);
                });
        log.info("Notificación actualizada con ID: {}", result.getId());
        return result;
    }

    /**
     * Delete - eliminar una notificación por su ID
     *
     * FLUJO:
     * 1. Verificar existencia con DAO.findById()
     * 2. Usar DAO.deleteById() para eliminar
     * 3. Si no existe, lanzar excepción
     */
    @Override
    public void deleteNotificacion(Long id) {
        log.info("Eliminando notificación con ID: {}", id);

        getNotificacionById(id);

        boolean deleted = notificacionDAO.deleteById(id);

        if (!deleted) {
            log.warn("No se pudo eliminar, notificación no encontrada con ID: {}", id);
            throw new RuntimeException("Notificación no encontrada con ID: " + id);
        }
        log.info("Notificación eliminada con ID: {}", id);
    }

    /**
     * Validar los datos de una notificación
     */
    private void validateNotificacionData(NotificacionDTO notificacionDTO) {
        if (notificacionDTO.getUsuarioId() == null) {
            log.error("Usuario ID no puede estar vacío");
            throw new RuntimeException("Usuario ID no puede estar vacío");
        }
        if (notificacionDTO.getTipo() == null || notificacionDTO.getTipo().isEmpty()) {
            log.error("Tipo de notificación no puede estar vacío");
            throw new RuntimeException("Tipo de notificación no puede estar vacío");
        }
        if (notificacionDTO.getTitulo() == null || notificacionDTO.getTitulo().isEmpty()) {
            log.error("Título de notificación no puede estar vacío");
            throw new RuntimeException("Título de notificación no puede estar vacío");
        }
        if (notificacionDTO.getMensaje() == null || notificacionDTO.getMensaje().isEmpty()) {
            log.error("Mensaje de notificación no puede estar vacío");
            throw new RuntimeException("Mensaje de notificación no puede estar vacío");
        }
    }

    /**
     * Validar los datos de actualización de una notificación
     */
    private void validateNotificacionUpdateData(NotificacionDTO notificacionDTO) {
        if (notificacionDTO.getTipo() != null && notificacionDTO.getTipo().isEmpty()) {
            log.error("Tipo de notificación no puede estar vacío si se actualiza");
            throw new RuntimeException("Tipo de notificación no puede estar vacío si se actualiza");
        }
        if (notificacionDTO.getTitulo() != null && notificacionDTO.getTitulo().isEmpty()) {
            log.error("Título de notificación no puede estar vacío si se actualiza");
            throw new RuntimeException("Título de notificación no puede estar vacío si se actualiza");
        }
        if (notificacionDTO.getMensaje() != null && notificacionDTO.getMensaje().isEmpty()) {
            log.error("Mensaje de notificación no puede estar vacío si se actualiza");
            throw new RuntimeException("Mensaje de notificación no puede estar vacío si se actualiza");
        }
    }

    /**
     * Buscar notificaciones por usuarioId
     */
    @Override
    @Transactional(readOnly = true)
    public List<NotificacionDTO> findNotificacionesByUsuarioId(Long usuarioId) {
        log.debug("Buscando notificaciones por usuarioId: {}", usuarioId);
        if (usuarioId == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        return notificacionDAO.findByUsuarioId(usuarioId);
    }

    /**
     * Buscar notificaciones por tipo
     */
    @Override
    @Transactional(readOnly = true)
    public List<NotificacionDTO> findNotificacionesByTipo(String tipo) {
        log.debug("Buscando notificaciones por tipo: {}", tipo);
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de notificación no puede ser nulo o vacío");
        }
        return notificacionDAO.findByTipo(tipo);
    }

    /**
     * Buscar notificaciones por estado
     */
    @Override
    @Transactional(readOnly = true)
    public List<NotificacionDTO> findNotificacionesByEstado(String estado) {
        log.debug("Buscando notificaciones por estado: {}", estado);
        if (estado == null || estado.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado de la notificación no puede ser nulo o vacío");
        }
        return notificacionDAO.findByEstado(estado);
    }

    /**
     * Buscar notificaciones enviadas por fecha
     */
    @Override
    @Transactional(readOnly = true)
    public List<NotificacionDTO> findNotificacionesByFechaEnvio(String fechaEnvio) {
        log.debug("Buscando notificaciones por fecha de envío: {}", fechaEnvio);
        if (fechaEnvio == null || fechaEnvio.trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha de envío no puede ser nula o vacía");
        }
        return notificacionDAO.findByFechaEnvio(fechaEnvio);
    }

}
