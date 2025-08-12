package com.acainfo.backend.teacher.application.mapper;

import com.acainfo.backend.teacher.domain.entity.Teacher;
import com.acainfo.backend.teacher.infrastructure.controller.dto.TeacherEditInputDto;
import com.acainfo.backend.teacher.infrastructure.controller.dto.TeacherInputDto;
import com.acainfo.backend.teacher.infrastructure.controller.dto.TeacherOutputDto;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper para convertir entre DTOs y entidades de dominio de Teacher.
 * MapStruct generará la implementación en tiempo de compilación.
 */
@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface TeacherMapper {

    // ============================================
    // Conversiones de entrada (DTOs → Domain)
    // ============================================

    /**
     * Convierte el DTO de creación a entidad de dominio.
     * El ID se deja como null ya que es una nueva entidad.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    Teacher toDomain(TeacherInputDto inputDto);

    /**
     * Convierte el DTO de edición a entidad de dominio.
     * Se debe establecer el ID manualmente después.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "isAdmin", ignore = true)
    Teacher toDomain(TeacherEditInputDto editDto);

    /**
     * Actualiza una entidad existente con datos del DTO de edición.
     * Preserva ID, contraseña, rol admin y fecha de registro.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "isAdmin", ignore = true)
    void updateDomainFromEditDto(TeacherEditInputDto editDto, @MappingTarget Teacher teacher);

    // ============================================
    // Conversiones de salida (Domain → DTOs)
    // ============================================

    /**
     * Convierte una entidad de dominio a DTO de salida.
     * Nunca incluye la contraseña en el DTO de salida.
     */
    TeacherOutputDto toOutputDto(Teacher teacher);

    /**
     * Convierte una lista de entidades a lista de DTOs de salida.
     */
    List<TeacherOutputDto> toOutputDtoList(List<Teacher> teachers);

    // ============================================
    // Métodos auxiliares personalizados
    // ============================================

    /**
     * Crea una copia de la entidad (útil para tests o clonación).
     * No copia el ID ni la fecha de registro.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    Teacher clone(Teacher source);

    /**
     * Para mapear con ID específico.
     * Útil cuando necesitas establecer el ID manualmente.
     */
    default Teacher toDomainWithId(TeacherEditInputDto editDto, Long id) {
        Teacher teacher = toDomain(editDto);
        teacher.setId(id);
        return teacher;
    }

    /**
     * Actualiza solo la contraseña de un profesor.
     */
    default void updatePassword(Teacher teacher, String newPassword) {
        teacher.setPassword(newPassword);
    }

    /**
     * Actualiza solo el estado admin de un profesor.
     */
    default void updateAdminStatus(Teacher teacher, boolean isAdmin) {
        teacher.setAdmin(isAdmin);
    }
}