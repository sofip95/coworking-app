package org.coworking.domain.service;

import org.coworking.domain.dto.SuscripcionDTO;
import org.coworking.domain.service.impl.SuscripcionServiceImpl;
import org.coworking.persistence.dao.SuscripcionDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * UNIT TESTS para SuscripcionServiceImpl
 *
 * OBJETIVO:
 * - Validar la lógica de negocio del servicio de suscripciones.
 * - Probar validaciones, excepciones y comportamientos esperados (CRUD, Filtros).
 * - Aislar de la base de datos usando Mockito.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SuscripcionService - Unit Tests")
class SuscripcionServiceTest {

    @Mock
    private SuscripcionDAO suscripcionDAO;

    @InjectMocks
    private SuscripcionServiceImpl suscripcionService;

    private SuscripcionDTO validSuscripcion;
    private Long validId;

    @BeforeEach
    void setUp() {
        validId = 1L;
        validSuscripcion = new SuscripcionDTO();
        validSuscripcion.setId(validId);
        validSuscripcion.setNombre("Suscripción Básica");
        validSuscripcion.setTipo("INDIVIDUAL");
        validSuscripcion.setDuracion("MENSUAL");
        validSuscripcion.setPrecio(BigDecimal.valueOf(50000));
        validSuscripcion.setCantidadReservasPermitidas(10);
    }

    // ==================== CREATE TESTS ====================

    @Test
    @DisplayName("CREATE - Suscripción válida debe crearse exitosamente")
    void createSuscripcion_ValidData_ShouldReturnCreatedSuscripcion() {
        when(suscripcionDAO.save(any(SuscripcionDTO.class))).thenReturn(validSuscripcion);

        SuscripcionDTO result = suscripcionService.createSuscripcion(validSuscripcion);

        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Suscripción Básica");
        verify(suscripcionDAO, times(1)).save(any(SuscripcionDTO.class));
    }

    @Test
    @DisplayName("CREATE - Nombre nulo debe lanzar IllegalArgumentException")
    void createSuscripcion_NullName_ShouldThrowException() {
        validSuscripcion.setNombre(null);

        assertThatThrownBy(() -> suscripcionService.createSuscripcion(validSuscripcion))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El nombre de la suscripción es obligatorio");

        verify(suscripcionDAO, never()).save(any());
    }

    @Test
    @DisplayName("CREATE - Tipo vacío debe lanzar IllegalArgumentException")
    void createSuscripcion_EmptyTipo_ShouldThrowException() {
        validSuscripcion.setTipo(" ");

        assertThatThrownBy(() -> suscripcionService.createSuscripcion(validSuscripcion))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El tipo de suscripción es obligatorio");
    }

    @Test
    @DisplayName("CREATE - Duración nula debe lanzar IllegalArgumentException")
    void createSuscripcion_NullDuracion_ShouldThrowException() {
        validSuscripcion.setDuracion(null);

        assertThatThrownBy(() -> suscripcionService.createSuscripcion(validSuscripcion))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La duración de la suscripción es obligatoria");
    }

    @Test
    @DisplayName("CREATE - Precio negativo debe lanzar IllegalArgumentException")
    void createSuscripcion_NegativePrecio_ShouldThrowException() {
        validSuscripcion.setPrecio(BigDecimal.valueOf(-100));

        assertThatThrownBy(() -> suscripcionService.createSuscripcion(validSuscripcion))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El precio de la suscripción debe ser positivo");
    }

    @Test
    @DisplayName("CREATE - Cantidad reservas negativas debe lanzar IllegalArgumentException")
    void createSuscripcion_NegativeReservas_ShouldThrowException() {
        validSuscripcion.setCantidadReservasPermitidas(-1);

        assertThatThrownBy(() -> suscripcionService.createSuscripcion(validSuscripcion))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La cantidad de reservas permitidas debe ser cero o positiva");
    }

    // ==================== READ TESTS ====================

    @Test
    @DisplayName("READ - Suscripción existente debe retornarse correctamente")
    void getSuscripcionById_ExistingId_ShouldReturnSuscripcion() {
        when(suscripcionDAO.findById(validId)).thenReturn(Optional.of(validSuscripcion));

        SuscripcionDTO result = suscripcionService.getSuscripcionById(validId);

        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Suscripción Básica");
        verify(suscripcionDAO, times(1)).findById(validId);
    }

