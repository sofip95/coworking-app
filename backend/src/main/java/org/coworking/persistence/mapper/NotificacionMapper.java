package org.coworking.persistence.mapper;

import java.util.List;

import org.coworking.domain.dto.NotificacionDTO;
import org.coworking.persistence.entity.NotificacionEntity;
import org.coworking.persistence.entity.UsuarioEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface NotificacionMapper {

    /**
     * Convierte NotificacionEntity a NotificacionDTO
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "usuarioId", source = "usuarioEntity.id")
    @Mapping(target = "tipo", source = "tipo")
    @Mapping(target = "titulo", source = "titulo")
    @Mapping(target = "mensaje", source = "mensaje")
    @Mapping(target = "estado", source = "estado")
    @Mapping(target = "fechaEnvio", source = "fechaEnvio")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    NotificacionDTO toDTO(NotificacionEntity entity);

    /**
     * Convierte lista de NotificacionEntity a lista de NotificacionDTO
     */
    List<NotificacionDTO> toDTOList(List<NotificacionEntity> entities);

    /**
     * Convierte NotificacionDTO a NotificacionEntity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "fechaEnvio", ignore = true)
    @Mapping(target = "usuarioEntity", source = "usuarioId", qualifiedByName = "idToUsuario")
    NotificacionEntity toEntity(NotificacionDTO notificacionDTO);

    /**
     * NotificacionDTO a NotificacionEntity - Actualiza una entidad existente con datos del DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "fechaEnvio", ignore = true)
    @Mapping(target = "usuarioEntity", ignore = true)
    // SI un campo en el DTO es null, NO sobreescribe el valor en la entidad
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(NotificacionDTO notificacionDTO, @MappingTarget NotificacionEntity entity);

    /**
     * Método auxiliar: Convierte usuarioId en usuarioEntity con solo el ID
     */
    @Named("idToUsuario")
    default UsuarioEntity idToUsuario(Long usuarioId) {
        if (usuarioId == null) {
            return null;
        }
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(usuarioId);
        return usuario;
    }

}
