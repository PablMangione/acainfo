package com.acainfo.backend.enrollment.application.mapper;

import com.acainfo.backend.enrollment.domain.entity.Enrollment;
import com.acainfo.backend.enrollment.domain.entity.EnrollmentId;
import com.acainfo.backend.enrollment.infrastructure.controller.dto.EnrollmentInputDto;
import com.acainfo.backend.enrollment.infrastructure.controller.dto.EnrollmentOutputDto;
import com.acainfo.backend.enrollment.infrastructure.controller.dto.EnrollmentStatusUpdateDto;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper para convertir entre DTOs y entidades de dominio de Enrollment.
 * MapStruct generará la implementación en tiempo de compilación.
 */
@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface EnrollmentMapper {

    // ============================================
    // Conversiones de entrada (DTOs → Domain)
    // ============================================

    /**
     * Convierte el DTO de creación a entidad de dominio.
     * Crea el ID compuesto a partir de los IDs individuales.
     */
    @Mapping(target = "id", expression = "java(createEnrollmentId(inputDto.getStudentId(), inputDto.getGroupId()))")
    @Mapping(target = "enrolledAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", constant = "PENDING_PAYMENT")
    Enrollment toDomain(EnrollmentInputDto inputDto);

    /**
     * Actualiza el estado de una entidad existente.
     * Solo actualiza el campo status.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enrolledAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateStatusFromDto(EnrollmentStatusUpdateDto statusDto, @MappingTarget Enrollment enrollment);

    // ============================================
    // Conversiones de salida (Domain → DTOs)
    // ============================================

    /**
     * Convierte una entidad de dominio a DTO de salida.
     * Extrae los IDs del ID compuesto.
     */
    @Mapping(source = "id.studentId", target = "studentId")
    @Mapping(source = "id.groupId", target = "groupId")
    EnrollmentOutputDto toOutputDto(Enrollment enrollment);

    /**
     * Convierte una lista de entidades a lista de DTOs de salida.
     */
    List<EnrollmentOutputDto> toOutputDtoList(List<Enrollment> enrollments);

    // ============================================
    // Métodos auxiliares personalizados
    // ============================================

    /**
     * Crea un ID compuesto a partir de los IDs individuales.
     */
    default EnrollmentId createEnrollmentId(Long studentId, Long groupId) {
        if (studentId == null || groupId == null) {
            return null;
        }
        return new EnrollmentId(studentId, groupId);
    }

    /**
     * Crea una copia de la entidad (útil para tests o clonación).
     * No copia los timestamps.
     */
    @Mapping(target = "enrolledAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Enrollment clone(Enrollment source);

    /**
     * Convierte con un ID compuesto específico.
     */
    default Enrollment toDomainWithId(Long studentId, Long groupId, EnrollmentStatusUpdateDto statusDto) {
        Enrollment enrollment = new Enrollment();
        enrollment.setId(createEnrollmentId(studentId, groupId));
        enrollment.setStatus(statusDto.getStatus());
        return enrollment;
    }
}