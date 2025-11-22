package org.coworking.persistence.dao;

import org.coworking.domain.dto.UsuarioSuscripcionDTO;
import org.coworking.persistence.entity.UsuarioSuscripcionEntity;
import org.coworking.persistence.mapper.UsuarioSuscripcionMapper;
import org.coworking.persistence.repository.UsuarioSuscripcionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * DAO para operaciones de base de datos con usuarios_suscripciones
 */
@Repository
@RequiredArgsConstructor
public class UsuarioSuscripcionDAO {

    private final UsuarioSuscripcionRepository usuarioSuscripcionRepository;
    private final UsuarioSuscripcionMapper usuarioSuscripcionMapper;

    /**
     * CREATE - Crear una nueva asignación de suscripción a usuario
     *
     * FLUJO:
     * 1. UsuarioSuscripcionDTO -> UsuarioSuscripcionEntity (mapper usa toEntity())
     * 2. Guardar Entity en BD
     * 3. Entity guardada -> UsuarioSuscripcionDTO (con ID generado)
     *
     * NOTA:
     * - UsuarioSuscripcionDTO.id será null para CREATE
     * - Mapper ignora id, usuarioEntity, suscripcionEntity, createdAt, updatedAt automáticamente
     * - Asignar usuarioEntity y suscripcionEntity por separado antes de guardar
     */
    public UsuarioSuscripcionDTO save(UsuarioSuscripcionDTO usuarioSuscripcionDTO) {
        UsuarioSuscripcionEntity entity = usuarioSuscripcionMapper.toEntity(usuarioSuscripcionDTO);
        UsuarioSuscripcionEntity savedEntity = usuarioSuscripcionRepository.save(entity);
        return usuarioSuscripcionMapper.toDTO(savedEntity);
    }

    /**
     * READ - Buscar asignación de suscripción por ID
     *
     * @return Optional<UsuarioSuscripcionDTO> (vacío si no existe)
     */
    public Optional<UsuarioSuscripcionDTO> findById(Long id) {
        return usuarioSuscripcionRepository.findById(id)
                .map(usuarioSuscripcionMapper::toDTO);
    }

    /**
     * READ - Listar todas las asignaciones de suscripciones
     */
    public List<UsuarioSuscripcionDTO> findAll() {
        List<UsuarioSuscripcionEntity> entities = usuarioSuscripcionRepository.findAll();
        return usuarioSuscripcionMapper.toDTOList(entities);
    }

    /**
     * UPDATE - Actualizar una asignación de suscripción existente
     *
     * FLUJO:
     * 1. Buscar la entidad existente por ID
     * 2. Si existe, actualizar campos con datos del DTO (mapper usa updateEntityFromDTO())
     * 3. Guardar la entidad actualizada en BD
     * 4. Entity actualizada -> UsuarioSuscripcionDTO
     *
     * NOTA:
     * - UsuarioSuscripcionDTO.id debe existir para UPDATE
     * - Mapper ignora id, usuarioEntity, suscripcionEntity, createdAt, updatedAt automáticamente
     * - No actualizar usuarioEntity ni suscripcionEntity aquí
     */
    public Optional<UsuarioSuscripcionDTO> update(UsuarioSuscripcionDTO usuarioSuscripcionDTO) {
        return usuarioSuscripcionRepository.findById(usuarioSuscripcionDTO.getId())
                .map(existingEntity -> {
                    usuarioSuscripcionMapper.updateEntityFromDTO(usuarioSuscripcionDTO, existingEntity);
                    UsuarioSuscripcionEntity updatedEntity = usuarioSuscripcionRepository.save(existingEntity);
                    return usuarioSuscripcionMapper.toDTO(updatedEntity);
                });
    }

    /**
     * DELETE - Eliminar una asignación de suscripción por ID
     *
     * FLUJO:
     * 1. Verificar si la entidad existe por ID
     * 2. Si existe, eliminarla de la BD
     *
     * @return true si se eliminó, false si no existía
     */
    public boolean deleteById(Long id) {
        if (usuarioSuscripcionRepository.existsById(id)) {
            usuarioSuscripcionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Buscar asignaciones de suscripciones por usuarioId
     */
    public List<UsuarioSuscripcionDTO> findByUsuarioId(Long usuarioId) {
        List<UsuarioSuscripcionEntity> entities = usuarioSuscripcionRepository.findByUsuarioEntityId(usuarioId);
        return usuarioSuscripcionMapper.toDTOList(entities);
    }

    /**
     * Buscar asignaciones de suscripciones por suscripcionId
     */
    public List<UsuarioSuscripcionDTO> findBySuscripcionId(Long suscripcionId) {
        List<UsuarioSuscripcionEntity> entities = usuarioSuscripcionRepository.findBySuscripcionEntityId(suscripcionId);
        return usuarioSuscripcionMapper.toDTOList(entities);
    }

    /**
     * Buscar asignaciones de suscripciones por estado
     */
    public List<UsuarioSuscripcionDTO> findByEstado(String estado) {
        List<UsuarioSuscripcionEntity> entities = usuarioSuscripcionRepository.findByEstado(estado);
        return usuarioSuscripcionMapper.toDTOList(entities);
    }

    /**
     * Buscar aignaciones de suscripciones por fecha
     */
    public List<UsuarioSuscripcionDTO> findByFechaInicio(LocalDateTime fechaInicio) {
        List<UsuarioSuscripcionEntity> entities = usuarioSuscripcionRepository.findByFechaInicio(fechaInicio);
        return usuarioSuscripcionMapper.toDTOList(entities);
    }

    /**
     * UTILIDAD - Verificar si existe asignación de suscripción por ID
     */
    public boolean existsById(Long id) {
        return usuarioSuscripcionRepository.existsById(id);
    }

    /**
     * UTILIDAD - Verificar si el usuario existe
     */
    public boolean usuarioExists(Long usuarioId) {
        return usuarioSuscripcionRepository.existsByUsuarioEntityId(usuarioId);
    }

    /**
     * UTILIDAD - Verificar si la suscripción existe
     */
    public boolean suscripcionExists(Long suscripcionId) {
        return usuarioSuscripcionRepository.existsBySuscripcionEntityId(suscripcionId);
    }

}
