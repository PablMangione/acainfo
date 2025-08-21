package com.acainfo.backend.subject.application.mapper;

import com.acainfo.backend.subject.domain.entity.Subject;
import com.acainfo.backend.subject.infrastructure.controller.dto.SubjectEditInputDto;
import com.acainfo.backend.subject.infrastructure.controller.dto.SubjectInputDto;
import com.acainfo.backend.subject.infrastructure.controller.dto.SubjectOutputDto;
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
public interface SubjectMapper {

    // ============================================
    // Conversiones de entrada (DTOs → Domain)
    // ============================================

    /**
     * Convierte el DTO de creación a entidad de dominio.
     * El ID se inicializa a 0 ya que es una nueva entidad.
     */
    @Mapping(target = "id", constant = "0L")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Subject toDomain(SubjectInputDto inputDto);

    /**
     * Convierte el DTO de edición a entidad de dominio.
     * Se debe establecer el ID manualmente después.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    Subject toDomain(SubjectEditInputDto editDto);

    /**
     * Actualiza una entidad existente con datos del DTO de edición.
     * Preserva ID, timestamps y estado activo.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    void updateDomainFromEditDto(SubjectEditInputDto editDto, @MappingTarget Subject subject);

    // ============================================
    // Conversiones de salida (Domain → DTOs)
    // ============================================

    /**
     * Convierte una entidad de dominio a DTO de salida.
     */
    @Mapping(target = "id", expression = "java(subject.getId() == 0L ? null : Long.valueOf(subject.getId()))")
    SubjectOutputDto toOutputDto(Subject subject);

    /**
     * Convierte una lista de entidades a lista de DTOs de salida.
     */
    List<SubjectOutputDto> toOutputDtoList(List<Subject> subjects);

    // ============================================
    // Métodos auxiliares personalizados
    // ============================================

    /**
     * Crea una copia de la entidad (útil para tests o clonación).
     */
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Subject clone(Subject source);

    /**
     * Para mapear con ID específico.
     * Útil cuando necesitas establecer el ID manualmente.
     */
    default Subject toDomainWithId(SubjectEditInputDto editDto, Long id) {
        Subject subject = toDomain(editDto);
        subject.setId(id);
        return subject;
    }
}