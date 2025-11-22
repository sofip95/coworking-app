package org.coworking.domain.service;

import org.coworking.domain.dto.ReporteDTO;
import org.coworking.domain.service.impl.ReporteServiceImpl;
import org.coworking.persistence.dao.ReporteDAO;
import org.coworking.persistence.dao.UsuarioDAO;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * UNIT TESTS para ReporteServiceImpl
 *
 * OBJETIVO:
 * - Validar la lógica de negocio del servicio de reportes
 * - Probar validaciones, excepciones y comportamiento correcto
 * - No usar base de datos ni contexto Spring (solo mocks)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ReporteService - Unit Tests")
class ReporteServiceTest {

    @Mock
    private ReporteDAO reporteDAO;

    @Mock
    private UsuarioDAO usuarioDAO;

    @InjectMocks
    private ReporteServiceImpl reporteService;

    private ReporteDTO validReporte;
    private Long validReporteId;
    private Long validUsuarioId;

    @BeforeEach
    void setUp() {
        validUsuarioId = 1L;
        validReporteId = 10L;

        validReporte = new ReporteDTO();
        validReporte.setId(validReporteId);
        validReporte.setUsuarioId(validUsuarioId);
        validReporte.setTitulo("Reporte de Ingresos");
        validReporte.setTipo("INGRESOS");
        validReporte.setCantidadRegistros(5);
        validReporte.setContenido("Contenido del reporte de prueba");
        validReporte.setCreatedAt(LocalDateTime.now());
    }

    // ==================== CREATE TESTS ====================

    @Test
    @DisplayName("CREATE - Reporte válido debe crearse exitosamente")
    void createReporte_ValidData_ShouldReturnCreatedReporte() {
        when(usuarioDAO.existsById(validUsuarioId)).thenReturn(true);
        when(reporteDAO.save(any(ReporteDTO.class))).thenReturn(validReporte);

        ReporteDTO result = reporteService.createReporte(validReporte);

        assertThat(result).isNotNull();
        assertThat(result.getTitulo()).isEqualTo("Reporte de Ingresos");
        assertThat(result.getTipo()).isEqualTo("INGRESOS");
        verify(usuarioDAO).existsById(validUsuarioId);
        verify(reporteDAO).save(any(ReporteDTO.class));
    }

    @Test
    @DisplayName("CREATE - Título nulo debe lanzar IllegalArgumentException")
    void createReporte_NullTitulo_ShouldThrowException() {
        validReporte.setTitulo(null);

        assertThatThrownBy(() -> reporteService.createReporte(validReporte))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("título del reporte es obligatorio");
    }

    @Test
    @DisplayName("CREATE - Título vacío debe lanzar IllegalArgumentException")
    void createReporte_EmptyTitulo_ShouldThrowException() {
        validReporte.setTitulo(" ");

        assertThatThrownBy(() -> reporteService.createReporte(validReporte))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("título del reporte es obligatorio");
    }

    @Test
    @DisplayName("CREATE - Título muy largo debe lanzar IllegalArgumentException")
    void createReporte_LongTitulo_ShouldThrowException() {
        validReporte.setTitulo("a".repeat(256));

        assertThatThrownBy(() -> reporteService.createReporte(validReporte))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("título no puede exceder 255 caracteres");
    }

    @Test
    @DisplayName("CREATE - Tipo nulo debe lanzar IllegalArgumentException")
    void createReporte_NullTipo_ShouldThrowException() {
        validReporte.setTipo(null);

        assertThatThrownBy(() -> reporteService.createReporte(validReporte))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tipo de reporte es obligatorio");
    }

    @Test
    @DisplayName("CREATE - Tipo vacío debe lanzar IllegalArgumentException")
    void createReporte_EmptyTipo_ShouldThrowException() {
        validReporte.setTipo(" ");

        assertThatThrownBy(() -> reporteService.createReporte(validReporte))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tipo de reporte es obligatorio");
    }

    @Test
    @DisplayName("CREATE - Cantidad de registros nula debe lanzar IllegalArgumentException")
    void createReporte_NullCantidadRegistros_ShouldThrowException() {
        validReporte.setCantidadRegistros(null);

        assertThatThrownBy(() -> reporteService.createReporte(validReporte))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cantidad de registros debe ser un número positivo");
    }


    @Test
    @DisplayName("CREATE - Cantidad de registros negativa debe lanzar IllegalArgumentException")
    void createReporte_NegativeCantidadRegistros_ShouldThrowException() {
        validReporte.setCantidadRegistros(-1);

        assertThatThrownBy(() -> reporteService.createReporte(validReporte))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cantidad de registros debe ser un número positivo");
    }

    @Test
    @DisplayName("CREATE - Contenido nulo debe lanzar IllegalArgumentException")
    void createReporte_NullContenido_ShouldThrowException() {
        validReporte.setContenido(null);

        assertThatThrownBy(() -> reporteService.createReporte(validReporte))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("contenido del reporte no puede estar vacío");
    }

    @Test
    @DisplayName("CREATE - Contenido vacío debe lanzar IllegalArgumentException")
    void createReporte_EmptyContenido_ShouldThrowException() {
        validReporte.setContenido(" ");

        assertThatThrownBy(() -> reporteService.createReporte(validReporte))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("contenido del reporte no puede estar vacío");
    }

