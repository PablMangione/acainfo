package com.acainfo.backend.groupcreationrequest.application.mapper;

import com.acainfo.backend.groupcreationrequest.domain.entity.GroupCreationRequest;
import com.acainfo.backend.groupcreationrequest.infrastructure.controller.dto.GroupCreationRequestEditInputDto;
import com.acainfo.backend.groupcreationrequest.infrastructure.controller.dto.GroupCreationRequestInputDto;
import com.acainfo.backend.groupcreationrequest.infrastructure.controller.dto.GroupCreationRequestOutputDto;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper para convertir entre DTOs y entidades de dominio de GroupCreationRequest.
 * MapStruct generará la implementación en tiempo de compilación.
 */
@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface GroupCreationRequestMapper {

    // ============================================
    // Conversiones de entrada (DTOs → Domain)
    // ============================================

    /**
     * Convierte el DTO de creación a entidad de dominio.
     * El ID se deja como null ya que es una nueva entidad.
     * El estado se inicializa como PENDING por defecto.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requestedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    GroupCreationRequest toDomain(GroupCreationRequestInputDto inputDto);

    /**
     * Convierte el DTO de edición a entidad de dominio.
     * Solo se permite actualizar el estado.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "subjectId", ignore = true)
    @Mapping(target = "requestedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    GroupCreationRequest toDomain(GroupCreationRequestEditInputDto editDto);

    /**
     * Actualiza una entidad existente con datos del DTO de edición.
     * Preserva ID, studentId, subjectId y timestamps.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "subjectId", ignore = true)
    @Mapping(target = "requestedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateDomainFromEditDto(GroupCreationRequestEditInputDto editDto, @MappingTarget GroupCreationRequest groupCreationRequest);

    // ============================================
    // Conversiones de salida (Domain → DTOs)
    // ============================================

    /**
     * Convierte una entidad de dominio a DTO de salida.
     */
    GroupCreationRequestOutputDto toOutputDto(GroupCreationRequest groupCreationRequest);

    /**
     * Convierte una lista de entidades a lista de DTOs de salida.
     */
    List<GroupCreationRequestOutputDto> toOutputDtoList(List<GroupCreationRequest> groupCreationRequests);

    // ============================================
    // Métodos auxiliares personalizados
    // ============================================

    /**
     * Crea una copia de la entidad (útil para tests o clonación).
     * No copia el ID ni los timestamps.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requestedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    GroupCreationRequest clone(GroupCreationRequest source);

    /**
     * Para mapear con ID específico.
     * Útil cuando necesitas establecer el ID manualmente.
     */
    default GroupCreationRequest toDomainWithId(GroupCreationRequestEditInputDto editDto, Long id) {
        GroupCreationRequest request = toDomain(editDto);
        request.setId(id);
        return request;
    }
}