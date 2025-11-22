package org.coworking.persistence.mapper;

import org.coworking.domain.dto.ReporteDTO;
import org.coworking.persistence.entity.ReporteEntity;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper para conversiones entre ReporteEntity y ReporteDTO usando MapStruct.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ReporteMapper {

    // Entity → DTO
    @Mapping(target = "usuarioId", source = "usuario.id")
    ReporteDTO toDTO(ReporteEntity entity);

    // Lista Entity → DTO
    List<ReporteDTO> toDTOList(List<ReporteEntity> entities);

    // DTO → Entity
    @Mapping(target = "usuario.id", source = "usuarioId")
    @Mapping(target = "createdAt", ignore = true) // se asigna automáticamente en el DAO o Service
    ReporteEntity toEntity(ReporteDTO dto);
}
