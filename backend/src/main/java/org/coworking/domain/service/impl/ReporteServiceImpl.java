package org.coworking.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.coworking.domain.dto.ReporteDTO;
import org.coworking.domain.service.ReporteService;
import org.coworking.persistence.dao.ReporteDAO;
import org.coworking.persistence.dao.UsuarioDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementación del servicio para la gestión de reportes.
 *
 * RESPONSABILIDADES:
 * - Validar datos antes de guardar.
 * - Verificar existencia de usuario.
 * - Coordinar operaciones entre DAO y capa de presentación.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReporteServiceImpl implements ReporteService {

    private final ReporteDAO reporteDAO;
    private final UsuarioDAO usuarioDAO;

    // =================== CREAR REPORTE ===================
    @Override
    public ReporteDTO createReporte(ReporteDTO reporteDTO) {
        log.info("Creando reporte para usuario ID: {}", reporteDTO.getUsuarioId());
        validateReporteData(reporteDTO);

        // Validar existencia del usuario asociado
        if (reporteDTO.getUsuarioId() == null) {
            throw new IllegalArgumentException("El ID del usuario es obligatorio para crear un reporte.");
        }
        if (!usuarioDAO.existsById(reporteDTO.getUsuarioId())) {
            throw new RuntimeException("No existe un usuario con el ID: " + reporteDTO.getUsuarioId());
        }

        // Asignar fecha de creación
        reporteDTO.setCreatedAt(LocalDateTime.now());

        ReporteDTO saved = reporteDAO.save(reporteDTO);
        log.info("Reporte creado exitosamente con ID: {}", saved.getId());
        return saved;
    }

    // =================== OBTENER REPORTE POR ID ===================
    @Override
    @Transactional(readOnly = true)
    public ReporteDTO getReporteById(Long id) {
        log.debug("Buscando reporte con ID: {}", id);
        return reporteDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con ID: " + id));
    }

    // =================== LISTAR TODOS ===================
    @Override
    @Transactional(readOnly = true)
    public List<ReporteDTO> getAllReportes() {
        log.debug("Listando todos los reportes...");
        return reporteDAO.findAll();
    }


    // =================== LISTAR POR TIPO ===================
    @Override
    @Transactional(readOnly = true)
    public List<ReporteDTO> getReportesByTipo(String tipo) {
        log.debug("Buscando reportes del tipo: {}", tipo);
        return reporteDAO.findByTipo(tipo);
    }

    // =================== ELIMINAR ===================
    @Override
    public void deleteReporte(Long id) {
        log.warn("Eliminando reporte con ID: {}", id);
        getReporteById(id); // Lanza excepción si no existe
        boolean deleted = reporteDAO.deleteById(id);
        if (!deleted) {
            throw new RuntimeException("Error al eliminar reporte con ID: " + id);
        }
    }

    // =================== VALIDACIONES ===================
    private void validateReporteData(ReporteDTO dto) {
        if (dto.getTitulo() == null || dto.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título del reporte es obligatorio.");
        }
        if (dto.getTitulo().length() > 255) {
            throw new IllegalArgumentException("El título no puede exceder 255 caracteres.");
        }
        if (dto.getTipo() == null || dto.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de reporte es obligatorio.");
        }
        if (dto.getCantidadRegistros() == null || dto.getCantidadRegistros() < 0) {
            throw new IllegalArgumentException("La cantidad de registros debe ser un número positivo.");
        }
        if (dto.getContenido() == null || dto.getContenido().trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido del reporte no puede estar vacío.");
        }
    }
}
