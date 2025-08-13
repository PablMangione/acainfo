package com.acainfo.backend.subjectgroup.application.mapper;

import com.acainfo.backend.subjectgroup.domain.entity.SubjectGroup;
import com.acainfo.backend.subjectgroup.infrastructure.controller.dto.SubjectGroupEditInputDto;
import com.acainfo.backend.subjectgroup.infrastructure.controller.dto.SubjectGroupInputDto;
import com.acainfo.backend.subjectgroup.infrastructure.controller.dto.SubjectGroupOutputDto;
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
public interface SubjectGroupMapper {

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
    @Mapping(target = "currentEnrollments", constant = "0")
    SubjectGroup toDomain(SubjectGroupInputDto inputDto);

    /**
     * Convierte el DTO de edición a entidad de dominio.
     * Se debe establecer el ID manualmente después.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "currentEnrollments", ignore = true)
    @Mapping(target = "subjectId", ignore = true) // No se puede cambiar la asignatura
    SubjectGroup toDomain(SubjectGroupEditInputDto editDto);

    /**
     * Actualiza una entidad existente con datos del DTO de edición.
     * Preserva ID, timestamps, enrollments y subjectId.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "currentEnrollments", ignore = true)
    @Mapping(target = "subjectId", ignore = true)
    void updateDomainFromEditDto(SubjectGroupEditInputDto editDto, @MappingTarget SubjectGroup subjectGroup);

    // ============================================
    // Conversiones de salida (Domain → DTOs)
    // ============================================

    /**
     * Convierte una entidad de dominio a DTO de salida.
     */
    SubjectGroupOutputDto toOutputDto(SubjectGroup subjectGroup);

    /**
     * Convierte una lista de entidades a lista de DTOs de salida.
     */
    List<SubjectGroupOutputDto> toOutputDtoList(List<SubjectGroup> subjectGroups);

    // ============================================
    // Métodos auxiliares personalizados
    // ============================================

    /**
     * Crea una copia de la entidad (útil para tests o clonación).
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    SubjectGroup clone(SubjectGroup source);

    /**
     * Para mapear con ID específico.
     * Útil cuando necesitas establecer el ID manualmente.
     */
    default SubjectGroup toDomainWithId(SubjectGroupEditInputDto editDto, Long id) {
        SubjectGroup subjectGroup = toDomain(editDto);
        subjectGroup.setId(id);
        return subjectGroup;
    }
}