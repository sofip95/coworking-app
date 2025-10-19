package org.coworking.persistence.mapper;

import org.coworking.domain.dto.RecursoDTO;
import org.coworking.persistence.entity.RecursoEntity;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper para conversiones entre RecursoEntity y RecursoDTO usando MapStruct.
 *
 * FUNCIÓN:
 * - Facilita la conversión automática entre la entidad de base de datos y el DTO
 * - Simplifica el paso de datos entre capas (controlador, servicio, persistencia)
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface RecursoMapper {

    /**
     * Entity -> DTO (para lecturas y respuestas)
     * Convierte un RecursoEntity de base de datos a un RecursoDTO para el cliente.
     */
    RecursoDTO toDTO(RecursoEntity entity);

    /**
     * Lista de Entities -> Lista de DTOs
     * Convierte una lista de entidades en una lista de DTOs.
     */
    List<RecursoDTO> toDTOList(List<RecursoEntity> entities);

    /**
     * DTO -> Entity (para creación)
     * Convierte un DTO recibido del cliente a una entidad lista para guardar en BD.
     *
     * NOTA:
     * - Se ignoran los campos automáticos o generados por el sistema.
     * - createdAt y updatedAt se generan automáticamente en la base de datos.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    RecursoEntity toEntity(RecursoDTO dto);

    /**
     * Actualiza un Entity existente con datos de un DTO (solo los valores no nulos).
     *
     * Usado en: PUT /api/v1/recursos/{id}
     *
     * NOTA:
     * - Ignora los campos nulos del DTO para no sobrescribir datos existentes.
     * - No permite modificar ID ni fechas automáticas.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(RecursoDTO dto, @MappingTarget RecursoEntity entity);
}
