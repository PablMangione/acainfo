package com.acainfo.backend.student.application.mapper;

import com.acainfo.backend.student.domain.entity.Student;
import com.acainfo.backend.student.infrastructure.controller.dto.StudentInputDto;
import com.acainfo.backend.student.infrastructure.controller.dto.StudentOutputDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface StudentMapper {

    /**
     * DTO Input → Domain
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", constant = "true")
    Student toDomain(StudentInputDto dto);

    /**
     * Domain → DTO Output
     */
    @Mapping(target = "isActive", source = "active")
    StudentOutputDto toOutputDto(Student domain);

    /**
     * Actualizar dominio desde DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateDomainFromDto(StudentInputDto dto, @MappingTarget Student domain);

    /**
     * Conversión de listas
     */
    List<StudentOutputDto> toOutputDtoList(List<Student> domainList);
}