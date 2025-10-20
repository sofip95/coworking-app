package org.coworking.domain.service;

import org.coworking.domain.dto.NotificacionDTO;
import org.coworking.domain.dto.SuscripcionDTO;
import org.coworking.domain.dto.UsuarioSuscripcionDTO;
import org.coworking.domain.service.impl.UsuarioSuscripcionServiceImpl;
import org.coworking.persistence.dao.SuscripcionDAO;
import org.coworking.persistence.dao.UsuarioSuscripcionDAO;
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
import static org.mockito.Mockito.*;

/**
 * UNIT TESTS para UsuarioSuscripcionServiceImpl
 *
 * OBJETIVO:
 * - Validar la lógica de negocio del servicio de usuario-suscripción.
 * - Probar validaciones, excepciones y comportamientos esperados (CRUD).
 * - Aislar de la base de datos usando Mockito.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioSuscripcionService - Unit Tests")
class UsuarioSuscripcionServiceTest {

    @Mock
    private UsuarioSuscripcionDAO usuarioSuscripcionDAO;

    @Mock
    private SuscripcionDAO suscripcionDAO;

    @Mock
    private NotificacionService notificacionService;

    @InjectMocks
    private UsuarioSuscripcionServiceImpl usuarioSuscripcionService;

    private UsuarioSuscripcionDTO validUsuarioSuscripcion;
    private SuscripcionDTO validSuscripcion;
    private Long validId;

    @BeforeEach
    void setUp() {
        validId = 1L;
        validSuscripcion = new SuscripcionDTO();
        validSuscripcion.setId(1L);
        validSuscripcion.setNombre("Suscripción Básica");
        validSuscripcion.setTipo("INDIVIDUAL");
        validSuscripcion.setDuracion("MENSUAL");

        validUsuarioSuscripcion = new UsuarioSuscripcionDTO();
        validUsuarioSuscripcion.setId(validId);
        validUsuarioSuscripcion.setUsuarioId(1L);
        validUsuarioSuscripcion.setSuscripcionId(1L);
        validUsuarioSuscripcion.setEstado("ACTIVA");
        validUsuarioSuscripcion.setFechaInicio(LocalDateTime.now());
        validUsuarioSuscripcion.setFechaFin(LocalDateTime.now().plusMonths(1));
    }

    // ==================== CREATE TESTS ====================

    @Test
    @DisplayName("CREATE - Usuario-suscripción válida debe crearse exitosamente")
    void createUsuarioSuscripcion_ValidData_ShouldReturnCreatedUsuarioSuscripcion() {
        when(usuarioSuscripcionDAO.usuarioExists(1L)).thenReturn(true);
        when(usuarioSuscripcionDAO.suscripcionExists(1L)).thenReturn(true);
        when(usuarioSuscripcionDAO.findByUsuarioId(1L)).thenReturn(Arrays.asList());
        when(suscripcionDAO.findById(1L)).thenReturn(Optional.of(validSuscripcion));
        when(usuarioSuscripcionDAO.save(any(UsuarioSuscripcionDTO.class))).thenReturn(validUsuarioSuscripcion);

        UsuarioSuscripcionDTO result = usuarioSuscripcionService.createUsuarioSuscripcion(validUsuarioSuscripcion);

        assertThat(result).isNotNull();
        assertThat(result.getEstado()).isEqualTo("ACTIVA");
        verify(usuarioSuscripcionDAO, times(1)).save(any(UsuarioSuscripcionDTO.class));
        verify(notificacionService, times(1)).createNotificacion(any(NotificacionDTO.class));
    }

    @Test
    @DisplayName("CREATE - Usuario ID nulo debe lanzar IllegalArgumentException")
    void createUsuarioSuscripcion_NullUsuarioId_ShouldThrowException() {
        validUsuarioSuscripcion.setUsuarioId(null);

        assertThatThrownBy(() -> usuarioSuscripcionService.createUsuarioSuscripcion(validUsuarioSuscripcion))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El ID del usuario es obligatorio");

        verify(usuarioSuscripcionDAO, never()).save(any());
    }

    @Test
    @DisplayName("CREATE - Suscripción ID nulo debe lanzar IllegalArgumentException")
    void createUsuarioSuscripcion_NullSuscripcionId_ShouldThrowException() {
        validUsuarioSuscripcion.setSuscripcionId(null);

        assertThatThrownBy(() -> usuarioSuscripcionService.createUsuarioSuscripcion(validUsuarioSuscripcion))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El ID de la suscripción es obligatorio");
    }

    @Test
    @DisplayName("CREATE - Usuario inexistente debe lanzar IllegalArgumentException")
    void createUsuarioSuscripcion_NonExistingUsuario_ShouldThrowException() {
        when(usuarioSuscripcionDAO.usuarioExists(1L)).thenReturn(false);

        assertThatThrownBy(() -> usuarioSuscripcionService.createUsuarioSuscripcion(validUsuarioSuscripcion))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El usuario con ID 1 no existe");

        verify(usuarioSuscripcionDAO, never()).save(any());
    }

    @Test
    @DisplayName("CREATE - Suscripción inexistente debe lanzar IllegalArgumentException")
    void createUsuarioSuscripcion_NonExistingSuscripcion_ShouldThrowException() {
        when(usuarioSuscripcionDAO.usuarioExists(1L)).thenReturn(true);
        when(usuarioSuscripcionDAO.suscripcionExists(1L)).thenReturn(false);

        assertThatThrownBy(() -> usuarioSuscripcionService.createUsuarioSuscripcion(validUsuarioSuscripcion))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La suscripción con ID 1 no existe");

        verify(usuarioSuscripcionDAO, never()).save(any());
    }

    @Test
    @DisplayName("CREATE - Usuario con suscripción activa debe lanzar IllegalArgumentException")
    void createUsuarioSuscripcion_UsuarioWithActiveSuscripcion_ShouldThrowException() {
        UsuarioSuscripcionDTO activeSuscripcion = new UsuarioSuscripcionDTO();
        activeSuscripcion.setEstado("ACTIVA");

        when(usuarioSuscripcionDAO.usuarioExists(1L)).thenReturn(true);
        when(usuarioSuscripcionDAO.suscripcionExists(1L)).thenReturn(true);
        when(usuarioSuscripcionDAO.findByUsuarioId(1L)).thenReturn(Arrays.asList(activeSuscripcion));

        assertThatThrownBy(() -> usuarioSuscripcionService.createUsuarioSuscripcion(validUsuarioSuscripcion))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El usuario con ID 1 ya tiene una suscripción activa");

        verify(usuarioSuscripcionDAO, never()).save(any());
    }

    // ==================== READ TESTS ====================

    @Test
    @DisplayName("READ - Usuario-suscripción existente debe retornarse correctamente")
    void getUsuarioSuscripcionById_ExistingId_ShouldReturnUsuarioSuscripcion() {
        when(usuarioSuscripcionDAO.findById(validId)).thenReturn(Optional.of(validUsuarioSuscripcion));

        UsuarioSuscripcionDTO result = usuarioSuscripcionService.getUsuarioSuscripcionById(validId);

        assertThat(result).isNotNull();
        assertThat(result.getEstado()).isEqualTo("ACTIVA");
        verify(usuarioSuscripcionDAO, times(1)).findById(validId);
    }

    @Test
    @DisplayName("READ - Usuario-suscripción inexistente debe lanzar RuntimeException")
    void getUsuarioSuscripcionById_NonExistingId_ShouldThrowException() {
        when(usuarioSuscripcionDAO.findById(validId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioSuscripcionService.getUsuarioSuscripcionById(validId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Relación usuario-suscripción no encontrada");
    }

    @Test
    @DisplayName("READ - Listar todas las usuario-suscripciones debe retornar lista")
    void getAllUsuarioSuscripciones_ShouldReturnList() {
        when(usuarioSuscripcionDAO.findAll()).thenReturn(Arrays.asList(validUsuarioSuscripcion));

        List<UsuarioSuscripcionDTO> result = usuarioSuscripcionService.getAllUsuarioSuscripciones();

        assertThat(result).hasSize(1);
        verify(usuarioSuscripcionDAO, times(1)).findAll();
    }

    // ==================== UPDATE TESTS ====================

    @Test
    @DisplayName("UPDATE - Datos válidos deben actualizar usuario-suscripción correctamente")
    void updateUsuarioSuscripcion_ValidData_ShouldReturnUpdatedUsuarioSuscripcion() {
        UsuarioSuscripcionDTO updateData = new UsuarioSuscripcionDTO();
        updateData.setEstado("INACTIVA");

        when(usuarioSuscripcionDAO.findById(validId)).thenReturn(Optional.of(validUsuarioSuscripcion));
        when(usuarioSuscripcionDAO.update(any(UsuarioSuscripcionDTO.class)))
                .thenReturn(Optional.of(updateData));

        UsuarioSuscripcionDTO result = usuarioSuscripcionService.updateUsuarioSuscripcion(validId, updateData);

        assertThat(result.getEstado()).isEqualTo("INACTIVA");
        verify(usuarioSuscripcionDAO, times(1)).findById(validId);
        verify(usuarioSuscripcionDAO, times(1)).update(any(UsuarioSuscripcionDTO.class));
    }

    @Test
    @DisplayName("UPDATE - Usuario-suscripción inexistente debe lanzar RuntimeException")
    void updateUsuarioSuscripcion_NonExistingId_ShouldThrowException() {
        when(usuarioSuscripcionDAO.findById(validId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioSuscripcionService.updateUsuarioSuscripcion(validId, validUsuarioSuscripcion))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Relación usuario-suscripción no encontrada");
    }

    @Test
    @DisplayName("UPDATE - Usuario inexistente debe lanzar IllegalArgumentException")
    void updateUsuarioSuscripcion_NonExistingUsuario_ShouldThrowException() {
        UsuarioSuscripcionDTO updateData = new UsuarioSuscripcionDTO();
        updateData.setUsuarioId(999L);

        when(usuarioSuscripcionDAO.findById(validId)).thenReturn(Optional.of(validUsuarioSuscripcion));
        when(usuarioSuscripcionDAO.usuarioExists(999L)).thenReturn(false);

        assertThatThrownBy(() -> usuarioSuscripcionService.updateUsuarioSuscripcion(validId, updateData))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El usuario con ID 999 no existe");
    }

    @Test
    @DisplayName("UPDATE - Suscripción inexistente debe lanzar IllegalArgumentException")
    void updateUsuarioSuscripcion_NonExistingSuscripcion_ShouldThrowException() {
        UsuarioSuscripcionDTO updateData = new UsuarioSuscripcionDTO();
        updateData.setSuscripcionId(999L);

        when(usuarioSuscripcionDAO.findById(validId)).thenReturn(Optional.of(validUsuarioSuscripcion));
        when(usuarioSuscripcionDAO.suscripcionExists(999L)).thenReturn(false);

        assertThatThrownBy(() -> usuarioSuscripcionService.updateUsuarioSuscripcion(validId, updateData))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La suscripción con ID 999 no existe");
    }

    // ==================== DELETE TESTS ====================

    @Test
    @DisplayName("DELETE - Usuario-suscripción existente debe eliminarse correctamente")
    void deleteUsuarioSuscripcion_ExistingId_ShouldCompleteWithoutError() {
        when(usuarioSuscripcionDAO.findById(validId)).thenReturn(Optional.of(validUsuarioSuscripcion));
        when(usuarioSuscripcionDAO.deleteById(validId)).thenReturn(true);

        assertThatCode(() -> usuarioSuscripcionService.deleteUsuarioSuscripcion(validId))
                .doesNotThrowAnyException();

        verify(usuarioSuscripcionDAO, times(1)).findById(validId);
        verify(usuarioSuscripcionDAO, times(1)).deleteById(validId);
    }

    @Test
    @DisplayName("DELETE - Usuario-suscripción inexistente debe lanzar RuntimeException")
    void deleteUsuarioSuscripcion_NonExistingId_ShouldThrowException() {
        when(usuarioSuscripcionDAO.findById(validId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioSuscripcionService.deleteUsuarioSuscripcion(validId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Relación usuario-suscripción no encontrada");
    }

    @Test
    @DisplayName("DELETE - Error al eliminar debe lanzar RuntimeException")
    void deleteUsuarioSuscripcion_DeleteFails_ShouldThrowException() {
        when(usuarioSuscripcionDAO.findById(validId)).thenReturn(Optional.of(validUsuarioSuscripcion));
        when(usuarioSuscripcionDAO.deleteById(validId)).thenReturn(false);

        assertThatThrownBy(() -> usuarioSuscripcionService.deleteUsuarioSuscripcion(validId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Relación usuario-suscripción no encontrada");
    }
}
