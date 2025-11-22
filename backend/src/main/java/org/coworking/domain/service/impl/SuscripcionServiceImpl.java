package org.coworking.domain.service.impl;

import org.coworking.domain.dto.SuscripcionDTO;
import org.coworking.domain.service.SuscripcionService;
import org.coworking.persistence.dao.SuscripcionDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SuscripcionServiceImpl implements SuscripcionService {

    private final SuscripcionDAO suscripcionDAO;

    /**
     * Create - crear una nueva suscripción
     *
     * FLUJO:
     * 1. Validar datos de entrada (precio positivo, datos obligatorios)
     * 2. Usar DAO.save() para persistir
     * 3. Retornar DTO con ID y timestamps
     */
    @Override
    public SuscripcionDTO createSuscripcion(SuscripcionDTO suscripcionDTO) {
        log.info("Creando suscripción: {}", suscripcionDTO);
        validateSuscripcionData(suscripcionDTO);

        SuscripcionDTO result = suscripcionDAO.save(suscripcionDTO);
        log.info("Suscripción creada con ID: {}", result.getId());

        return result;
    }

    /**
     * Read - obtener una suscripción por su ID
     */
    @Override
    @Transactional(readOnly = true)
    public SuscripcionDTO getSuscripcionById(Long id) {
        log.debug("Obteniendo suscripción con ID: {}", id);
        return suscripcionDAO.findById(id)
                .orElseThrow(() -> {
                    log.warn("Suscripción no encontrada con ID: {}", id);
                    return new RuntimeException("Suscripción no encontrada con ID: " + id);
                });
    }

    /**
     * Read - listar todas las suscripciones
     */
    @Override
    @Transactional(readOnly = true)
    public List<SuscripcionDTO> getAllSuscripciones() {
        log.debug("Obteniendo todas las suscripciones");
        return suscripcionDAO.findAll();
    }

    /**
     * READ - buscar suscripciones por tipo
     */
    @Override
    @Transactional(readOnly = true)
    public List<SuscripcionDTO> findSuscripcionesByTipo(String tipo) {
        log.debug("Buscando suscripciones por tipo: {}", tipo);
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de suscripción no puede ser nulo o vacío");
        }
        return suscripcionDAO.findSuscripcionesByTipo(tipo);
    }

    /**
     * READ - buscar suscripciones por duracion
     */
    @Override
    @Transactional(readOnly = true)
    public List<SuscripcionDTO> findSuscripcionesByDuracion(String duracion) {
        log.debug("Buscando suscripciones por duración: {}", duracion);
        if (duracion == null || duracion.trim().isEmpty()) {
            throw new IllegalArgumentException("La duración de la suscripción no puede ser nula o vacía");
        }
        return suscripcionDAO.findSuscripcionesByDuracion(duracion);
    }

    /**
     * Update - actualizar una suscripción existente
     *
     * FLUJO:
     * 1. Validar datos de entrada (existencia, precio positivo si se actualiza)
     * 2. Usar DAO.update() para persistir cambios
     * 3. Retornar DTO actualizado
     */
    @Override
    public SuscripcionDTO updateSuscripcion(Long id, SuscripcionDTO suscripcionDTO) {
        log.info("Actualizando suscripción con ID: {}", id);

        getSuscripcionById(id);

        validateSuscripcionUpdateData(suscripcionDTO);

        SuscripcionDTO updated = suscripcionDAO.update(id, suscripcionDTO)
                .orElseThrow(() -> new RuntimeException("Suscripción no encontrada con ID: " + id));

        log.info("Suscripción actualizada exitosamente ID: {}", id);
        return updated;
    }

    /**
     * Delete - eliminar una suscripción por su ID
     *
     * FLUJO:
     * 1. Verificar existencia con DAO.findById()
     * 2. Si existe, usar DAO.delete() para eliminar
     * 3. Si no existe, lanzar excepción
     */
    @Override
    public void deleteSuscripcion(Long id) {
        log.info("Eliminando suscripción con ID: {}", id);

        getSuscripcionById(id);

        boolean deleted = suscripcionDAO.deleteById(id);

        if (!deleted) {
            log.warn("No se pudo eliminar, suscripción no encontrada con ID: {}", id);
            throw new RuntimeException("Suscripción no encontrada con ID: " + id);
        }

        log.info("Suscripción eliminada exitosamente ID: {}", id);
    }

    /**
     * Validar los datos de la suscripción
     */
    private void validateSuscripcionData(SuscripcionDTO suscripcionDTO) {
        if (suscripcionDTO.getNombre() == null || suscripcionDTO.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la suscripción es obligatorio");
        }

        if (suscripcionDTO.getTipo() == null || suscripcionDTO.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de suscripción es obligatorio");
        }

        if (suscripcionDTO.getDuracion() == null || suscripcionDTO.getDuracion().trim().isEmpty()) {
            throw new IllegalArgumentException("La duración de la suscripción es obligatoria");
        }

        if (suscripcionDTO.getPrecio() == null || suscripcionDTO.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio de la suscripción debe ser positivo");
        }

        if (suscripcionDTO.getCantidadReservasPermitidas() == null || suscripcionDTO.getCantidadReservasPermitidas() < 0) {
            throw new IllegalArgumentException("La cantidad de reservas permitidas debe ser cero o positiva");
        }
    }

    /**
     * Validar los datos de la suscripción para actualización
     */
    private void validateSuscripcionUpdateData(SuscripcionDTO suscripcionDTO) {
        if (suscripcionDTO.getPrecio() != null && suscripcionDTO.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio de la suscripción debe ser positivo");
        }

        if (suscripcionDTO.getCantidadReservasPermitidas() != null && suscripcionDTO.getCantidadReservasPermitidas() < 0) {
            throw new IllegalArgumentException("La cantidad de reservas permitidas debe ser cero o positiva");
        }
    }

}
