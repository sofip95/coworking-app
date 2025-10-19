package org.coworking.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.coworking.domain.dto.RecursoDTO;
import org.coworking.domain.service.RecursoService;
import org.coworking.persistence.dao.RecursoDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementación del servicio de recursos.
 *
 * RESPONSABILIDADES:
 * - Gestionar operaciones CRUD
 * - Aplicar validaciones de negocio
 * - Delegar persistencia al DAO correspondiente
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RecursoServiceImpl implements RecursoService {

    private final RecursoDAO recursoDAO;

    // ===================== CREATE =====================
    @Override
    public RecursoDTO createRecurso(RecursoDTO recursoDTO) {
        log.info("Creando nuevo recurso con nombre: {}", recursoDTO.getNombre());
        validateRecursoData(recursoDTO);

        RecursoDTO saved = recursoDAO.save(recursoDTO);
        log.info("Recurso creado exitosamente con ID: {}", saved.getId());
        return saved;
    }

    // ===================== READ BY ID =====================
    @Override
    @Transactional(readOnly = true)
    public RecursoDTO getRecursoById(Long id) {
        log.debug("Buscando recurso por ID: {}", id);
        return recursoDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado con ID: " + id));
    }

    // ===================== FILTER =====================
    @Override
    @Transactional(readOnly = true)
    public List<RecursoDTO> getFilteredRecursos(String estado, String tipo, String ubicacion, Integer capacidadMinima, BigDecimal precioMaximo) {
        log.debug("Filtrando recursos con parámetros: estado={}, tipo={}, ubicación={}, capacidadMinima={}, precioMaximo={}",
                estado, tipo, ubicacion, capacidadMinima, precioMaximo);

        return recursoDAO.findByFilters(estado, tipo, ubicacion, capacidadMinima, precioMaximo);
    }

    // ===================== UPDATE =====================
    @Override
    public RecursoDTO updateRecurso(Long id, RecursoDTO recursoDTO) {
        log.info("Actualizando recurso ID: {}", id);
        getRecursoById(id); // lanza excepción si no existe

        validateRecursoUpdateData(recursoDTO);

        return recursoDAO.update(id, recursoDTO)
                .orElseThrow(() -> new RuntimeException("Error al actualizar recurso con ID: " + id));
    }

    // ===================== DELETE =====================
    @Override
    public void deleteRecurso(Long id) {
        getRecursoById(id); // valida existencia
        boolean deleted = recursoDAO.deleteById(id);
        if (!deleted) throw new RuntimeException("Error al eliminar recurso con ID: " + id);
        log.info("Recurso eliminado exitosamente con ID: {}", id);
    }

    // ===================== VALIDACIONES =====================
    private void validateRecursoData(RecursoDTO recursoDTO) {
        if (recursoDTO.getNombre() == null || recursoDTO.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del recurso es obligatorio");
        }
        if (recursoDTO.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
        }
        if (recursoDTO.getTipo() == null || recursoDTO.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo del recurso es obligatorio");
        }
        if (recursoDTO.getPrecioPorHora() == null || recursoDTO.getPrecioPorHora().doubleValue() < 0) {
            throw new IllegalArgumentException("El precio por hora debe ser mayor o igual a 0");
        }
        if (recursoDTO.getCapacidad() != null && recursoDTO.getCapacidad() < 0) {
            throw new IllegalArgumentException("La capacidad no puede ser negativa");
        }
    }

    private void validateRecursoUpdateData(RecursoDTO recursoDTO) {
        if (recursoDTO.getNombre() != null) {
            if (recursoDTO.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            if (recursoDTO.getNombre().length() > 100) {
                throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
            }
        }
        if (recursoDTO.getPrecioPorHora() != null && recursoDTO.getPrecioPorHora().doubleValue() < 0) {
            throw new IllegalArgumentException("El precio por hora debe ser mayor o igual a 0");
        }
        if (recursoDTO.getCapacidad() != null && recursoDTO.getCapacidad() < 0) {
            throw new IllegalArgumentException("La capacidad no puede ser negativa");
        }
    }
}
