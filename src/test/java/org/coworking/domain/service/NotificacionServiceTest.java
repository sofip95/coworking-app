package org.coworking.domain.service;

import org.coworking.domain.dto.NotificacionDTO;
import org.coworking.domain.service.impl.NotificacionServiceImpl;
import org.coworking.persistence.dao.NotificacionDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * UNIT TESTS para NotificacionServiceImpl
 *
 * OBJETIVO:
 * - Validar la lógica de negocio del servicio de notificaciones.
 * - Probar validaciones, excepciones y comportamientos esperados (CRUD, Filtros).
 * - Aislar de la base de datos usando Mockito.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("NotificacionService - Unit Tests")
class NotificacionServiceTest {

    @Mock
    private NotificacionDAO notificacionDAO;

    @InjectMocks
    private NotificacionServiceImpl notificacionService;

    private NotificacionDTO validNotificacion;
    private Long validId;

    @BeforeEach
    void setUp() {
        validId = 1L;
        validNotificacion = new NotificacionDTO();
        validNotificacion.setId(validId);
        validNotificacion.setUsuarioId(1L);
        validNotificacion.setTipo("CONFIRMACION");
        validNotificacion.setTitulo("Notificación de Prueba");
        validNotificacion.setMensaje("Mensaje de prueba");
        validNotificacion.setEstado("PENDIENTE");
        validNotificacion.setFechaEnvio(LocalDateTime.now());
    }

    // ==================== CREATE TESTS ====================

    @Test
    @DisplayName("CREATE - Notificación válida debe crearse exitosamente")
    void createNotificacion_ValidData_ShouldReturnCreatedNotificacion() {
        when(notificacionDAO.save(any(NotificacionDTO.class))).thenReturn(validNotificacion);

        NotificacionDTO result = notificacionService.createNotificacion(validNotificacion);

        assertThat(result).isNotNull();
        assertThat(result.getTitulo()).isEqualTo("Notificación de Prueba");
        assertThat(result.getEstado()).isEqualTo("PENDIENTE");
        verify(notificacionDAO, times(1)).save(any(NotificacionDTO.class));
    }

    @Test
    @DisplayName("CREATE - Usuario ID nulo debe lanzar RuntimeException")
    void createNotificacion_NullUsuarioId_ShouldThrowException() {
        validNotificacion.setUsuarioId(null);

        assertThatThrownBy(() -> notificacionService.createNotificacion(validNotificacion))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario ID no puede estar vacío");

        verify(notificacionDAO, never()).save(any());
    }

    @Test
    @DisplayName("CREATE - Tipo vacío debe lanzar RuntimeException")
    void createNotificacion_EmptyTipo_ShouldThrowException() {
        validNotificacion.setTipo("");

        assertThatThrownBy(() -> notificacionService.createNotificacion(validNotificacion))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Tipo de notificación no puede estar vacío");
    }

    @Test
    @DisplayName("CREATE - Título nulo debe lanzar RuntimeException")
    void createNotificacion_NullTitulo_ShouldThrowException() {
        validNotificacion.setTitulo(null);

        assertThatThrownBy(() -> notificacionService.createNotificacion(validNotificacion))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Título de notificación no puede estar vacío");
    }

    @Test
    @DisplayName("CREATE - Mensaje vacío debe lanzar RuntimeException")
    void createNotificacion_EmptyMensaje_ShouldThrowException() {
        validNotificacion.setMensaje(" ");

        assertThatThrownBy(() -> notificacionService.createNotificacion(validNotificacion))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Mensaje de notificación no puede estar vacío");
    }

    // ==================== READ TESTS ====================

    @Test
    @DisplayName("READ - Notificación existente debe retornarse correctamente")
    void getNotificacionById_ExistingId_ShouldReturnNotificacion() {
        when(notificacionDAO.findById(validId)).thenReturn(Optional.of(validNotificacion));

        NotificacionDTO result = notificacionService.getNotificacionById(validId);

        assertThat(result).isNotNull();
        assertThat(result.getTitulo()).isEqualTo("Notificación de Prueba");
        verify(notificacionDAO, times(1)).findById(validId);
    }