    @Test
    @DisplayName("READ - Suscripción inexistente debe lanzar RuntimeException")
    void getSuscripcionById_NonExistingId_ShouldThrowException() {
        when(suscripcionDAO.findById(validId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> suscripcionService.getSuscripcionById(validId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Suscripción no encontrada");
    }

    @Test
    @DisplayName("READ - Listar todas las suscripciones debe retornar lista")
    void getAllSuscripciones_ShouldReturnList() {
        when(suscripcionDAO.findAll()).thenReturn(Arrays.asList(validSuscripcion));

        List<SuscripcionDTO> result = suscripcionService.getAllSuscripciones();

        assertThat(result).hasSize(1);
        verify(suscripcionDAO, times(1)).findAll();
    }

    @Test
    @DisplayName("READ - Buscar por tipo válido debe retornar lista")
    void findSuscripcionesByTipo_ValidTipo_ShouldReturnList() {
        when(suscripcionDAO.findSuscripcionesByTipo("INDIVIDUAL")).thenReturn(Arrays.asList(validSuscripcion));

        List<SuscripcionDTO> result = suscripcionService.findSuscripcionesByTipo("INDIVIDUAL");

        assertThat(result).hasSize(1);
        verify(suscripcionDAO, times(1)).findSuscripcionesByTipo("INDIVIDUAL");
    }

    @Test
    @DisplayName("READ - Buscar por tipo nulo debe lanzar IllegalArgumentException")
    void findSuscripcionesByTipo_NullTipo_ShouldThrowException() {
        assertThatThrownBy(() -> suscripcionService.findSuscripcionesByTipo(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El tipo de suscripción no puede ser nulo o vacío");
    }

    @Test
    @DisplayName("READ - Buscar por duración válida debe retornar lista")
    void findSuscripcionesByDuracion_ValidDuracion_ShouldReturnList() {
        when(suscripcionDAO.findSuscripcionesByDuracion("MENSUAL")).thenReturn(Arrays.asList(validSuscripcion));

        List<SuscripcionDTO> result = suscripcionService.findSuscripcionesByDuracion("MENSUAL");

        assertThat(result).hasSize(1);
        verify(suscripcionDAO, times(1)).findSuscripcionesByDuracion("MENSUAL");
    }

    @Test
    @DisplayName("READ - Buscar por duración vacía debe lanzar IllegalArgumentException")
    void findSuscripcionesByDuracion_EmptyDuracion_ShouldThrowException() {
        assertThatThrownBy(() -> suscripcionService.findSuscripcionesByDuracion(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La duración de la suscripción no puede ser nula o vacía");
    }

    // ==================== UPDATE TESTS ====================

    @Test
    @DisplayName("UPDATE - Datos válidos deben actualizar suscripción correctamente")
    void updateSuscripcion_ValidData_ShouldReturnUpdatedSuscripcion() {
        SuscripcionDTO updateData = new SuscripcionDTO();
        updateData.setNombre("Suscripción Actualizada");
        updateData.setPrecio(BigDecimal.valueOf(60000));

        when(suscripcionDAO.findById(validId)).thenReturn(Optional.of(validSuscripcion));
        when(suscripcionDAO.update(eq(validId), any(SuscripcionDTO.class)))
                .thenReturn(Optional.of(updateData));

        SuscripcionDTO result = suscripcionService.updateSuscripcion(validId, updateData);

        assertThat(result.getNombre()).isEqualTo("Suscripción Actualizada");
        verify(suscripcionDAO, times(1)).findById(validId);
        verify(suscripcionDAO, times(1)).update(eq(validId), any(SuscripcionDTO.class));
    }

    @Test
    @DisplayName("UPDATE - Suscripción inexistente debe lanzar RuntimeException")
    void updateSuscripcion_NonExistingId_ShouldThrowException() {
        when(suscripcionDAO.findById(validId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> suscripcionService.updateSuscripcion(validId, validSuscripcion))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Suscripción no encontrada");
    }

    @Test
    @DisplayName("UPDATE - Precio negativo debe lanzar IllegalArgumentException")
    void updateSuscripcion_NegativePrecio_ShouldThrowException() {
        SuscripcionDTO updateData = new SuscripcionDTO();
        updateData.setPrecio(BigDecimal.valueOf(-500));

        when(suscripcionDAO.findById(validId)).thenReturn(Optional.of(validSuscripcion));

        assertThatThrownBy(() -> suscripcionService.updateSuscripcion(validId, updateData))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El precio de la suscripción debe ser positivo");
    }

    @Test
    @DisplayName("UPDATE - Cantidad reservas negativas debe lanzar IllegalArgumentException")
    void updateSuscripcion_NegativeReservas_ShouldThrowException() {
        SuscripcionDTO updateData = new SuscripcionDTO();
        updateData.setCantidadReservasPermitidas(-2);

        when(suscripcionDAO.findById(validId)).thenReturn(Optional.of(validSuscripcion));

        assertThatThrownBy(() -> suscripcionService.updateSuscripcion(validId, updateData))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La cantidad de reservas permitidas debe ser cero o positiva");
    }

    // ==================== DELETE TESTS ====================

    @Test
    @DisplayName("DELETE - Suscripción existente debe eliminarse correctamente")
    void deleteSuscripcion_ExistingId_ShouldCompleteWithoutError() {
        when(suscripcionDAO.findById(validId)).thenReturn(Optional.of(validSuscripcion));
        when(suscripcionDAO.deleteById(validId)).thenReturn(true);

        assertThatCode(() -> suscripcionService.deleteSuscripcion(validId))
                .doesNotThrowAnyException();

        verify(suscripcionDAO, times(1)).findById(validId);
        verify(suscripcionDAO, times(1)).deleteById(validId);
    }

    @Test
    @DisplayName("DELETE - Suscripción inexistente debe lanzar RuntimeException")
    void deleteSuscripcion_NonExistingId_ShouldThrowException() {
        when(suscripcionDAO.findById(validId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> suscripcionService.deleteSuscripcion(validId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Suscripción no encontrada");
    }

    @Test
    @DisplayName("DELETE - Error al eliminar debe lanzar RuntimeException")
    void deleteSuscripcion_DeleteFails_ShouldThrowException() {
        when(suscripcionDAO.findById(validId)).thenReturn(Optional.of(validSuscripcion));
        when(suscripcionDAO.deleteById(validId)).thenReturn(false);

        assertThatThrownBy(() -> suscripcionService.deleteSuscripcion(validId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Suscripción no encontrada");
    }
}
