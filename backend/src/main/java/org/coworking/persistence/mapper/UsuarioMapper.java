package org.coworking.persistence.mapper;

import org.coworking.domain.dto.UsuarioDTO;
import org.coworking.persistence.entity.UsuarioEntity;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper para conversiones entre UsuarioEntity y UsuarioDTO usando MapStruct.
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface UsuarioMapper {

    /**
     * Entity -> DTO (para lecturas y respuestas)
     */
    UsuarioDTO toDTO(UsuarioEntity entity);

    /**
     * Lista de Entities -> Lista de DTOs
     */
    List<UsuarioDTO> toDTOList(List<UsuarioEntity> entities);

    /**
     * DTO -> Entity (para creación)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UsuarioEntity toEntity(UsuarioDTO dto);

    /**
     * Actualiza un Entity existente con datos del DTO (sin sobrescribir nulos)
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(UsuarioDTO dto, @MappingTarget UsuarioEntity entity);
}
