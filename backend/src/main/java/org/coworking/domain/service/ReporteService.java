package org.coworking.domain.service;

import org.coworking.domain.dto.ReporteDTO;
import java.util.List;

/**
 * Interfaz del servicio de reportes.
 *
 * RESPONSABILIDADES:
 * - Validar datos del reporte antes de guardar
 * - Manejar la lógica de generación, consulta y eliminación de reportes
 * - Aplicar reglas de negocio antes de acceder al DAO
 */
public interface ReporteService {

    /**
     * Crear un nuevo reporte.
     *
     * VALIDACIONES:
     * - El tipo de reporte debe ser válido (OCUPACION, INGRESOS, USUARIOS, USO_RECURSOS)
     * - El título y contenido no deben ser nulos ni vacíos
     *
     * @param reporteDTO Datos del reporte a crear
     * @return DTO del reporte creado con ID generado
     * @throws IllegalArgumentException Si los datos no son válidos
     */
    ReporteDTO createReporte(ReporteDTO reporteDTO);

    /**
     * Obtener un reporte por su ID.
     *
     * @param id ID del reporte
     * @return DTO del reporte encontrado
     * @throws RuntimeException Si el reporte no existe
     */
    ReporteDTO getReporteById(Long id);

    /**
     * Obtener todos los reportes registrados.
     *
     * @return Lista completa de reportes
     */
    List<ReporteDTO> getAllReportes();

    /**
     * Eliminar un reporte por su ID.
     *
     * @param id ID del reporte a eliminar
     * @throws RuntimeException Si el reporte no existe
     */
    void deleteReporte(Long id);

    /**
     * Buscar reportes por tipo (ejemplo: "INGRESOS", "OCUPACION").
     *
     * @param tipo Tipo de reporte
     * @return Lista de reportes del tipo indicado
     * @throws IllegalArgumentException Si el tipo es nulo o vacío
     */
    List<ReporteDTO> getReportesByTipo(String tipo);

}
