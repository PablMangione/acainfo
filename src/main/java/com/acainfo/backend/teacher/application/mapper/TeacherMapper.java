package com.acainfo.backend.teacher.application.mapper;

import com.acainfo.backend.teacher.domain.entity.Teacher;
import com.acainfo.backend.teacher.infrastructure.controller.dto.TeacherInputDto;
import com.acainfo.backend.teacher.infrastructure.controller.dto.TeacherOutputDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TeacherMapper {

    /**
     * DTO Input → Domain
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    @Mapping(target = "admin", source = "isAdmin")
    Teacher toDomain(TeacherInputDto dto);

    /**
     * Domain → DTO Output
     */
    @Mapping(target = "isAdmin", source = "admin")
    TeacherOutputDto toOutputDto(Teacher domain);

    /**
     * Actualizar dominio desde DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    @Mapping(target = "admin", ignore = true) // No se debe cambiar desde DTO normal
    void updateDomainFromDto(TeacherInputDto dto, @MappingTarget Teacher domain);

    /**
     * Conversión de listas
     */
    List<TeacherOutputDto> toOutputDtoList(List<Teacher> domainList);
}