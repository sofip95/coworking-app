package org.coworking.persistence.dao;

import org.coworking.domain.dto.NotificacionDTO;
import org.coworking.persistence.entity.NotificacionEntity;
import org.coworking.persistence.mapper.NotificacionMapper;
import org.coworking.persistence.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/** DAO para operaciones de base de datos con notificaciones
 */
@Repository
@RequiredArgsConstructor
public class NotificacionDAO {

    private final NotificacionRepository notificacionRepository;
    private final NotificacionMapper notificacionMapper;

    /**
     * CREATE - Crear una nueva notificación
     *
     * FLUJO:
     * 1. NotificacionDTO -> NotificacionEntity (mapper usa toEntity())
     * 2. Guardar Entity en BD
     * 3. Entity guardada -> NotificacionDTO (con ID generado)
     *
     * NOTA:
     * - NotificacionDTO.id será null para CREATE
     * - Mapper ignora id, createdAt, updatedAt automáticamente
     */
    public NotificacionDTO save(NotificacionDTO notificacionDTO) {
        NotificacionEntity entity = notificacionMapper.toEntity(notificacionDTO);
        NotificacionEntity savedEntity = notificacionRepository.save(entity);
        return notificacionMapper.toDTO(savedEntity);
    }

    /**
     * READ - Buscar notificación por ID
     *
     * @return Optional<NotificacionEntity> (vacío si no existe)
     */
    public Optional<NotificacionDTO> findById(Long id) {
        return notificacionRepository.findById(id)
                .map(notificacionMapper::toDTO);
    }

    /**
     * READ - Listar todas las notificaciones
     */
    public List<NotificacionDTO> findAll() {
        List<NotificacionEntity> entities = notificacionRepository.findAll();
        return notificacionMapper.toDTOList(entities);
    }

    /**
     * UPDATE - Actualizar una notificación existente
     *
     * FLUJO:
     * 1. Buscar Entity en BD por ID (si no existe, lanzar excepción o retornar null)
     * 2. Copiar campos no nulos de NotificacionDTO a la Entity encontrada (mapper usa updateEntityFromDTO())
     * 3. Guardar Entity actualizada en BD
     * 4. Entity actualizada -> NotificacionDTO y retornarla
     *
     * NOTA:
     * - NotificacionDTO.id debe existir en BD para UPDATE
     * - Mapper ignora id, createdAt, updatedAt automáticamente
     * - Si un campo en el DTO es null, NO sobreescribe el valor en la entidad
     */
    public Optional<NotificacionDTO> update(Long id, NotificacionDTO notificacionDTO) {
        return notificacionRepository.findById(id)
                .map(existingEntity -> {
                    notificacionMapper.updateEntityFromDTO(notificacionDTO, existingEntity);
                    NotificacionEntity updatedEntity = notificacionRepository.save(existingEntity);
                    return notificacionMapper.toDTO(updatedEntity);
                });
    }

    /**
     * DELETE - Eliminar una notificación por ID
     *
     * FLUJO:
     * 1. Verificar si la notificación existe por ID
     * 2. Si existe, eliminarla de la BD
     * 3. Retornar true si se eliminó, false si no existía
     */
    public boolean deleteById(Long id) {
        if (notificacionRepository.existsById(id)) {
            notificacionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Buscar notificaciones por usuarioId
     */
    public List<NotificacionDTO> findByUsuarioId(Long usuarioId) {
        List<NotificacionEntity> entities = notificacionRepository.findByUsuarioEntityId(usuarioId);
        return notificacionMapper.toDTOList(entities);
    }

    /**
     * Buscar notificaciones por tipo
     */
    public List<NotificacionDTO> findByTipo(String tipo) {
        List<NotificacionEntity> entities = notificacionRepository.findByTipo(tipo);
        return notificacionMapper.toDTOList(entities);
    }

    /**
     * Buscar notificaciones por estado
     */
    public List<NotificacionDTO> findByEstado(String estado) {
        List<NotificacionEntity> entities = notificacionRepository.findByEstado(estado);
        return notificacionMapper.toDTOList(entities);
    }

    /**
     * Buscar notificaciones enviadas por fecha
     */
    public List<NotificacionDTO> findByFechaEnvio(String fechaEnvio) {
        LocalDateTime fecha = LocalDateTime.parse(fechaEnvio);
        List<NotificacionEntity> entities = notificacionRepository.findByFechaEnvio(fecha);
        return notificacionMapper.toDTOList(entities);
    }

    /**
     * UTILIDAD - Verificar si existe notificacion por ID
     *
     * Método auxiliar para validaciones rápidas sin cargar entity completa
     */
    public boolean existsById(Long id) {
        return notificacionRepository.existsById(id);
    }

}
