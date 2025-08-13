package com.acainfo.backend.student.application.mapper;

import com.acainfo.backend.student.domain.entity.Student;
import com.acainfo.backend.student.infrastructure.controller.dto.StudentEditInputDto;
import com.acainfo.backend.student.infrastructure.controller.dto.StudentInputDto;
import com.acainfo.backend.student.infrastructure.controller.dto.StudentOutputDto;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper para convertir entre DTOs y entidades de dominio de Student.
 * MapStruct generará la implementación en tiempo de compilación.
 */
@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface StudentMapper {

    // ============================================
    // Conversiones de entrada (DTOs → Domain)
    // ============================================

    /**
     * Convierte el DTO de creación a entidad de dominio.
     * El ID se deja como null ya que es una nueva entidad.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Student toDomain(StudentInputDto inputDto);

    /**
     * Convierte el DTO de edición a entidad de dominio.
     * Se debe establecer el ID manualmente después.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    Student toDomain(StudentEditInputDto editDto);

    /**
     * Actualiza una entidad existente con datos del DTO de edición.
     * Preserva ID, contraseña, estado activo y timestamps.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    void updateDomainFromEditDto(StudentEditInputDto editDto, @MappingTarget Student student);

    // ============================================
    // Conversiones de salida (Domain → DTOs)
    // ============================================

    /**
     * Convierte una entidad de dominio a DTO de salida.
     * Nunca incluye la contraseña en el DTO de salida.
     */
    StudentOutputDto toOutputDto(Student student);

    /**
     * Convierte una lista de entidades a lista de DTOs de salida.
     */
    List<StudentOutputDto> toOutputDtoList(List<Student> students);

    // ============================================
    // Métodos auxiliares personalizados
    // ============================================

    /**
     * Crea una copia de la entidad (útil para tests o clonación).
     * No copia el ID ni los timestamps.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Student clone(Student source);

    /**
     * Para mapear con ID específico.
     * Útil cuando necesitas establecer el ID manualmente.
     */
    default Student toDomainWithId(StudentEditInputDto editDto, Long id) {
        Student student = toDomain(editDto);
        student.setId(id);
        return student;
    }

    /**
     * Actualiza solo la contraseña de un estudiante.
     */
    default void updatePassword(Student student, String newPassword) {
        student.setPassword(newPassword);
    }

    /**
     * Actualiza solo el estado activo de un estudiante.
     */
    default void updateActiveStatus(Student student, boolean isActive) {
        student.setActive(isActive);
    }
}