package com.acainfo.backend.subject.infrastructure.controller;

import com.acainfo.backend.globalenum.Major;
import com.acainfo.backend.subject.application.CreateSubjectUseCase;
import com.acainfo.backend.subject.application.DeleteSubjectUseCase;
import com.acainfo.backend.subject.application.ReadSubjectUseCase;
import com.acainfo.backend.subject.application.UpdateSubjectUseCase;
import com.acainfo.backend.subject.application.mapper.SubjectMapper;
import com.acainfo.backend.subject.domain.entity.Subject;
import com.acainfo.backend.subject.domain.value.CourseYear;
import com.acainfo.backend.subject.domain.value.Quarter;
import com.acainfo.backend.subject.infrastructure.controller.dto.SubjectEditInputDto;
import com.acainfo.backend.subject.infrastructure.controller.dto.SubjectInputDto;
import com.acainfo.backend.subject.infrastructure.controller.dto.SubjectOutputDto;
import com.acainfo.backend.subjectgroup.application.mapper.SubjectGroupMapper;
import com.acainfo.backend.subjectgroup.domain.entity.SubjectGroup;
import com.acainfo.backend.subjectgroup.domain.value.GroupStatus;
import com.acainfo.backend.subjectgroup.infrastructure.controller.dto.SubjectGroupOutputDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de asignaturas.
 *
 * Maneja las conversiones DTO ↔ Domain y delega la lógica
 * de negocio a los casos de uso.
 */
@RestController
@RequestMapping("/api/v1/subjects")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Subjects", description = "Gestión de asignaturas")
public class SubjectController {

    private final CreateSubjectUseCase createSubjectUseCase;
    private final ReadSubjectUseCase readSubjectUseCase;
    private final UpdateSubjectUseCase updateSubjectUseCase;
    private final DeleteSubjectUseCase deleteSubjectUseCase;
    private final SubjectMapper subjectMapper;
    private final SubjectGroupMapper subjectGroupMapper;

