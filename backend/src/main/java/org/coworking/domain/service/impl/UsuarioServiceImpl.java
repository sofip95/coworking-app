package org.coworking.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.coworking.domain.dto.UsuarioDTO;
import org.coworking.domain.service.UsuarioService;
import org.coworking.persistence.dao.UsuarioDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDAO usuarioDAO;

    @Override
    public UsuarioDTO createUsuario(UsuarioDTO usuarioDTO) {
        log.info("Creando nuevo usuario con email: {}", usuarioDTO.getEmail());
        validateUsuarioData(usuarioDTO);

        if (usuarioDAO.existsByEmail(usuarioDTO.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + usuarioDTO.getEmail());
        }

        UsuarioDTO saved = usuarioDAO.save(usuarioDTO);
        log.info("Usuario creado exitosamente con ID: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO getUsuarioById(Long id) {
        log.debug("Buscando usuario por ID: {}", id);
        return usuarioDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> getAllUsuarios() {
        return usuarioDAO.findAll();
    }

    @Override
    public UsuarioDTO updateUsuario(Long id, UsuarioDTO usuarioDTO) {
        log.info("Actualizando usuario ID: {}", id);
        getUsuarioById(id);
        validateUsuarioUpdateData(usuarioDTO);

        return usuarioDAO.update(id, usuarioDTO)
                .orElseThrow(() -> new RuntimeException("Error al actualizar usuario con ID: " + id));
    }

    @Override
    public void deleteUsuario(Long id) {
        getUsuarioById(id);
        boolean deleted = usuarioDAO.deleteById(id);
        if (!deleted) throw new RuntimeException("Error al eliminar usuario con ID: " + id);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO getUsuarioByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        return usuarioDAO.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> getUsuariosActivos() {
        return usuarioDAO.findByEstado(true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> getUsuariosInactivos() {
        return usuarioDAO.findByEstado(false);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalUsuariosCount() {
        return usuarioDAO.count();
    }

    @Override
    public UsuarioDTO authenticateUsuario(String email, String contraseña) {
        if (email == null || email.trim().isEmpty() ||
            contraseña == null || contraseña.trim().isEmpty()) {
            throw new IllegalArgumentException("El email y la contraseña no pueden estar vacíos");
        }
        return usuarioDAO.findByEmailAndContraseña(email, contraseña)
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));
    }

    // ===================== VALIDACIONES =====================
    private void validateUsuarioData(UsuarioDTO usuarioDTO) {
        if (usuarioDTO.getNombre() == null || usuarioDTO.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del usuario es obligatorio");
        }
        if (usuarioDTO.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
        }
        if (usuarioDTO.getEmail() == null || usuarioDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email del usuario es obligatorio");
        }
        if (!isValidEmailFormat(usuarioDTO.getEmail())) {
            throw new IllegalArgumentException("El formato del email no es válido");
        }
        if (usuarioDTO.getRol() == null || usuarioDTO.getRol().trim().isEmpty()) {
            throw new IllegalArgumentException("El rol del usuario es obligatorio");
        }
    }

    private void validateUsuarioUpdateData(UsuarioDTO usuarioDTO) {
        if (usuarioDTO.getNombre() != null) {
            if (usuarioDTO.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }
            if (usuarioDTO.getNombre().length() > 100) {
                throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
            }
        }
        if (usuarioDTO.getEmail() != null && !isValidEmailFormat(usuarioDTO.getEmail())) {
            throw new IllegalArgumentException("El formato del email no es válido");
        }
        if (usuarioDTO.getRol() != null && usuarioDTO.getRol().trim().isEmpty()) {
            throw new IllegalArgumentException("El rol no puede estar vacío");
        }
    }

    private boolean isValidEmailFormat(String email) {
        return email.contains("@") &&
                email.indexOf("@") < email.lastIndexOf(".") &&
                email.length() > 5;
    }
}
