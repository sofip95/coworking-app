package org.coworking.persistence.dao;

import lombok.RequiredArgsConstructor;
import org.coworking.domain.dto.RecursoDTO;
import org.coworking.persistence.entity.RecursoEntity;
import org.coworking.persistence.mapper.RecursoMapper;
import org.coworking.persistence.repository.RecursoRepository;
import org.springframework.stereotype.Repository;
import org.coworking.persistence.entity.RecursoEntity.EstadoRecurso;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * DAO para manejar operaciones CRUD y consultas personalizadas de recursos.
 *
 * FLUJO GENERAL:
 * - Convierte entre DTO y Entity usando RecursoMapper.
 * - Usa RecursoRepository para acceder a la base de datos.
 * - Devuelve DTOs listos para el servicio o controlador.
 */
@Repository
@RequiredArgsConstructor
public class RecursoDAO {

    private final RecursoRepository recursoRepository;
    private final RecursoMapper recursoMapper;

    // -------------------------- CREATE --------------------------
    /**
     * Crear un nuevo recurso.
     *
     * Flujo:
     * 1. DTO → Entity (mappeo con RecursoMapper)
     * 2. Guardar Entity en BD
     * 3. Entity guardada → DTO (retorna con ID generado)
     */
    public RecursoDTO save(RecursoDTO recursoDTO) {
        RecursoEntity entity = recursoMapper.toEntity(recursoDTO);
        RecursoEntity saved = recursoRepository.save(entity);
        return recursoMapper.toDTO(saved);
    }

    // -------------------------- READ --------------------------
    /**
     * Buscar recurso por ID.
     */
    public Optional<RecursoDTO> findById(Long id) {
        return recursoRepository.findById(id)
                .map(recursoMapper::toDTO);
    }

    /**
     * Buscar todos los recursos.
     */
    public List<RecursoDTO> findAll() {
        List<RecursoEntity> entities = recursoRepository.findAll();
        return recursoMapper.toDTOList(entities);
    }

    /**
     * Buscar por estado (ej. DISPONIBLE, OCUPADO).
     */
    public List<RecursoDTO> findByEstado(String estado) {
        List<RecursoEntity> entities = recursoRepository.findByEstado(estado);
        return recursoMapper.toDTOList(entities);
    }

    /**
     * Buscar por tipo (ej. SALA, OFICINA, ESCRITORIO).
     */
    public List<RecursoDTO> findByTipo(String tipo) {
        List<RecursoEntity> entities = recursoRepository.findByTipo(tipo);
        return recursoMapper.toDTOList(entities);
    }

    /**
     * Buscar por ubicación parcial (LIKE).
     */
    public List<RecursoDTO> findByUbicacion(String ubicacion) {
        List<RecursoEntity> entities = recursoRepository.findByUbicacionContainingIgnoreCase(ubicacion);
        return recursoMapper.toDTOList(entities);
    }

    /**
     * Buscar por capacidad mínima.
     */
    public List<RecursoDTO> findByCapacidadMinima(Integer capacidadMinima) {
        List<RecursoEntity> entities = recursoRepository.findByCapacidadGreaterThanEqual(capacidadMinima);
        return recursoMapper.toDTOList(entities);
    }

    /**
     * Buscar por precio máximo por hora.
     */
    public List<RecursoDTO> findByPrecioMaximo(BigDecimal precioMaximo) {
        List<RecursoEntity> entities = recursoRepository.findByPrecioPorHoraLessThanEqual(precioMaximo);
        return recursoMapper.toDTOList(entities);
    }

    /**
     * Buscar con filtros combinados dinámicos.
     */
    public List<RecursoDTO> findByFilters(String estado, String tipo, String ubicacion,
                                          Integer capacidadMinima, BigDecimal precioMaximo) {

        EstadoRecurso estadoEnum = null;
        if (estado != null && !estado.trim().isEmpty()) {
            try {
                estadoEnum = EstadoRecurso.valueOf(estado.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                        "El estado '" + estado + "' no es válido. Valores permitidos: DISPONIBLE, OCUPADO, MANTENIMIENTO"
                );
            }
        }

        List<RecursoEntity> entities = recursoRepository.findByFilters(
                estadoEnum, tipo, ubicacion, capacidadMinima, precioMaximo
        );
        return recursoMapper.toDTOList(entities);
    }

    // -------------------------- UPDATE --------------------------
    /**
     * Actualizar un recurso existente.
     */
    public Optional<RecursoDTO> update(Long id, RecursoDTO recursoDTO) {
        return recursoRepository.findById(id)
                .map(existingEntity -> {
                    recursoMapper.updateEntityFromDTO(recursoDTO, existingEntity);
                    RecursoEntity updated = recursoRepository.save(existingEntity);
                    return recursoMapper.toDTO(updated);
                });
    }

    // -------------------------- DELETE --------------------------
    /**
     * Eliminar recurso por ID.
     */
    public boolean deleteById(Long id) {
        if (recursoRepository.existsById(id)) {
            recursoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // -------------------------- UTILIDADES --------------------------

    /**
     * Contar total de recursos.
     */
    public long count() {
        return recursoRepository.count();
    }
}