    // ============================================
    // Endpoints de Creación
    // ============================================

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear nueva asignatura", description = "Solo administradores pueden crear asignaturas")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Asignatura creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe una asignatura con esa combinación"),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para esta operación")
    })
    public ResponseEntity<SubjectOutputDto> create(@Valid @RequestBody SubjectInputDto inputDto) {
        log.info("POST /api/v1/subjects - Creando asignatura: {}", inputDto.getName());
        // Convertir DTO a Domain
        Subject subject = subjectMapper.toDomain(inputDto);
        // Ejecutar caso de uso
        Subject createdSubject = createSubjectUseCase.create(subject);
        // Convertir Domain a DTO
        SubjectOutputDto outputDto = subjectMapper.toOutputDto(createdSubject);
        return ResponseEntity.status(HttpStatus.CREATED).body(outputDto);
    }

    // ============================================
    // Endpoints de Lectura
    // ============================================

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "Obtener asignatura por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Asignatura encontrada"),
            @ApiResponse(responseCode = "404", description = "Asignatura no encontrada")
    })
    public ResponseEntity<SubjectOutputDto> getById(
            @Parameter(description = "ID de la asignatura") @PathVariable Long id) {
        log.info("GET /api/v1/subjects/{}", id);

        return readSubjectUseCase.findById(id)
                .map(subjectMapper::toOutputDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "Listar todas las asignaturas", description = "Puede filtrar por carrera, año y cuatrimestre")
    public ResponseEntity<List<SubjectOutputDto>> getAll(
            @Parameter(description = "Filtrar por carrera") @RequestParam(required = false) Major major,
            @Parameter(description = "Filtrar por año") @RequestParam(required = false) CourseYear courseYear,
            @Parameter(description = "Filtrar por cuatrimestre") @RequestParam(required = false) Quarter quarter,
            @Parameter(description = "Solo asignaturas activas") @RequestParam(defaultValue = "false") boolean activeOnly) {

        log.info("GET /api/v1/subjects - Filtros: major={}, year={}, quarter={}, activeOnly={}",
                major, courseYear, quarter, activeOnly);

        List<Subject> subjects;

        // Aplicar filtros según los parámetros
        if (major != null && courseYear != null && quarter != null) {
            subjects = readSubjectUseCase.findByMajorAndCourseYearAndQuarter(major, courseYear, quarter);
        } else if (major != null && courseYear != null) {
            subjects = readSubjectUseCase.findByMajorAndCourseYear(major, courseYear);
        } else if (major != null) {
            subjects = activeOnly ?
                    readSubjectUseCase.findByMajorActive(major) :
                    readSubjectUseCase.findByMajor(major);
        } else {
            subjects = activeOnly ?
                    readSubjectUseCase.findAllActive() :
                    readSubjectUseCase.findAll();
        }

        List<SubjectOutputDto> outputDtos = subjectMapper.toOutputDtoList(subjects);
        return ResponseEntity.ok(outputDtos);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "Buscar asignaturas por nombre")
    public ResponseEntity<List<SubjectOutputDto>> searchByName(
            @Parameter(description = "Texto a buscar en el nombre") @RequestParam String query) {
        log.info("GET /api/v1/subjects/search?query={}", query);

        List<Subject> subjects = readSubjectUseCase.searchByName(query);
        List<SubjectOutputDto> outputDtos = subjectMapper.toOutputDtoList(subjects);

        return ResponseEntity.ok(outputDtos);
    }

    @GetMapping("/paginated")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "Obtener asignaturas paginadas")
    public ResponseEntity<List<SubjectOutputDto>> getPaginated(
            @Parameter(description = "Número de página (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/v1/subjects/paginated?page={}&size={}", page, size);

        List<Subject> subjects = readSubjectUseCase.findPaginated(page, size);
        List<SubjectOutputDto> outputDtos = subjectMapper.toOutputDtoList(subjects);

        return ResponseEntity.ok(outputDtos);
    }

    @GetMapping("/{id}/groups")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @Operation(summary = "Obtener grupos de una asignatura")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grupos encontrados"),
            @ApiResponse(responseCode = "404", description = "Asignatura no encontrada")
    })
    public ResponseEntity<List<SubjectGroupOutputDto>> getGroups(
            @PathVariable Long id,
            @Parameter(description = "Filtrar por estado del grupo")
            @RequestParam(required = false) GroupStatus status) {

        log.info("GET /api/v1/subjects/{}/groups - Estado: {}", id, status);

        List<SubjectGroup> groups = readSubjectUseCase.findGroupsBySubjectId(id, status);
        List<SubjectGroupOutputDto> outputDtos = subjectGroupMapper.toOutputDtoList(groups);

        return ResponseEntity.ok(outputDtos);
    }

    // ============================================
    // Endpoints de Actualización
    // ============================================

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar asignatura", description = "Actualización completa de la asignatura")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Asignatura actualizada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Asignatura no encontrada"),
            @ApiResponse(responseCode = "409", description = "La actualización genera duplicados")
    })
    public ResponseEntity<SubjectOutputDto> update(
            @PathVariable Long id,
            @Valid @RequestBody SubjectEditInputDto editDto) {
        log.info("PUT /api/v1/subjects/{}", id);

        // Convertir DTO a Domain con el ID
        Subject subject = subjectMapper.toDomainWithId(editDto, id);

        // Ejecutar caso de uso
        Subject updatedSubject = updateSubjectUseCase.update(subject);

        // Convertir Domain a DTO
        SubjectOutputDto outputDto = subjectMapper.toOutputDto(updatedSubject);

        return ResponseEntity.ok(outputDto);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualización parcial", description = "Solo actualiza los campos proporcionados")
    public ResponseEntity<SubjectOutputDto> partialUpdate(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        log.info("PATCH /api/v1/subjects/{} - Campos: {}", id, updates.keySet());

        // Crear un Subject con solo los campos a actualizar
        Subject partialSubject = Subject.builder()
                .name((String) updates.get("name"))
                .major(updates.containsKey("major") ? Major.valueOf((String) updates.get("major")) : null)
                .courseYear(updates.containsKey("courseYear") ? CourseYear.valueOf((String) updates.get("courseYear")) : null)
                .quarter(updates.containsKey("quarter") ? Quarter.valueOf((String) updates.get("quarter")) : null)
                .isActive((Boolean) updates.get("isActive"))
                .build();

        // Ejecutar caso de uso
        Subject updatedSubject = updateSubjectUseCase.partialUpdate(id, partialSubject);

        // Convertir Domain a DTO
        SubjectOutputDto outputDto = subjectMapper.toOutputDto(updatedSubject);

        return ResponseEntity.ok(outputDto);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activar/Desactivar asignatura")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado"),
            @ApiResponse(responseCode = "400", description = "No se puede desactivar por tener grupos activos"),
            @ApiResponse(responseCode = "404", description = "Asignatura no encontrada")
    })
    public ResponseEntity<SubjectOutputDto> updateStatus(
            @PathVariable Long id,
            @Parameter(description = "true para activar, false para desactivar")
            @RequestParam boolean active) {
        log.info("PATCH /api/v1/subjects/{}/status?active={}", id, active);

        Subject updatedSubject = updateSubjectUseCase.updateActiveStatus(id, active);
        SubjectOutputDto outputDto = subjectMapper.toOutputDto(updatedSubject);

        return ResponseEntity.ok(outputDto);
    }

    // ============================================
    // Endpoints de Eliminación
    // ============================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar asignatura",
            description = "Eliminación física. Se recomienda usar desactivación en su lugar")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Asignatura eliminada"),
            @ApiResponse(responseCode = "400", description = "No se puede eliminar por tener dependencias"),
            @ApiResponse(responseCode = "404", description = "Asignatura no encontrada")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.warn("DELETE /api/v1/subjects/{} - Eliminación física solicitada", id);

        boolean deleted = deleteSubjectUseCase.deleteById(id);

        return deleted ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/soft")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Desactivar asignatura",
            description = "Soft delete recomendado. Mantiene el registro pero lo marca como inactivo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Asignatura desactivada"),
            @ApiResponse(responseCode = "400", description = "No se puede desactivar por tener dependencias activas"),
            @ApiResponse(responseCode = "404", description = "Asignatura no encontrada")
    })
    public ResponseEntity<SubjectOutputDto> softDelete(@PathVariable Long id) {
        log.info("DELETE /api/v1/subjects/{}/soft - Desactivación solicitada", id);

        Subject deactivatedSubject = deleteSubjectUseCase.softDelete(id);
        SubjectOutputDto outputDto = subjectMapper.toOutputDto(deactivatedSubject);

        return ResponseEntity.ok(outputDto);
    }

    // ============================================
    // Endpoints de Validación
    // ============================================

    @GetMapping("/{id}/can-update")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Verificar si se puede actualizar")
    public ResponseEntity<Map<String, Boolean>> canUpdate(@PathVariable Long id) {
        log.info("GET /api/v1/subjects/{}/can-update", id);

        // Obtener la asignatura actual
        return readSubjectUseCase.findById(id)
                .map(subject -> {
                    boolean canUpdate = updateSubjectUseCase.canBeUpdated(subject);
                    return ResponseEntity.ok(Map.of("canUpdate", canUpdate));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/can-deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Verificar si se puede desactivar")
    public ResponseEntity<Map<String, Boolean>> canDeactivate(@PathVariable Long id) {
        log.info("GET /api/v1/subjects/{}/can-deactivate", id);

        boolean canDeactivate = updateSubjectUseCase.canBeDeactivated(id);
        return ResponseEntity.ok(Map.of("canDeactivate", canDeactivate));
    }

    @GetMapping("/{id}/can-delete")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Verificar si se puede eliminar")
    public ResponseEntity<Map<String, Boolean>> canDelete(@PathVariable Long id) {
        log.info("GET /api/v1/subjects/{}/can-delete", id);

        boolean canDelete = deleteSubjectUseCase.canBeDeleted(id);
        return ResponseEntity.ok(Map.of("canDelete", canDelete));
    }


}