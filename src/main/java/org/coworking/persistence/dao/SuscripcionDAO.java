package org.coworking.persistence.dao;

import org.coworking.domain.dto.SuscripcionDTO;
import org.coworking.persistence.entity.SuscripcionEntity;
import org.coworking.persistence.mapper.SuscripcionMapper;
import org.coworking.persistence.repository.SuscripcionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * DAO para operaciones de base de datos con suscripciones
 */
@Repository
@RequiredArgsConstructor
public class SuscripcionDAO {

    private final SuscripcionRepository suscripcionRepository;
    private final SuscripcionMapper suscripcionMapper;

    /**
     * CREATE - Crear una nueva suscripción
     *
     * FLUJO:
     * 1. SuscripcionDTO -> SuscripcionEntity (mapper usa toEntity())
     * 2. Guardar Entity en BD
     * 3. Entity guardada -> SuscripcionDTO (con ID generado)
     *
     * NOTA:
     * - SuscripcionDTO.id será null para CREATE
     * - Mapper ignora id, createdAt, updatedAt automáticamente
     */
    public SuscripcionDTO save(SuscripcionDTO suscripcionDTO) {
        SuscripcionEntity entity = suscripcionMapper.toEntity(suscripcionDTO);
        SuscripcionEntity savedEntity = suscripcionRepository.save(entity);
        return suscripcionMapper.toDTO(savedEntity);
    }

    /**
     * READ - Buscar suscripción por ID
     *
     * @return Optional<SuscripcionEntity> (vacío si no existe)
     */
    public Optional<SuscripcionDTO> findById(Long id) {
        return suscripcionRepository.findById(id)
                .map(suscripcionMapper::toDTO);
    }

    /**
     * READ - Listar todas las suscripciones
     */
    public List<SuscripcionDTO> findAll() {
        List<SuscripcionEntity> entities = suscripcionRepository.findAll();
        return suscripcionMapper.toDTOList(entities);
    }

    /**
     * UPDATE - Actualizar una suscripción existente
     * FLUJO:
     * 1. Buscar la entidad existente en BD por ID
     * 2. Si existe, actualizar campos con mapper.updateEntityFromDTO()
     * 3. Guardar Entity actualizada en BD
     * 4. Entity actualizada -> SuscripcionDTO
     * NOTA:
     * - SuscripcionDTO.id debe tener el ID de la suscripción a actualizar
     * - Mapper ignora id, createdAt, updatedAt automáticamente
     * - Si un campo en el DTO es null, NO sobreescribe el valor en la entidad
     */
    public Optional<SuscripcionDTO> update(Long id, SuscripcionDTO suscripcionDTO) {
        return suscripcionRepository.findById(id)
                .map(existingEntity -> {
                    suscripcionMapper.updateEntityFromDTO(suscripcionDTO, existingEntity);
                    SuscripcionEntity updatedEntity = suscripcionRepository.save(existingEntity);
                    return suscripcionMapper.toDTO(updatedEntity);
                });
    }

    /**
     * DELETE - Eliminar una suscripción por ID
     *
     * @return true si se eliminó, false si no existía
     */
    public boolean deleteById(Long id) {
        if (suscripcionRepository.existsById(id)) {
            suscripcionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Buscar suscripciones por tipo
     */
    public List<SuscripcionDTO> findSuscripcionesByTipo(String tipo) {
        List<SuscripcionEntity> entities = suscripcionRepository.findByTipo(tipo);
        return suscripcionMapper.toDTOList(entities);
    }

    /**
     * Buscar suscripciones por duracion
     */
    public List<SuscripcionDTO> findSuscripcionesByDuracion(String duracion) {
        List<SuscripcionEntity> entities = suscripcionRepository.findByDuracion(duracion);
        return suscripcionMapper.toDTOList(entities);
    }

    /**
     * UTILIDAD - Verificar si existe suscripcion por ID
     *
     * Método auxiliar para validaciones rápidas sin cargar entity completa
     */
    public boolean existsById(Long id) {
        return suscripcionRepository.existsById(id);
    }

}