package org.coworking.domain.service;

import org.coworking.domain.dto.UsuarioDTO;
import org.coworking.domain.service.impl.UsuarioServiceImpl;
import org.coworking.persistence.dao.UsuarioDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * UNIT TESTS para UsuarioServiceImpl
 *
 * OBJETIVO:
 * - Validar la lógica de negocio de UsuarioServiceImpl
 * - Aislar del contexto Spring y de la base de datos
 * - Verificar validaciones, excepciones y flujos correctos
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioService - Unit Tests")
class UsuarioServiceTest {

    @Mock
    private UsuarioDAO usuarioDAO;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private UsuarioDTO validUsuario;
    private Long validId;

    @BeforeEach
    void setUp() {
        validId = 1L;
        validUsuario = new UsuarioDTO();
        validUsuario.setId(validId);
        validUsuario.setNombre("Juan Pérez");
        validUsuario.setEmail("juan@test.com");
        validUsuario.setRol("ADMIN");
        validUsuario.setEstado(true);
    }

    // ==================== CREATE TESTS ====================

    @Test
    @DisplayName("CREATE - Usuario válido debe crearse exitosamente")
    void createUsuario_ValidData_ShouldReturnCreatedUsuario() {
        when(usuarioDAO.existsByEmail(validUsuario.getEmail())).thenReturn(false);
        when(usuarioDAO.save(any(UsuarioDTO.class))).thenReturn(validUsuario);

        UsuarioDTO result = usuarioService.createUsuario(validUsuario);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("juan@test.com");
        verify(usuarioDAO).save(any(UsuarioDTO.class));
    }

