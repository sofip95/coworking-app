package org.coworking.domain.service.impl;

import org.coworking.domain.dto.NotificacionDTO;
import org.coworking.domain.dto.SuscripcionDTO;
import org.coworking.domain.dto.UsuarioSuscripcionDTO;
import org.coworking.domain.service.NotificacionService;
import org.coworking.domain.service.UsuarioSuscripcionService;
import org.coworking.persistence.dao.SuscripcionDAO;
import org.coworking.persistence.dao.UsuarioSuscripcionDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UsuarioSuscripcionServiceImpl implements UsuarioSuscripcionService {

    private final UsuarioSuscripcionDAO usuarioSuscripcionDAO;
    private final SuscripcionDAO suscripcionDAO;
    private final NotificacionService notificacionService;

    /**
     * Create - crear una nueva relación usuario-suscripción
     *
     * FLUJO:
     * 1. Validar datos de entrada (datos obligatorios, existencia de usuario y suscripción)
     * 2. Calcular fecha de fin basada en la suscripción
     * 3. Usar DAO.save() para persistir
     * 4. Retornar DTO con ID y timestamps
     */
    @Override
    public UsuarioSuscripcionDTO createUsuarioSuscripcion(UsuarioSuscripcionDTO usuarioSuscripcionDTO) {
        log.info("Creando relación usuario-suscripción: {}", usuarioSuscripcionDTO);
        validateUsuarioSuscripcionData(usuarioSuscripcionDTO);

        if (usuarioTieneSuscripcionActiva(usuarioSuscripcionDTO.getUsuarioId())) {
            throw new IllegalArgumentException("El usuario con ID " + usuarioSuscripcionDTO.getUsuarioId() + " ya tiene una suscripción activa");
        }

        usuarioSuscripcionDTO.setFechaInicio(LocalDateTime.now());
        usuarioSuscripcionDTO.setFechaFin(calcularFechaFin(usuarioSuscripcionDTO));

        UsuarioSuscripcionDTO result = usuarioSuscripcionDAO.save(usuarioSuscripcionDTO);
        log.info("Relación usuario-suscripción creada con ID: {}", result.getId(),
                "Con usuario de ID: {}", result.getUsuarioId(), "y suscripción ID: {}", result.getSuscripcionId());

        // Enviar notificación automática de confirmación de suscripción
        enviarNotificacionSuscripcion(result);

        return result;
    }

    /**
     * Read - obtener una relación usuario-suscripción por su ID
     */
    @Override
    @Transactional(readOnly = true)
    public UsuarioSuscripcionDTO getUsuarioSuscripcionById(Long id) {
        log.debug("Obteniendo relación usuario-suscripción con ID: {}", id);
        return usuarioSuscripcionDAO.findById(id)
                .orElseThrow(() -> {
                    log.warn("Relación usuario-suscripción no encontrada con ID: {}", id);
                    return new RuntimeException("Relación usuario-suscripción no encontrada con ID: " + id);
                });
    }

    /**
     * Read - listar todas las relaciones usuario-suscripción
     */
    @Override
    @Transactional(readOnly = true)
    public List<UsuarioSuscripcionDTO> getAllUsuarioSuscripciones() {
        log.debug("Obteniendo todas las relaciones usuario-suscripción");
        return usuarioSuscripcionDAO.findAll();
    }

    /**
     * Update - actualizar una relación usuario-suscripción existente
     *
     * FLUJO:
     * 1. Validar datos de entrada (existencia, existencia de usuario y suscripción si se actualizan)
     * 2. Usar DAO.update() para persistir cambios
     * 3. Retornar DTO actualizado
     */
    @Override
    public UsuarioSuscripcionDTO updateUsuarioSuscripcion(Long id, UsuarioSuscripcionDTO usuarioSuscripcionDTO) {
        log.info("Actualizando relación usuario-suscripción con ID: {}", id);

        getUsuarioSuscripcionById(id);

        validateUsuarioSuscripcionUpdateData(usuarioSuscripcionDTO);

        UsuarioSuscripcionDTO updated = usuarioSuscripcionDAO.update(usuarioSuscripcionDTO)
                .orElseThrow(() -> new RuntimeException("Relación usuario-suscripción no encontrada con ID: " + id));

        log.info("Relación usuario-suscripción actualizada exitosamente ID: {}", id);
        return updated;
    }

    /**
     * Delete - eliminar una relación usuario-suscripción por su ID
     *
     * FLUJO:
     * 1. Verificar existencia con DAO.findById()
     * 2. Si existe, usar DAO.delete() para eliminar
     * 3. Si no existe, lanzar excepción
     */
    @Override
    public void deleteUsuarioSuscripcion(Long id) {
        log.info("Eliminando relación usuario-suscripción con ID: {}", id);

        getUsuarioSuscripcionById(id);

        boolean deleted = usuarioSuscripcionDAO.deleteById(id);

        if (!deleted) {
            log.warn("No se pudo eliminar, relación usuario-suscripción no encontrada con ID: {}", id);
            throw new RuntimeException("Relación usuario-suscripción no encontrada con ID: " + id);
        }

        log.info("Relación usuario-suscripción eliminada exitosamente ID: {}", id);
    }

    /**
     * Validar los datos de la relación usuario-suscripción
     */
    private void validateUsuarioSuscripcionData(UsuarioSuscripcionDTO usuarioSuscripcionDTO) {
        if (usuarioSuscripcionDTO.getUsuarioId() == null) {
            throw new IllegalArgumentException("El ID del usuario es obligatorio");
        }
        if (usuarioSuscripcionDTO.getSuscripcionId() == null) {
            throw new IllegalArgumentException("El ID de la suscripción es obligatorio");
        }

        if (!usuarioSuscripcionDAO.usuarioExists(usuarioSuscripcionDTO.getUsuarioId())) {
            throw new IllegalArgumentException("El usuario con ID " + usuarioSuscripcionDTO.getUsuarioId() + " no existe");
        }
        if (!usuarioSuscripcionDAO.suscripcionExists(usuarioSuscripcionDTO.getSuscripcionId())) {
            throw new IllegalArgumentException("La suscripción con ID " + usuarioSuscripcionDTO.getSuscripcionId() + " no existe");
        }
    }

    private void validateUsuarioSuscripcionUpdateData(UsuarioSuscripcionDTO usuarioSuscripcionDTO) {
        if (usuarioSuscripcionDTO.getUsuarioId() != null &&
                !usuarioSuscripcionDAO.usuarioExists(usuarioSuscripcionDTO.getUsuarioId())) {
            throw new IllegalArgumentException("El usuario con ID " + usuarioSuscripcionDTO.getUsuarioId() + " no existe");
        }
        if (usuarioSuscripcionDTO.getSuscripcionId() != null &&
                !usuarioSuscripcionDAO.suscripcionExists(usuarioSuscripcionDTO.getSuscripcionId())) {
            throw new IllegalArgumentException("La suscripción con ID " + usuarioSuscripcionDTO.getSuscripcionId() + " no existe");
        }
    }

    private LocalDateTime calcularFechaFin(UsuarioSuscripcionDTO usuarioSuscripcionDTO) {
        LocalDateTime fechaInicio = usuarioSuscripcionDTO.getFechaInicio();
        if (fechaInicio == null) {
            fechaInicio = LocalDateTime.now();
        }
        SuscripcionDTO suscripcionDTO = suscripcionDAO.findById(usuarioSuscripcionDTO.getSuscripcionId())
                .orElseThrow(() -> new RuntimeException("Suscripción no encontrada con ID: " + usuarioSuscripcionDTO.getSuscripcionId()));
        switch (suscripcionDTO.getDuracion()) {
            case "MENSUAL":
                return fechaInicio.plusMonths(1);
            case "TRIMESTRAL":
                return fechaInicio.plusMonths(3);
            case "SEMESTRAL":
                return fechaInicio.plusMonths(6);
            case "ANUAL":
                return fechaInicio.plusYears(1);
            default:
                throw new IllegalArgumentException("Tipo de suscripción inválido: " + suscripcionDTO.getTipo());
        }
    }

    /**
     * Validar si un usuario ya tiene una suscripción activa
     */
    public boolean usuarioTieneSuscripcionActiva(Long usuarioId) {
        List<UsuarioSuscripcionDTO> suscripciones = usuarioSuscripcionDAO.findByUsuarioId(usuarioId);
        for (UsuarioSuscripcionDTO suscripcion : suscripciones) {
            if ("ACTIVA".equalsIgnoreCase(suscripcion.getEstado())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Enviar notificación automática de confirmación de suscripción
     */
    private void enviarNotificacionSuscripcion(UsuarioSuscripcionDTO usuarioSuscripcionDTO) {
        try {
            SuscripcionDTO suscripcionDTO = suscripcionDAO.findById(usuarioSuscripcionDTO.getSuscripcionId())
                    .orElseThrow(() -> new RuntimeException("Suscripción no encontrada con ID: " + usuarioSuscripcionDTO.getSuscripcionId()));

            NotificacionDTO notificacion = new NotificacionDTO();
            notificacion.setUsuarioId(usuarioSuscripcionDTO.getUsuarioId());
            notificacion.setTipo("CONFIRMACION");
            notificacion.setTitulo("Suscripción Confirmada");
            notificacion.setMensaje(String.format("Su suscripción '%s' ha sido activada exitosamente. ID de suscripción: %d",
                    suscripcionDTO.getNombre(), usuarioSuscripcionDTO.getId()));

            notificacionService.createNotificacion(notificacion);
            log.info("Notificación de suscripción enviada al usuario ID: {}", usuarioSuscripcionDTO.getUsuarioId());
        } catch (Exception e) {
            log.error("Error al enviar notificación de suscripción: {}", e.getMessage());
            // No lanzamos excepción para no interrumpir el flujo de suscripción
        }
    }

}
