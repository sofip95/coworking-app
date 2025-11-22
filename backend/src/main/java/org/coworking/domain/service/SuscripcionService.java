package org.coworking.domain.service;

import org.coworking.domain.dto.SuscripcionDTO;
import java.util.List;

/**
 * Interfaz del servicio de gestión de suscripciones.
 */
public interface SuscripcionService {

    /** Crear una nueva suscripción
     *
     * VALIDACIONES:
     * - Precio debe ser positivo
     * - Datos obligatorios deben estar completos
     *
     * @param suscripcionDTO Datos de la suscripción a crear
     * @return SuscripcionDTO con ID generado y timestamps
     * @throws IllegalArgumentException Si los datos no son válidos
     */
    SuscripcionDTO createSuscripcion(SuscripcionDTO suscripcionDTO);

    /** Obtener una suscripción por su ID
     *
     * @param id ID de la suscripción a buscar
     * @return SuscripcionDTO si se encuentra
     * @throws RuntimeException si no se encuentra
     */
    SuscripcionDTO getSuscripcionById(Long id);

    /** Listar todas las suscripciones
     *
     * @return Lista de todas las suscripciones en el sistema
     */
    List<SuscripcionDTO> getAllSuscripciones();

    /** Actualizar una suscripción existente
     *
     * VALIDACIONES:
     * - La suscripción debe existir
     * - Precio debe ser positivo si se actualiza
     *
     * @param id ID de la suscripción a actualizar
     * @param suscripcionDTO Datos de la suscripción a actualizar (campos null se ignoran)
     * @return SuscripcionDTO actualizado
     * @throws RuntimeException si la suscripción no existe
     */
    SuscripcionDTO updateSuscripcion(Long id, SuscripcionDTO suscripcionDTO);

    /** Eliminar una suscripción por su ID
     *
     * @param id ID de la suscripción a eliminar
     * @throws RuntimeException si la suscripción no existe
     * @return true si se eliminó, false si no existía
     */
    void deleteSuscripcion(Long id);

    /**
     * Buscar suscripciones por tipo
     *
     * @param tipo Tipo de suscripción a buscar
     * @return Lista de suscripciones que coinciden con el tipo dado
     * @throws RuntimeException si no se encuentran suscripciones
     */
    List<SuscripcionDTO> findSuscripcionesByTipo(String tipo);

    /**
     * Buscar suscripciones por duracion
     *
     * @param duracion Duración de suscripción a buscar
     * @return Lista de suscripciones que coinciden con la duración dada
     * @throws RuntimeException si no se encuentran suscripciones
     */
    List<SuscripcionDTO> findSuscripcionesByDuracion(String duracion);

}