    @Test
    @DisplayName("READ - Notificación inexistente debe lanzar RuntimeException")
    void getNotificacionById_NonExistingId_ShouldThrowException() {
        when(notificacionDAO.findById(validId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> notificacionService.getNotificacionById(validId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Notificación no encontrada");
    }

    @Test
    @DisplayName("READ - Listar todas las notificaciones debe retornar lista")
    void getAllNotificaciones_ShouldReturnList() {
        when(notificacionDAO.findAll()).thenReturn(Arrays.asList(validNotificacion));

        List<NotificacionDTO> result = notificacionService.getAllNotificaciones();

        assertThat(result).hasSize(1);
        verify(notificacionDAO, times(1)).findAll();
    }

    @Test
    @DisplayName("READ - Buscar por usuario ID válido debe retornar lista")
    void findNotificacionesByUsuarioId_ValidUsuarioId_ShouldReturnList() {
        when(notificacionDAO.findByUsuarioId(1L)).thenReturn(Arrays.asList(validNotificacion));

        List<NotificacionDTO> result = notificacionService.findNotificacionesByUsuarioId(1L);

        assertThat(result).hasSize(1);
        verify(notificacionDAO, times(1)).findByUsuarioId(1L);
    }

    @Test
    @DisplayName("READ - Buscar por usuario ID nulo debe lanzar IllegalArgumentException")
    void findNotificacionesByUsuarioId_NullUsuarioId_ShouldThrowException() {
        assertThatThrownBy(() -> notificacionService.findNotificacionesByUsuarioId(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El ID del usuario no puede ser nulo");
    }

    @Test
    @DisplayName("READ - Buscar por tipo válido debe retornar lista")
    void findNotificacionesByTipo_ValidTipo_ShouldReturnList() {
        when(notificacionDAO.findByTipo("CONFIRMACION")).thenReturn(Arrays.asList(validNotificacion));

        List<NotificacionDTO> result = notificacionService.findNotificacionesByTipo("CONFIRMACION");

        assertThat(result).hasSize(1);
        verify(notificacionDAO, times(1)).findByTipo("CONFIRMACION");
    }

    @Test
    @DisplayName("READ - Buscar por tipo vacío debe lanzar IllegalArgumentException")
    void findNotificacionesByTipo_EmptyTipo_ShouldThrowException() {
        assertThatThrownBy(() -> notificacionService.findNotificacionesByTipo(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El tipo de notificación no puede ser nulo o vacío");
    }

    @Test
    @DisplayName("READ - Buscar por estado válido debe retornar lista")
    void findNotificacionesByEstado_ValidEstado_ShouldReturnList() {
        when(notificacionDAO.findByEstado("PENDIENTE")).thenReturn(Arrays.asList(validNotificacion));

        List<NotificacionDTO> result = notificacionService.findNotificacionesByEstado("PENDIENTE");

        assertThat(result).hasSize(1);
        verify(notificacionDAO, times(1)).findByEstado("PENDIENTE");
    }

    @Test
    @DisplayName("READ - Buscar por estado nulo debe lanzar IllegalArgumentException")
    void findNotificacionesByEstado_NullEstado_ShouldThrowException() {
        assertThatThrownBy(() -> notificacionService.findNotificacionesByEstado(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El estado de la notificación no puede ser nulo o vacío");
    }

    @Test
    @DisplayName("READ - Buscar por fecha de envío válida debe retornar lista")
    void findNotificacionesByFechaEnvio_ValidFecha_ShouldReturnList() {
        String fecha = "2023-10-19T10:30:00";
        when(notificacionDAO.findByFechaEnvio(fecha)).thenReturn(Arrays.asList(validNotificacion));

        List<NotificacionDTO> result = notificacionService.findNotificacionesByFechaEnvio(fecha);

        assertThat(result).hasSize(1);
        verify(notificacionDAO, times(1)).findByFechaEnvio(fecha);
    }

    @Test
    @DisplayName("READ - Buscar por fecha de envío vacía debe lanzar IllegalArgumentException")
    void findNotificacionesByFechaEnvio_EmptyFecha_ShouldThrowException() {
        assertThatThrownBy(() -> notificacionService.findNotificacionesByFechaEnvio(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La fecha de envío no puede ser nula o vacía");
    }

    // ==================== UPDATE TESTS ====================

    @Test
    @DisplayName("UPDATE - Datos válidos deben actualizar notificación correctamente")
    void updateNotificacion_ValidData_ShouldReturnUpdatedNotificacion() {
        NotificacionDTO updateData = new NotificacionDTO();
        updateData.setTitulo("Título Actualizado");
        updateData.setMensaje("Mensaje actualizado");

        when(notificacionDAO.findById(validId)).thenReturn(Optional.of(validNotificacion));
        when(notificacionDAO.update(eq(validId), any(NotificacionDTO.class)))
                .thenReturn(Optional.of(updateData));

        NotificacionDTO result = notificacionService.updateNotificacion(validId, updateData);

        assertThat(result.getTitulo()).isEqualTo("Título Actualizado");
        verify(notificacionDAO, times(1)).findById(validId);
        verify(notificacionDAO, times(1)).update(eq(validId), any(NotificacionDTO.class));
    }

    @Test
    @DisplayName("UPDATE - Notificación inexistente debe lanzar RuntimeException")
    void updateNotificacion_NonExistingId_ShouldThrowException() {
        when(notificacionDAO.findById(validId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> notificacionService.updateNotificacion(validId, validNotificacion))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Notificación no encontrada");
    }

    @Test
    @DisplayName("UPDATE - Tipo vacío debe lanzar RuntimeException")
    void updateNotificacion_EmptyTipo_ShouldThrowException() {
        NotificacionDTO updateData = new NotificacionDTO();
        updateData.setTipo("");

        when(notificacionDAO.findById(validId)).thenReturn(Optional.of(validNotificacion));

        assertThatThrownBy(() -> notificacionService.updateNotificacion(validId, updateData))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Tipo de notificación no puede estar vacío si se actualiza");
    }

    @Test
    @DisplayName("UPDATE - Título vacío debe lanzar RuntimeException")
    void updateNotificacion_EmptyTitulo_ShouldThrowException() {
        NotificacionDTO updateData = new NotificacionDTO();
        updateData.setTitulo(" ");

        when(notificacionDAO.findById(validId)).thenReturn(Optional.of(validNotificacion));

        assertThatThrownBy(() -> notificacionService.updateNotificacion(validId, updateData))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Título de notificación no puede estar vacío si se actualiza");
    }

    @Test
    @DisplayName("UPDATE - Mensaje vacío debe lanzar RuntimeException")
    void updateNotificacion_EmptyMensaje_ShouldThrowException() {
        NotificacionDTO updateData = new NotificacionDTO();
        updateData.setMensaje("");

        when(notificacionDAO.findById(validId)).thenReturn(Optional.of(validNotificacion));

        assertThatThrownBy(() -> notificacionService.updateNotificacion(validId, updateData))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Mensaje de notificación no puede estar vacío si se actualiza");
    }

    // ==================== DELETE TESTS ====================

    @Test
    @DisplayName("DELETE - Notificación existente debe eliminarse correctamente")
    void deleteNotificacion_ExistingId_ShouldCompleteWithoutError() {
        when(notificacionDAO.findById(validId)).thenReturn(Optional.of(validNotificacion));
        when(notificacionDAO.deleteById(validId)).thenReturn(true);

        assertThatCode(() -> notificacionService.deleteNotificacion(validId))
                .doesNotThrowAnyException();

        verify(notificacionDAO, times(1)).findById(validId);
        verify(notificacionDAO, times(1)).deleteById(validId);
    }

    @Test
    @DisplayName("DELETE - Notificación inexistente debe lanzar RuntimeException")
    void deleteNotificacion_NonExistingId_ShouldThrowException() {
        when(notificacionDAO.findById(validId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> notificacionService.deleteNotificacion(validId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Notificación no encontrada");
    }

    @Test
    @DisplayName("DELETE - Error al eliminar debe lanzar RuntimeException")
    void deleteNotificacion_DeleteFails_ShouldThrowException() {
        when(notificacionDAO.findById(validId)).thenReturn(Optional.of(validNotificacion));
        when(notificacionDAO.deleteById(validId)).thenReturn(false);

        assertThatThrownBy(() -> notificacionService.deleteNotificacion(validId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Notificación no encontrada");
    }
}
