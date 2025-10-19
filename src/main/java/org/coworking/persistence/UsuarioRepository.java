package org.coworking.persistence.repository;

import org.coworking.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para UsuarioEntity.
 *
 * DESCRIPCIÓN:
 * - Gestiona el acceso a datos de la tabla usuarios.
 * - Define las consultas personalizadas utilizadas por UsuarioDAO y UsuarioService.
 *
 * NOTA:
 * - JpaRepository ya incluye métodos como save(), findById(), findAll(), deleteById(), count(), etc.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    /**
     * Buscar usuario por email exacto.
     * Usado en: GET /api/v1/usuarios/email/{email}
     */
    Optional<UsuarioEntity> findByEmail(String email);

    /**
     * Buscar usuarios por estado (true = activo, false = inactivo).
     * Usado en: GET /api/v1/usuarios/activos o inactivos
     */
    List<UsuarioEntity> findByEstado(Boolean estado);

    /**
     * Verificar si existe un usuario con el email dado.
     * Usado en validaciones del Service (create/update)
     */
    boolean existsByEmail(String email);

}
