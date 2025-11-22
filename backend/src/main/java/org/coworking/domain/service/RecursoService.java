package org.coworking.domain.service;

import org.coworking.domain.dto.RecursoDTO;
import java.math.BigDecimal;
import java.util.List;

/**
 * Interfaz del servicio de recursos.
 *
 * RESPONSABILIDADES:
 * - Gestionar la lógica de negocio y validación de datos de recursos
 * - Interactuar con el DAO/Repository
 * - Proveer operaciones CRUD y filtrado flexible
 */
public interface RecursoService {

    /**
     * Crear un nuevo recurso.
     *
     * VALIDACIONES:
     * - Nombre único
     * - Precio por hora mayor o igual a 0
     * - Campos obligatorios no nulos
     *
     * @param recursoDTO Datos del recurso a crear
     * @return DTO del recurso creado
     * @throws IllegalArgumentException Si los datos son inválidos
     * @throws RuntimeException Si el nombre ya existe
     */
    RecursoDTO createRecurso(RecursoDTO recursoDTO);

    /**
     * Obtener un recurso por su ID.
     *
     * @param id ID del recurso
     * @return DTO del recurso encontrado
     * @throws RuntimeException Si el recurso no existe
     */
    RecursoDTO getRecursoById(Long id);

    /**
     * Filtrar y listar recursos según los parámetros opcionales.
     *
     * Criterios soportados:
     * - estado: DISPONIBLE, OCUPADO, MANTENIMIENTO
     * - tipo: tipo del recurso (sala, oficina, escritorio, etc.)
     * - ubicación: texto parcial o exacto
     * - capacidadMinima: capacidad mínima requerida
     * - precioMaximo: precio máximo por hora
     *
     * Si no se especifican filtros, devuelve todos los recursos.
     *
     * @param estado Estado del recurso
     * @param tipo Tipo del recurso
     * @param ubicacion Ubicación del recurso
     * @param capacidadMinima Capacidad mínima
     * @param precioMaximo Precio máximo por hora
     * @return Lista de recursos que cumplen con los filtros
     */
    List<RecursoDTO> getFilteredRecursos(String estado, String tipo, String ubicacion, Integer capacidadMinima, BigDecimal precioMaximo);

    /**
     * Actualizar los datos de un recurso existente.
     *
     * VALIDACIONES:
     * - El recurso debe existir
     * - Nombre único (excepto el mismo)
     * - Precio válido
     *
     * @param id ID del recurso a actualizar
     * @param recursoDTO Datos nuevos del recurso
     * @return DTO del recurso actualizado
     * @throws RuntimeException Si el recurso no existe o los datos son inválidos
     */
    RecursoDTO updateRecurso(Long id, RecursoDTO recursoDTO);

    /**
     * Eliminar un recurso por su ID.
     *
     * @param id ID del recurso a eliminar
     * @throws RuntimeException Si el recurso no existe
     */
    void deleteRecurso(Long id);
}
