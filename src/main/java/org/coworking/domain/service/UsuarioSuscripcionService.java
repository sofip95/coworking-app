package org.coworking.domain.service;

import org.coworking.domain.dto.UsuarioSuscripcionDTO;
import java.util.List;

public interface UsuarioSuscripcionService {

    /** Crear una nueva relación usuario-suscripción
     *
     * VALIDACIONES:
     * - Datos obligatorios deben estar completos
     * - La suscripción debe existir
     * - El usuario debe existir
     *
     * @param usuarioSuscripcionDTO Datos de la relación a crear
     * @return UsuarioSuscripcionDTO con ID generado y timestamps
     */
    UsuarioSuscripcionDTO createUsuarioSuscripcion(UsuarioSuscripcionDTO usuarioSuscripcionDTO);

    /** Obtener una relación usuario-suscripción por su ID
     *
     * @param id ID de la relación a buscar
     * @return UsuarioSuscripcionDTO si se encuentra
     * @throws RuntimeException si no se encuentra
     */
    UsuarioSuscripcionDTO getUsuarioSuscripcionById(Long id);

    /** Listar todas las relaciones usuario-suscripción
     *
     * @return Lista de todas las relaciones en el sistema
     */
    List<UsuarioSuscripcionDTO> getAllUsuarioSuscripciones();

    /** Actualizar una relación usuario-suscripción existente
     *
     * VALIDACIONES:
     * - La relación debe existir
     * - La suscripción debe existir si se actualiza
     * - El usuario debe existir si se actualiza
     *
     * @param id ID de la relación a actualizar
     * @param usuarioSuscripcionDTO Datos de la relación a actualizar (campos null se ignoran)
     * @return UsuarioSuscripcionDTO actualizado
     * @throws RuntimeException si la relación no existe
     */
    UsuarioSuscripcionDTO updateUsuarioSuscripcion(Long id, UsuarioSuscripcionDTO usuarioSuscripcionDTO);

    /** Eliminar una relación usuario-suscripción por su ID
     *
     * @param id ID de la relación a eliminar
     * @throws RuntimeException si la relación no existe
     * @return true si se eliminó, false si no existía
     */
    void deleteUsuarioSuscripcion(Long id);

}