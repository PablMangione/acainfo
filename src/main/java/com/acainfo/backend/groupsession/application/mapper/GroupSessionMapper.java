package com.acainfo.backend.groupsession.application.mapper;

import com.acainfo.backend.groupsession.domain.entity.GroupSession;
import com.acainfo.backend.groupsession.infrastructure.controller.dto.GroupSessionEditInputDto;
import com.acainfo.backend.groupsession.infrastructure.controller.dto.GroupSessionInputDto;
import com.acainfo.backend.groupsession.infrastructure.controller.dto.GroupSessionOutputDto;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper para convertir entre DTOs y entidades de dominio.
 * MapStruct generará la implementación en tiempo de compilación.
 */
@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface GroupSessionMapper {

    // ============================================
    // Conversiones de entrada (DTOs → Domain)
    // ============================================

    /**
     * Convierte el DTO de creación a entidad de dominio.
     * El ID se deja como null ya que es una nueva entidad.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    GroupSession toDomain(GroupSessionInputDto inputDto);

    /**
     * Convierte el DTO de edición a entidad de dominio.
     * Se debe establecer el ID y groupId manualmente después.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "groupId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    GroupSession toDomain(GroupSessionEditInputDto editDto);

    /**
     * Actualiza una entidad existente con datos del DTO de edición.
     * Preserva ID, groupId y timestamps.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "groupId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateDomainFromEditDto(GroupSessionEditInputDto editDto, @MappingTarget GroupSession groupSession);

    // ============================================
    // Conversiones de salida (Domain → DTOs)
    // ============================================

    /**
     * Convierte una entidad de dominio a DTO de salida.
     */
    GroupSessionOutputDto toOutputDto(GroupSession groupSession);

    /**
     * Convierte una lista de entidades a lista de DTOs de salida.
     */
    List<GroupSessionOutputDto> toOutputDtoList(List<GroupSession> groupSessions);

    // ============================================
    // Métodos auxiliares personalizados
    // ============================================

    /**
     * Crea una copia de la entidad (útil para tests o clonación).
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    GroupSession clone(GroupSession source);

    /**
     * Para mapear con ID específico.
     * Útil cuando necesitas establecer el ID manualmente.
     */
    default GroupSession toDomainWithId(GroupSessionEditInputDto editDto, Long id, Long groupId) {
        GroupSession groupSession = toDomain(editDto);
        groupSession.setId(id);
        groupSession.setGroupId(groupId);
        return groupSession;
    }
}