    @Test
    @DisplayName("CREATE - Email duplicado debe lanzar IllegalArgumentException")
    void createUsuario_DuplicateEmail_ShouldThrowException() {
        when(usuarioDAO.existsByEmail(validUsuario.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> usuarioService.createUsuario(validUsuario))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe un usuario con el email");
    }

    @Test
    @DisplayName("CREATE - Nombre nulo debe lanzar IllegalArgumentException")
    void createUsuario_NullName_ShouldThrowException() {
        validUsuario.setNombre(null);

        assertThatThrownBy(() -> usuarioService.createUsuario(validUsuario))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nombre del usuario es obligatorio");
    }

    @Test
    @DisplayName("CREATE - Nombre vacío debe lanzar IllegalArgumentException")
    void createUsuario_EmptyName_ShouldThrowException() {
        validUsuario.setNombre(" ");

        assertThatThrownBy(() -> usuarioService.createUsuario(validUsuario))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nombre del usuario es obligatorio");
    }

    @Test
    @DisplayName("CREATE - Nombre muy largo debe lanzar IllegalArgumentException")
    void createUsuario_LongName_ShouldThrowException() {
        validUsuario.setNombre("a".repeat(101));

        assertThatThrownBy(() -> usuarioService.createUsuario(validUsuario))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nombre no puede exceder 100 caracteres");
    }

    @Test
    @DisplayName("CREATE - Email nulo debe lanzar IllegalArgumentException")
    void createUsuario_NullEmail_ShouldThrowException() {
        validUsuario.setEmail(null);

        assertThatThrownBy(() -> usuarioService.createUsuario(validUsuario))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("email del usuario es obligatorio");
    }

    @Test
    @DisplayName("CREATE - Email vacío debe lanzar IllegalArgumentException")
    void createUsuario_EmptyEmail_ShouldThrowException() {
        validUsuario.setEmail(" ");

        assertThatThrownBy(() -> usuarioService.createUsuario(validUsuario))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("email del usuario es obligatorio");
    }

    @Test
    @DisplayName("CREATE - Email con formato inválido debe lanzar IllegalArgumentException (sin @)")
    void createUsuario_InvalidEmailFormatNoAt_ShouldThrowException() {
        validUsuario.setEmail("correosinarroba.com");

        assertThatThrownBy(() -> usuarioService.createUsuario(validUsuario))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("formato del email no es válido");
    }

    @Test
    @DisplayName("CREATE - Email con formato inválido debe lanzar IllegalArgumentException (sin punto despues de @)")
    void createUsuario_InvalidEmailFormatNoDot_ShouldThrowException() {
        validUsuario.setEmail("correo@sinpunto");

        assertThatThrownBy(() -> usuarioService.createUsuario(validUsuario))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("formato del email no es válido");
    }

    @Test
    @DisplayName("CREATE - Email con formato inválido debe lanzar IllegalArgumentException (demasiado corto)")
    void createUsuario_InvalidEmailFormatTooShort_ShouldThrowException() {
        validUsuario.setEmail("a@b.c"); // Longitud 5

        assertThatThrownBy(() -> usuarioService.createUsuario(validUsuario))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("formato del email no es válido");
    }

    @Test
    @DisplayName("CREATE - Rol nulo debe lanzar IllegalArgumentException")
    void createUsuario_NullRole_ShouldThrowException() {
        validUsuario.setRol(null);

        assertThatThrownBy(() -> usuarioService.createUsuario(validUsuario))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("rol del usuario es obligatorio");
    }

    @Test
    @DisplayName("CREATE - Rol vacío debe lanzar IllegalArgumentException")
    void createUsuario_EmptyRole_ShouldThrowException() {
        validUsuario.setRol(" ");

        assertThatThrownBy(() -> usuarioService.createUsuario(validUsuario))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("rol del usuario es obligatorio");
    }


    // ==================== READ TESTS ====================

    @Test
    @DisplayName("READ - Usuario existente debe retornarse correctamente")
    void getUsuarioById_ExistingId_ShouldReturnUsuario() {
        when(usuarioDAO.findById(validId)).thenReturn(Optional.of(validUsuario));

        UsuarioDTO result = usuarioService.getUsuarioById(validId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(validId);
        assertThat(result.getEmail()).isEqualTo("juan@test.com");
        verify(usuarioDAO).findById(validId);
    }

    @Test
    @DisplayName("READ - Usuario inexistente debe lanzar RuntimeException")
    void getUsuarioById_NonExistingId_ShouldThrowException() {
        when(usuarioDAO.findById(validId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.getUsuarioById(validId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    // ==================== READ ALL TEST ====================

    @Test
    @DisplayName("READ ALL - Debe retornar lista de usuarios")
    void getAllUsuarios_ShouldReturnList() {
        when(usuarioDAO.findAll()).thenReturn(Arrays.asList(validUsuario));

        List<UsuarioDTO> result = usuarioService.getAllUsuarios();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("juan@test.com");
        verify(usuarioDAO).findAll();
    }

    // ==================== UPDATE TESTS ====================

    @Test
    @DisplayName("UPDATE - Usuario válido debe actualizarse correctamente")
    void updateUsuario_ValidData_ShouldReturnUpdatedUsuario() {
        UsuarioDTO updateData = new UsuarioDTO();
        updateData.setNombre("Juan Actualizado");
        updateData.setEmail("nuevo@test.com");

        when(usuarioDAO.findById(validId)).thenReturn(Optional.of(validUsuario));
        when(usuarioDAO.update(eq(validId), any(UsuarioDTO.class)))
                .thenReturn(Optional.of(updateData));

        UsuarioDTO result = usuarioService.updateUsuario(validId, updateData);

        assertThat(result.getNombre()).isEqualTo("Juan Actualizado");
        verify(usuarioDAO).update(eq(validId), any(UsuarioDTO.class));
    }

    @Test
    @DisplayName("UPDATE - Error de actualización en DAO debe lanzar RuntimeException")
    void updateUsuario_DAOUpdateFails_ShouldThrowException() {
        UsuarioDTO updateData = new UsuarioDTO();
        updateData.setNombre("Juan Actualizado");

        when(usuarioDAO.findById(validId)).thenReturn(Optional.of(validUsuario));
        when(usuarioDAO.update(eq(validId), any(UsuarioDTO.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.updateUsuario(validId, updateData))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error al actualizar usuario con ID");

        verify(usuarioDAO).update(eq(validId), any(UsuarioDTO.class));
    }

    @Test
    @DisplayName("UPDATE - Usuario inexistente (en el check inicial) debe lanzar RuntimeException")
    void updateUsuario_NonExistingId_ShouldThrowException() {
        when(usuarioDAO.findById(validId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.updateUsuario(validId, validUsuario))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");

        verify(usuarioDAO, never()).update(anyLong(), any(UsuarioDTO.class));
    }

    @Test
    @DisplayName("UPDATE - Intentar actualizar con nombre vacío debe lanzar IllegalArgumentException")
    void updateUsuario_EmptyName_ShouldThrowException() {
        UsuarioDTO updateData = new UsuarioDTO();
        updateData.setNombre(" ");

        when(usuarioDAO.findById(validId)).thenReturn(Optional.of(validUsuario));

        assertThatThrownBy(() -> usuarioService.updateUsuario(validId, updateData))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El nombre no puede estar vacío");
    }

    @Test
    @DisplayName("UPDATE - Intentar actualizar con nombre muy largo debe lanzar IllegalArgumentException")
    void updateUsuario_LongName_ShouldThrowException() {
        UsuarioDTO updateData = new UsuarioDTO();
        updateData.setNombre("a".repeat(101));

        when(usuarioDAO.findById(validId)).thenReturn(Optional.of(validUsuario));

        assertThatThrownBy(() -> usuarioService.updateUsuario(validId, updateData))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nombre no puede exceder 100 caracteres");
    }

    @Test
    @DisplayName("UPDATE - Intentar actualizar con email inválido debe lanzar IllegalArgumentException")
    void updateUsuario_InvalidEmail_ShouldThrowException() {
        UsuarioDTO updateData = new UsuarioDTO();
        updateData.setEmail("email_invalido");

        when(usuarioDAO.findById(validId)).thenReturn(Optional.of(validUsuario));

        assertThatThrownBy(() -> usuarioService.updateUsuario(validId, updateData))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("formato del email no es válido");
    }

    @Test
    @DisplayName("UPDATE - Intentar actualizar con rol vacío debe lanzar IllegalArgumentException")
    void updateUsuario_EmptyRole_ShouldThrowException() {
        UsuarioDTO updateData = new UsuarioDTO();
        updateData.setRol(" ");

        when(usuarioDAO.findById(validId)).thenReturn(Optional.of(validUsuario));

        assertThatThrownBy(() -> usuarioService.updateUsuario(validId, updateData))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("rol no puede estar vacío");
    }


    // ==================== DELETE TESTS ====================

    @Test
    @DisplayName("DELETE - Usuario existente debe eliminarse correctamente")
    void deleteUsuario_ExistingId_ShouldCompleteWithoutError() {
        when(usuarioDAO.findById(validId)).thenReturn(Optional.of(validUsuario));
        when(usuarioDAO.deleteById(validId)).thenReturn(true);

        assertThatCode(() -> usuarioService.deleteUsuario(validId))
                .doesNotThrowAnyException();

        verify(usuarioDAO).deleteById(validId);
    }

    @Test
    @DisplayName("DELETE - Usuario inexistente debe lanzar RuntimeException")
    void deleteUsuario_NonExistingId_ShouldThrowException() {
        when(usuarioDAO.findById(validId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.deleteUsuario(validId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    @DisplayName("DELETE - Error al eliminar (DAO devuelve false) debe lanzar RuntimeException")
    void deleteUsuario_DeleteFails_ShouldThrowException() {
        when(usuarioDAO.findById(validId)).thenReturn(Optional.of(validUsuario));
        when(usuarioDAO.deleteById(validId)).thenReturn(false);

        assertThatThrownBy(() -> usuarioService.deleteUsuario(validId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error al eliminar usuario con ID");
    }


    // ==================== EMAIL SEARCH TESTS ====================

    @Test
    @DisplayName("GET BY EMAIL - Email válido debe retornar usuario")
    void getUsuarioByEmail_ValidEmail_ShouldReturnUsuario() {
        when(usuarioDAO.findByEmail(validUsuario.getEmail())).thenReturn(Optional.of(validUsuario));

        UsuarioDTO result = usuarioService.getUsuarioByEmail("juan@test.com");

        assertThat(result).isNotNull();
        verify(usuarioDAO).findByEmail("juan@test.com");
    }

    @Test
    @DisplayName("GET BY EMAIL - Email nulo debe lanzar IllegalArgumentException")
    void getUsuarioByEmail_NullEmail_ShouldThrowException() {
        assertThatThrownBy(() -> usuarioService.getUsuarioByEmail(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("email no puede estar vacío");
    }

    @Test
    @DisplayName("GET BY EMAIL - Email vacío debe lanzar IllegalArgumentException")
    void getUsuarioByEmail_EmptyEmail_ShouldThrowException() {
        assertThatThrownBy(() -> usuarioService.getUsuarioByEmail(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("email no puede estar vacío");
    }

    @Test
    @DisplayName("GET BY EMAIL - Email inexistente debe lanzar RuntimeException")
    void getUsuarioByEmail_NotFound_ShouldThrowException() {
        when(usuarioDAO.findByEmail("noexiste@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.getUsuarioByEmail("noexiste@test.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    // ==================== ACTIVO/INACTIVO & COUNT TESTS ====================

    @Test
    @DisplayName("GET ACTIVOS - Debe retornar lista de usuarios activos")
    void getUsuariosActivos_ShouldReturnList() {
        when(usuarioDAO.findByEstado(true)).thenReturn(Arrays.asList(validUsuario));

        List<UsuarioDTO> result = usuarioService.getUsuariosActivos();

        assertThat(result).hasSize(1);
        verify(usuarioDAO).findByEstado(true);
    }

    @Test
    @DisplayName("GET INACTIVOS - Debe retornar lista de usuarios inactivos")
    void getUsuariosInactivos_ShouldReturnList() {
        // Creamos un usuario inactivo para simular el caso
        UsuarioDTO inactiveUser = new UsuarioDTO();
        inactiveUser.setId(2L);
        inactiveUser.setEstado(false);

        when(usuarioDAO.findByEstado(false)).thenReturn(Arrays.asList(inactiveUser));

        List<UsuarioDTO> result = usuarioService.getUsuariosInactivos();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEstado()).isFalse();
        verify(usuarioDAO).findByEstado(false);
    }

    @Test
    @DisplayName("COUNT - Debe retornar total de usuarios")
    void getTotalUsuariosCount_ShouldReturnValue() {
        when(usuarioDAO.count()).thenReturn(5L);

        long count = usuarioService.getTotalUsuariosCount();

        assertThat(count).isEqualTo(5);
        verify(usuarioDAO).count();
    }
}