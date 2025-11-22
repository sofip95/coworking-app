package org.coworking.persistence.dao;

import lombok.RequiredArgsConstructor;
import org.coworking.domain.dto.ReporteDTO;
import org.coworking.persistence.entity.ReporteEntity;
import org.coworking.persistence.mapper.ReporteMapper;
import org.coworking.persistence.repository.ReporteRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * DAO para manejar operaciones CRUD de reportes.
 *
 * FLUJO GENERAL:
 * - Convierte entre DTO y Entity usando ReporteMapper.
 * - Usa ReporteRepository para interactuar con la base de datos.
 * - Devuelve DTOs al servicio o controlador.
 */
@Repository
@RequiredArgsConstructor
public class ReporteDAO {

    private final ReporteRepository reporteRepository;
    private final ReporteMapper reporteMapper;

    /**
     * CREATE - Crear un nuevo reporte.
     */
    public ReporteDTO save(ReporteDTO reporteDTO) {
        ReporteEntity entity = reporteMapper.toEntity(reporteDTO);
        ReporteEntity savedEntity = reporteRepository.save(entity);
        return reporteMapper.toDTO(savedEntity);
    }

    /**
     * READ - Buscar reporte por ID.
     */
    public Optional<ReporteDTO> findById(Long id) {
        return reporteRepository.findById(id)
                .map(reporteMapper::toDTO);
    }

    /**
     * READ ALL - Buscar todos los reportes.
     */
    public List<ReporteDTO> findAll() {
        List<ReporteEntity> entities = reporteRepository.findAll();
        return reporteMapper.toDTOList(entities);
    }

    /**
     * READ - Buscar reportes por tipo.
     * Ejemplo: tipo = "OCUPACION", "INGRESOS", "USUARIOS"
     */
    public List<ReporteDTO> findByTipo(String tipo) {
        List<ReporteEntity> entities = reporteRepository.findByTipo(tipo);
        return reporteMapper.toDTOList(entities);
    }

    /**
     * DELETE - Eliminar reporte por ID.
     *
     * @return true si se eliminó correctamente, false si no existía.
     */
    public boolean deleteById(Long id) {
        if (reporteRepository.existsById(id)) {
            reporteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
