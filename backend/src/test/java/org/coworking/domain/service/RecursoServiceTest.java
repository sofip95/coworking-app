package org.coworking.domain.service;

import org.coworking.domain.dto.RecursoDTO;
import org.coworking.domain.service.impl.RecursoServiceImpl;
import org.coworking.persistence.dao.RecursoDAO;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * UNIT TESTS para RecursoServiceImpl
 *
 * OBJETIVO:
 * - Validar la lógica de negocio del servicio de recursos.
 * - Probar validaciones, excepciones y comportamientos esperados (CRUD, Filtros).
 * - Aislar de la base de datos usando Mockito.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RecursoService - Unit Tests")
class RecursoServiceTest {

    @Mock
    private RecursoDAO recursoDAO;

    @InjectMocks
    private RecursoServiceImpl recursoService;

    private RecursoDTO validRecurso;
    private Long validId;

    @BeforeEach
    void setUp() {
        validId = 1L;
        validRecurso = new RecursoDTO();
        validRecurso.setId(validId);
        validRecurso.setNombre("Sala de reuniones A1");
        validRecurso.setTipo("SALA");
        validRecurso.setUbicacion("Piso 3");
        validRecurso.setCapacidad(10);
        validRecurso.setPrecioPorHora(BigDecimal.valueOf(25000));
        validRecurso.setEstado("DISPONIBLE");
    }

    // ==================== CREATE TESTS ====================

    @Test
    @DisplayName("CREATE - Recurso válido debe crearse exitosamente")
    void createRecurso_ValidData_ShouldReturnCreatedRecurso() {
        when(recursoDAO.save(any(RecursoDTO.class))).thenReturn(validRecurso);

        RecursoDTO result = recursoService.createRecurso(validRecurso);

        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Sala de reuniones A1");
        verify(recursoDAO, times(1)).save(any(RecursoDTO.class));
    }

    @Test
    @DisplayName("CREATE - Nombre nulo debe lanzar IllegalArgumentException")
    void createRecurso_NullName_ShouldThrowException() {
        validRecurso.setNombre(null);

        assertThatThrownBy(() -> recursoService.createRecurso(validRecurso))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nombre del recurso es obligatorio");

        verify(recursoDAO, never()).save(any());
    }

    @Test
    @DisplayName("CREATE - Nombre muy largo debe lanzar IllegalArgumentException")
    void createRecurso_NameTooLong_ShouldThrowException() {
        validRecurso.setNombre("a".repeat(101));

        assertThatThrownBy(() -> recursoService.createRecurso(validRecurso))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nombre no puede exceder 100 caracteres");
    }

    @Test
    @DisplayName("CREATE - Tipo vacío debe lanzar IllegalArgumentException")
    void createRecurso_EmptyTipo_ShouldThrowException() {
        validRecurso.setTipo(" ");

        assertThatThrownBy(() -> recursoService.createRecurso(validRecurso))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tipo del recurso es obligatorio");
    }

    @Test
    @DisplayName("CREATE - Precio nulo debe lanzar IllegalArgumentException")
    void createRecurso_NullPrecio_ShouldThrowException() {
        validRecurso.setPrecioPorHora(null);

        assertThatThrownBy(() -> recursoService.createRecurso(validRecurso))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("precio por hora debe ser mayor o igual a 0");
    }

    @Test
    @DisplayName("CREATE - Precio negativo debe lanzar IllegalArgumentException")
    void createRecurso_NegativePrecio_ShouldThrowException() {
        validRecurso.setPrecioPorHora(BigDecimal.valueOf(-500));

        assertThatThrownBy(() -> recursoService.createRecurso(validRecurso))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("precio por hora debe ser mayor o igual a 0");
    }

    @Test
    @DisplayName("CREATE - Capacidad negativa debe lanzar IllegalArgumentException")
    void createRecurso_NegativeCapacidad_ShouldThrowException() {
        validRecurso.setCapacidad(-3);

        assertThatThrownBy(() -> recursoService.createRecurso(validRecurso))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("capacidad no puede ser negativa");
    }

