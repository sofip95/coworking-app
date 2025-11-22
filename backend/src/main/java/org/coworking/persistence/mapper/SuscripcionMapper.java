package org.coworking.persistence.mapper;

import java.util.List;

import org.coworking.domain.dto.SuscripcionDTO;
import org.coworking.persistence.entity.SuscripcionEntity;
import org.mapstruct.*;

/**
 * Mapper para conversiones entre SuscripcionEntity y SuscripcionDTO usando MapStruct
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface SuscripcionMapper {

    /**
     * Convierte SuscripcionEntity a SuscripcionDTO
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "descripcion", source = "descripcion")
    @Mapping(target = "tipo", source = "tipo")
    @Mapping(target = "duracion", source = "duracion")
    @Mapping(target = "precio", source = "precio")
    @Mapping(target = "cantidadReservasPermitidas", source = "cantidadReservasPermitidas")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    SuscripcionDTO toDTO(SuscripcionEntity entity);

    /**
     * Convierte lista de SuscripcionEntity a lista de SuscripcionDTO
     */
    List<SuscripcionDTO> toDTOList(List<SuscripcionEntity> entities);

    /**
     * Convierte SuscripcionDTO a SuscripcionEntity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    SuscripcionEntity toEntity(SuscripcionDTO dto);

    /**
     * SuscripcionDTO a SuscripcionEntity - Actualiza una entidad existente con datos del DTO
     * Hereda la configuración inversa de toDTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    // SI un campo en el DTO es null, NO sobreescribe el valor en la entidad
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(SuscripcionDTO dto, @MappingTarget SuscripcionEntity entity);

}