    @Test
    @DisplayName("CREATE - UsuarioId nulo (en DTO) debe lanzar IllegalArgumentException")
    void createReporte_NullUsuarioIdInDTO_ShouldThrowException() {
        // En ReporteServiceImpl, el método validateReporteData NO valida UsuarioId,
        // pero la lógica principal sí lo valida ANTES de usuarioDAO.existsById
        validReporte.setUsuarioId(null);

        assertThatThrownBy(() -> reporteService.createReporte(validReporte))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID del usuario es obligatorio para crear un reporte");

        verify(usuarioDAO, never()).existsById(anyLong());
        verify(reporteDAO, never()).save(any(ReporteDTO.class));
    }


    @Test
    @DisplayName("CREATE - Usuario inexistente debe lanzar RuntimeException")
    void createReporte_NonExistentUser_ShouldThrowException() {
        when(usuarioDAO.existsById(validUsuarioId)).thenReturn(false);

        assertThatThrownBy(() -> reporteService.createReporte(validReporte))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No existe un usuario con el ID");

        verify(usuarioDAO).existsById(validUsuarioId);
        verify(reporteDAO, never()).save(any(ReporteDTO.class));
    }

    // ==================== READ TESTS ====================

    @Test
    @DisplayName("READ - Reporte existente debe retornarse correctamente")
    void getReporteById_ExistingId_ShouldReturnReporte() {
        when(reporteDAO.findById(validReporteId)).thenReturn(Optional.of(validReporte));

        ReporteDTO result = reporteService.getReporteById(validReporteId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validReporteId);
        verify(reporteDAO).findById(validReporteId);
    }

    @Test
    @DisplayName("READ - Reporte inexistente debe lanzar RuntimeException")
    void getReporteById_NonExistingId_ShouldThrowException() {
        when(reporteDAO.findById(validReporteId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reporteService.getReporteById(validReporteId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Reporte no encontrado");
    }

    // ==================== READ ALL TEST ====================

    @Test
    @DisplayName("READ ALL - Debe retornar lista de reportes")
    void getAllReportes_ShouldReturnList() {
        when(reporteDAO.findAll()).thenReturn(Arrays.asList(validReporte));

        List<ReporteDTO> result = reporteService.getAllReportes();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitulo()).isEqualTo("Reporte de Ingresos");
        verify(reporteDAO).findAll();
    }

    // ==================== GET BY TIPO TESTS ====================

    @Test
    @DisplayName("GET BY TIPO - Tipo válido debe retornar lista de reportes")
    void getReportesByTipo_ValidTipo_ShouldReturnList() {
        when(reporteDAO.findByTipo("INGRESOS")).thenReturn(Arrays.asList(validReporte));

        List<ReporteDTO> result = reporteService.getReportesByTipo("INGRESOS");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTipo()).isEqualTo("INGRESOS");
        verify(reporteDAO).findByTipo("INGRESOS");
    }

    @Test
    @DisplayName("GET BY TIPO - Tipo nulo debe lanzar IllegalArgumentException")
    void getReportesByTipo_NullTipo_ShouldThrowException() {
        assertThatThrownBy(() -> reporteService.getReportesByTipo(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tipo de reporte es obligatorio");
    }

    @Test
    @DisplayName("GET BY TIPO - Tipo vacío debe lanzar IllegalArgumentException")
    void getReportesByTipo_EmptyTipo_ShouldThrowException() {
        assertThatThrownBy(() -> reporteService.getReportesByTipo(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tipo de reporte es obligatorio");
    }


    // ==================== DELETE TESTS ====================

    @Test
    @DisplayName("DELETE - Reporte existente debe eliminarse correctamente")
    void deleteReporte_ExistingId_ShouldCompleteWithoutError() {
        when(reporteDAO.findById(validReporteId)).thenReturn(Optional.of(validReporte));
        when(reporteDAO.deleteById(validReporteId)).thenReturn(true);

        assertThatCode(() -> reporteService.deleteReporte(validReporteId))
                .doesNotThrowAnyException();

        verify(reporteDAO).deleteById(validReporteId);
    }

    @Test
    @DisplayName("DELETE - Reporte inexistente debe lanzar RuntimeException")
    void deleteReporte_NonExistingId_ShouldThrowException() {
        when(reporteDAO.findById(validReporteId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reporteService.deleteReporte(validReporteId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Reporte no encontrado");
    }

    @Test
    @DisplayName("DELETE - Error al eliminar (DAO devuelve false) debe lanzar RuntimeException")
    void deleteReporte_DeleteFails_ShouldThrowException() {
        // 1. Configurar el DAO para que encuentre el reporte (para que pase getReporteById(id))
        when(reporteDAO.findById(validReporteId)).thenReturn(Optional.of(validReporte));

        // 2. Configurar el DAO para que simule que la eliminación falla (devuelve false)
        when(reporteDAO.deleteById(validReporteId)).thenReturn(false);

        // 3. Afirmar que se lanza una RuntimeException con el mensaje de error esperado
        assertThatThrownBy(() -> reporteService.deleteReporte(validReporteId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error al eliminar reporte");

        // 4. Verificar que se intentó eliminar
        verify(reporteDAO).deleteById(validReporteId);
    }
}
