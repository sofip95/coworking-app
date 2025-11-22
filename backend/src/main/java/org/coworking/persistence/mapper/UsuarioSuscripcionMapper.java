package org.coworking.persistence.mapper;

import java.util.List;

import org.coworking.domain.dto.UsuarioSuscripcionDTO;
import org.coworking.persistence.entity.UsuarioSuscripcionEntity;
import org.coworking.persistence.entity.SuscripcionEntity;
import org.coworking.persistence.entity.UsuarioEntity;
import org.mapstruct.*;

/**
 * Mapper para conversiones entre UsuarioSuscripcionEntity y UsuarioSuscripcionDTO usando MapStruct
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface UsuarioSuscripcionMapper {

    /**
     * Convierte UsuarioSuscripcionEntity a UsuarioSuscripcionDTO
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "usuarioId", source = "usuarioEntity.id")
    @Mapping(target = "suscripcionId", source = "suscripcionEntity.id")
    @Mapping(target = "estado", source = "estado")
    @Mapping(target = "fechaInicio", source = "fechaInicio")
    @Mapping(target = "fechaFin", source = "fechaFin")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    UsuarioSuscripcionDTO toDTO(UsuarioSuscripcionEntity entity);

    /**
     * Convierte lista de UsuarioSuscripcionEntity a lista de UsuarioSuscripcionDTO
     */
    List<UsuarioSuscripcionDTO> toDTOList(List<UsuarioSuscripcionEntity> entities);

    /**
     * Convierte UsuarioSuscripcionDTO a UsuarioSuscripcionEntity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuarioEntity", ignore = true) // Asignar usuarioEntity por separado
    @Mapping(target = "suscripcionEntity", ignore = true) // Asignar suscripcionEntity por separado
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UsuarioSuscripcionEntity toEntity(UsuarioSuscripcionDTO dto);

    /**
     * UsuarioSuscripcionDTO a UsuarioSuscripcionEntity - Actualiza una entidad existente con datos del DTO
     * Hereda la configuración inversa de toDTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuarioEntity", ignore = true) // No actualizar usuarioEntity aquí
    @Mapping(target = "suscripcionEntity", ignore = true) // No actualizar suscripcionEntity aquí
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    // SI un campo en el DTO es null, NO sobreescribe el valor en la entidad
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(UsuarioSuscripcionDTO dto, @MappingTarget UsuarioSuscripcionEntity entity);

}