    // Capacidad nula es permitida, no debe lanzar excepción
    @Test
    @DisplayName("CREATE - Capacidad nula debe crearse exitosamente")
    void createRecurso_NullCapacidad_ShouldCreateSuccessfully() {
        validRecurso.setCapacidad(null);
        when(recursoDAO.save(any(RecursoDTO.class))).thenReturn(validRecurso);

        assertThatCode(() -> recursoService.createRecurso(validRecurso))
                .doesNotThrowAnyException();

        verify(recursoDAO, times(1)).save(any(RecursoDTO.class));
    }


    // ==================== READ TESTS ====================

    @Test
    @DisplayName("READ - Recurso existente debe retornarse correctamente")
    void getRecursoById_ExistingId_ShouldReturnRecurso() {
        when(recursoDAO.findById(validId)).thenReturn(Optional.of(validRecurso));

        RecursoDTO result = recursoService.getRecursoById(validId);

        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Sala de reuniones A1");
        verify(recursoDAO, times(1)).findById(validId);
    }

    @Test
    @DisplayName("READ - Recurso inexistente debe lanzar RuntimeException")
    void getRecursoById_NonExistingId_ShouldThrowException() {
        when(recursoDAO.findById(validId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recursoService.getRecursoById(validId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Recurso no encontrado");
    }

    // ==================== FILTER TESTS ====================

    @Test
    @DisplayName("FILTER - Debe retornar lista de recursos filtrados cuando todos los parámetros están presentes")
    void getFilteredRecursos_AllFiltersPresent_ShouldReturnList() {
        // Configuramos el mock para retornar la lista cuando se llame con cualquier parámetro
        when(recursoDAO.findByFilters(eq("DISPONIBLE"), eq("SALA"), eq("Centro"), eq(5), eq(BigDecimal.valueOf(30000))))
                .thenReturn(Arrays.asList(validRecurso));

        List<RecursoDTO> result = recursoService.getFilteredRecursos(
                "DISPONIBLE", "SALA", "Centro", 5, BigDecimal.valueOf(30000));

        assertThat(result).hasSize(1);
        verify(recursoDAO, times(1)).findByFilters(any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("FILTER - Debe retornar lista de recursos filtrados cuando todos los parámetros son nulos/vacíos")
    void getFilteredRecursos_NoFiltersPresent_ShouldReturnList() {
        // Configuramos el mock para retornar la lista cuando se llame con parámetros nulos
        when(recursoDAO.findByFilters(isNull(), isNull(), isNull(), isNull(), isNull()))
                .thenReturn(Arrays.asList(validRecurso, validRecurso)); // Devuelve 2 para verificar

        List<RecursoDTO> result = recursoService.getFilteredRecursos(
                null, null, null, null, null);

        assertThat(result).hasSize(2);
        // Verificamos que el DAO fue llamado con todos los argumentos nulos
        verify(recursoDAO, times(1)).findByFilters(isNull(), isNull(), isNull(), isNull(), isNull());
    }

    // ==================== UPDATE TESTS ====================

    @Test
    @DisplayName("UPDATE - Datos válidos deben actualizar recurso correctamente")
    void updateRecurso_ValidData_ShouldReturnUpdatedRecurso() {
        RecursoDTO updateData = new RecursoDTO();
        updateData.setNombre("Sala Actualizada");
        updateData.setPrecioPorHora(BigDecimal.valueOf(35000));
        updateData.setCapacidad(5);

        // 1. Verificar existencia
        when(recursoDAO.findById(validId)).thenReturn(Optional.of(validRecurso));
        // 2. Realizar la actualización
        when(recursoDAO.update(eq(validId), any(RecursoDTO.class)))
                .thenReturn(Optional.of(updateData));

        RecursoDTO result = recursoService.updateRecurso(validId, updateData);

        assertThat(result.getNombre()).isEqualTo("Sala Actualizada");
        assertThat(result.getCapacidad()).isEqualTo(5);
        verify(recursoDAO, times(1)).findById(validId); // Verifica la validación inicial
        verify(recursoDAO, times(1)).update(eq(validId), any(RecursoDTO.class));
    }

    @Test
    @DisplayName("UPDATE - Recurso inexistente debe lanzar RuntimeException")
    void updateRecurso_NonExistingId_ShouldThrowException() {
        when(recursoDAO.findById(validId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recursoService.updateRecurso(validId, validRecurso))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Recurso no encontrado");

        verify(recursoDAO, never()).update(anyLong(), any());
    }

    @Test
    @DisplayName("UPDATE - Error de actualización en DAO debe lanzar RuntimeException")
    void updateRecurso_DAOUpdateFails_ShouldThrowException() {
        RecursoDTO updateData = new RecursoDTO();
        updateData.setNombre("Sala Actualizada");

        // 1. Existe
        when(recursoDAO.findById(validId)).thenReturn(Optional.of(validRecurso));
        // 2. Falla la actualización
        when(recursoDAO.update(eq(validId), any(RecursoDTO.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recursoService.updateRecurso(validId, updateData))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error al actualizar recurso con ID");

        verify(recursoDAO).update(eq(validId), any(RecursoDTO.class));
    }

    @Test
    @DisplayName("UPDATE - Intentar actualizar con nombre vacío debe lanzar IllegalArgumentException")
    void updateRecurso_EmptyNombre_ShouldThrowException() {
        RecursoDTO updateData = new RecursoDTO();
        updateData.setNombre(" ");
        when(recursoDAO.findById(validId)).thenReturn(Optional.of(validRecurso));

        assertThatThrownBy(() -> recursoService.updateRecurso(validId, updateData))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nombre no puede estar vacío");
    }

    @Test
    @DisplayName("UPDATE - Intentar actualizar con nombre muy largo debe lanzar IllegalArgumentException")
    void updateRecurso_NameTooLong_ShouldThrowException() {
        RecursoDTO updateData = new RecursoDTO();
        updateData.setNombre("a".repeat(101));
        when(recursoDAO.findById(validId)).thenReturn(Optional.of(validRecurso));

        assertThatThrownBy(() -> recursoService.updateRecurso(validId, updateData))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nombre no puede exceder 100 caracteres");
    }

    @Test
    @DisplayName("UPDATE - Precio negativo debe lanzar IllegalArgumentException")
    void updateRecurso_NegativePrecio_ShouldThrowException() {
        RecursoDTO updateData = new RecursoDTO();
        updateData.setPrecioPorHora(BigDecimal.valueOf(-100));
        when(recursoDAO.findById(validId)).thenReturn(Optional.of(validRecurso));

        assertThatThrownBy(() -> recursoService.updateRecurso(validId, updateData))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("precio por hora debe ser mayor o igual a 0");
    }

    @Test
    @DisplayName("UPDATE - Capacidad negativa debe lanzar IllegalArgumentException")
    void updateRecurso_NegativeCapacidad_ShouldThrowException() {
        RecursoDTO updateData = new RecursoDTO();
        updateData.setCapacidad(-1);
        when(recursoDAO.findById(validId)).thenReturn(Optional.of(validRecurso));

        assertThatThrownBy(() -> recursoService.updateRecurso(validId, updateData))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("capacidad no puede ser negativa");
    }


    // ==================== DELETE TESTS ====================

    @Test
    @DisplayName("DELETE - Recurso existente debe eliminarse correctamente")
    void deleteRecurso_ExistingId_ShouldCompleteWithoutError() {
        when(recursoDAO.findById(validId)).thenReturn(Optional.of(validRecurso));
        when(recursoDAO.deleteById(validId)).thenReturn(true);

        assertThatCode(() -> recursoService.deleteRecurso(validId))
                .doesNotThrowAnyException();

        verify(recursoDAO, times(1)).findById(validId); // Verifica la validación inicial
        verify(recursoDAO, times(1)).deleteById(validId);
    }

    @Test
    @DisplayName("DELETE - Recurso inexistente debe lanzar RuntimeException")
    void deleteRecurso_NonExistingId_ShouldThrowException() {
        when(recursoDAO.findById(validId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recursoService.deleteRecurso(validId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Recurso no encontrado");

        verify(recursoDAO, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("DELETE - Error al eliminar (DAO devuelve false) debe lanzar RuntimeException")
    void deleteRecurso_DeleteFails_ShouldThrowException() {
        when(recursoDAO.findById(validId)).thenReturn(Optional.of(validRecurso));
        when(recursoDAO.deleteById(validId)).thenReturn(false);

        assertThatThrownBy(() -> recursoService.deleteRecurso(validId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error al eliminar recurso");

        verify(recursoDAO).deleteById(validId);
    }
}
