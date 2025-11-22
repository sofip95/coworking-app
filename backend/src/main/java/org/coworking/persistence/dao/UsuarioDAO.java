package org.coworking.persistence.dao;

import lombok.RequiredArgsConstructor;
import org.coworking.domain.dto.UsuarioDTO;
import org.coworking.persistence.entity.UsuarioEntity;
import org.coworking.persistence.mapper.UsuarioMapper;
import org.coworking.persistence.repository.UsuarioRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * DAO para manejar operaciones CRUD de usuarios.
 *
 * FLUJO GENERAL:
 * - Convierte entre DTO y Entity usando UsuarioMapper.
 * - Usa UsuarioRepository para interactuar con la base de datos.
 * - Devuelve DTOs al servicio o controlador.
 */
@Repository
@RequiredArgsConstructor
public class UsuarioDAO {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    /**
     * CREATE - Crear un nuevo usuario.
     *
     * Flujo:
     * 1. DTO → Entity (mappeo con UsuarioMapper)
     * 2. Guardar Entity en BD
     * 3. Entity guardada → DTO (retorna con ID generado)
     */
    public UsuarioDTO save(UsuarioDTO usuarioDTO) {
        UsuarioEntity entity = usuarioMapper.toEntity(usuarioDTO);
        UsuarioEntity savedEntity = usuarioRepository.save(entity);
        return usuarioMapper.toDTO(savedEntity);
    }

    /**
     * Authenticate - Buscar usuario por email y contraseña.
     */
    public Optional<UsuarioDTO> findByEmailAndContraseña(String email, String contraseña) {
        return usuarioRepository.findByEmailAndContraseña(email, contraseña)
                .map(usuarioMapper::toDTO);
    }

    /**
     * READ - Buscar usuario por ID.
     *
     * @return Optional<UsuarioDTO> vacío si no existe el ID.
     */
    public Optional<UsuarioDTO> findById(Long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toDTO);
    }

    /**
     * READ ALL - Buscar todos los usuarios.
     *
     * Convierte la lista completa de entidades a DTOs.
     */
    public List<UsuarioDTO> findAll() {
        List<UsuarioEntity> entities = usuarioRepository.findAll();
        return usuarioMapper.toDTOList(entities);
    }


    /**
     * READ - Buscar usuario por email exacto.
     */
    public Optional<UsuarioDTO> findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(usuarioMapper::toDTO);
    }

    /**
     * READ - Buscar usuarios por estado (true = activos, false = inactivos).
     */
    public List<UsuarioDTO> findByEstado(Boolean estado) {
        List<UsuarioEntity> entities = usuarioRepository.findByEstado(estado);
        return usuarioMapper.toDTOList(entities);
    }

    /**
     * UPDATE - Actualizar un usuario existente.
     *
     * Flujo:
     * 1. Buscar entity existente por ID
     * 2. Si existe, usar mapper.updateEntityFromDTO()
     * 3. Guardar entity modificada
     * 4. Retornar DTO actualizado
     */
    public Optional<UsuarioDTO> update(Long id, UsuarioDTO usuarioDTO) {
        return usuarioRepository.findById(id)
                .map(existingEntity -> {
                    usuarioMapper.updateEntityFromDTO(usuarioDTO, existingEntity);
                    UsuarioEntity updatedEntity = usuarioRepository.save(existingEntity);
                    return usuarioMapper.toDTO(updatedEntity);
                });
    }

    /**
     * DELETE - Eliminar usuario por ID.
     *
     * @return true si se eliminó correctamente, false si no existía.
     */
    public boolean deleteById(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * UTILIDAD - Verificar si existe un usuario con el email dado.
     */
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }


    /**
     * UTILIDAD - Verificar si existe un usuario con el ID dado.
     */
    public boolean existsById(Long id) {
        return usuarioRepository.existsById(id);
    }

    /**
     * UTILIDAD - Contar el total de usuarios.
     */
    public long count() {
        return usuarioRepository.count();
    }
}
