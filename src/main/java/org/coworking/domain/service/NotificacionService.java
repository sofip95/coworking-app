package org.coworking.domain.service;

import org.coworking.domain.dto.NotificacionDTO;
import java.util.List;

public interface NotificacionService {

    /** Crear una nueva notificación
     *
     * VALIDACIONES:
     * - El mensaje no debe estar vacío
     * - Datos obligatorios deben estar completos
     *
     * @param notificacionDTO Datos de la notificación a crear
     * @return NotificacionDTO con ID generado y timestamps
     */
    NotificacionDTO createNotificacion(NotificacionDTO notificacionDTO);

    /** Obtener una notificación por su ID
     *
     * @param id ID de la notificación a buscar
     * @return NotificacionDTO si se encuentra
     * @throws RuntimeException si no se encuentra
     */
    NotificacionDTO getNotificacionById(Long id);

    /** Listar todas las notificaciones
     *
     * @return Lista de todas las notificaciones en el sistema
     */
    List<NotificacionDTO> getAllNotificaciones();

    /** Actualizar una notificación existente
     *
     * VALIDACIONES:
     * - La notificación debe existir
     * - El mensaje no debe estar vacío si se actualiza
     *
     * @param id ID de la notificación a actualizar
     * @param notificacionDTO Datos de la notificación a actualizar (campos null se ignoran)
     * @return NotificacionDTO actualizado
     * @throws RuntimeException si la notificación no existe
     */
    NotificacionDTO updateNotificacion(Long id, NotificacionDTO notificacionDTO);

    /** Eliminar una notificación por su ID
     *
     * @param id ID de la notificación a eliminar
     * @throws RuntimeException si la notificación no existe
     * @return true si se eliminó, false si no existía
     */
    void deleteNotificacion(Long id);

    /** Buscar notificaciones por usuarioId
     *
     * @param usuarioId ID del usuario
     * @return Lista de notificaciones del usuario
     */
    List<NotificacionDTO> findNotificacionesByUsuarioId(Long usuarioId);

    /** Buscar notificaciones por tipo
     *
     * @param tipo Tipo de notificación
     * @return Lista de notificaciones del tipo especificado
     */
    List<NotificacionDTO> findNotificacionesByTipo(String tipo);

    /** Buscar notificaciones por estado
     *
     * @param estado Estado de la notificación
     * @return Lista de notificaciones con el estado especificado
     */
    List<NotificacionDTO> findNotificacionesByEstado(String estado);

    /** Buscar notificaciones enviadas por fecha
     *
     * @param fechaEnvio Fecha de envío en formato ISO (ej: "2023-10-19T10:30:00")
     * @return Lista de notificaciones enviadas en esa fecha
     */
    List<NotificacionDTO> findNotificacionesByFechaEnvio(String fechaEnvio);

}
