package org.coworking.domain.service;

import org.coworking.domain.dto.UsuarioDTO;
import java.util.List;

/**
 * Interfaz del servicio de usuarios.
 *
 * RESPONSABILIDADES:
 * - Validar datos de usuario
 * - Manejar la lógica de registro, actualización y eliminación
 * - Aplicar reglas de negocio antes de acceder al DAO
 */
public interface UsuarioService {

    /**
     * Crear un nuevo usuario.
     *
     * VALIDACIONES:
     * - Email único
     * - Datos requeridos completos
     *
     * @param usuarioDTO Datos del usuario a crear
     * @return DTO del usuario creado con ID generado
     * @throws IllegalArgumentException Si los datos no son válidos
     * @throws RuntimeException Si el email ya existe
     */
    UsuarioDTO createUsuario(UsuarioDTO usuarioDTO);

    /**
     * Obtener un usuario por su ID.
     *
     * @param id ID del usuario
     * @return DTO del usuario encontrado
     * @throws RuntimeException Si el usuario no existe
     */
    UsuarioDTO getUsuarioById(Long id);

    /**
     * Obtener todos los usuarios.
     *
     * @return Lista completa de usuarios
     */
    List<UsuarioDTO> getAllUsuarios();

    /**
     * Actualizar un usuario existente.
     *
     * VALIDACIONES:
     * - Email único (excepto el propio)
     *
     * @param id ID del usuario a actualizar
     * @param usuarioDTO Datos nuevos
     * @return DTO del usuario actualizado
     * @throws RuntimeException Si el usuario no existe o los datos son inválidos
     */
    UsuarioDTO updateUsuario(Long id, UsuarioDTO usuarioDTO);

    /**
     * Eliminar usuario por su ID.
     *
     * @param id ID del usuario a eliminar
     * @throws RuntimeException Si el usuario no existe
     */
    void deleteUsuario(Long id);

    /**
     * Buscar un usuario por email exacto.
     *
     * @param email Email del usuario
     * @return DTO del usuario encontrado
     * @throws RuntimeException Si no se encuentra el usuario
     */
    UsuarioDTO getUsuarioByEmail(String email);

    /**
     * Obtener todos los usuarios activos.
     *
     * @return Lista de usuarios con estado "ACTIVO"
     */
    List<UsuarioDTO> getUsuariosActivos();

    /**
     * Obtener todos los usuarios inactivos.
     *
     * @return Lista de usuarios con estado "INACTIVO"
     */
    List<UsuarioDTO> getUsuariosInactivos();

    /**
     * Obtener el total de usuarios registrados.
     *
     * @return Número total de usuarios
     */
    long getTotalUsuariosCount();

    /**
     * Autenticar usuario por email y contraseña.
     *
     * @param email Email del usuario
     * @param contraseña Contraseña del usuario
     * @return DTO del usuario autenticado
     * @throws RuntimeException Si las credenciales son inválidas
     */
    UsuarioDTO authenticateUsuario(String email, String contraseña);
    
}
