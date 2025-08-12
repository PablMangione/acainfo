package com.acainfo.backend.subject.application.impl;

import com.acainfo.backend.subject.application.UpdateSubjectUseCase;
import com.acainfo.backend.subject.application.exception.SubjectBusinessException;
import com.acainfo.backend.subject.application.mapper.SubjectMapper;
import com.acainfo.backend.subject.domain.entity.Subject;
import com.acainfo.backend.subject.domain.exception.SubjectNotFoundException;
import com.acainfo.backend.subject.domain.repository.ReadSubjectRepository;
import com.acainfo.backend.subject.domain.repository.UpdateSubjectRepository;
import com.acainfo.backend.subject.infrastructure.controller.dto.SubjectEditInputDto;
import com.acainfo.backend.subject.infrastructure.controller.dto.SubjectOutputDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del caso de uso para actualizar asignaturas.
 * Coordina la lógica de negocio para modificaciones de asignaturas existentes.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateSubjectUseCaseImpl implements UpdateSubjectUseCase {

    private final UpdateSubjectRepository updateRepository;
    private final ReadSubjectRepository readRepository;
    private final SubjectMapper mapper;

    /**
     * Actualiza una asignatura existente.
     *
     * @param id el identificador de la asignatura a actualizar
     * @param editDto los datos actualizados de la asignatura
     * @return la asignatura actualizada
     */
    @Override
    @Transactional
    public SubjectOutputDto update(long id, SubjectEditInputDto editDto) {
        log.info("Iniciando actualización de asignatura con ID: {}", id);

        // Verificar que la asignatura existe
        Subject existingSubject = readRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Intento de actualizar asignatura inexistente. ID: {}", id);
                    return new SubjectNotFoundException(
                            "No se puede actualizar. No existe asignatura con ID: " + id
                    );
                });

        // Aplicar los cambios del DTO a la entidad existente
        mapper.updateDomainFromEditDto(editDto, existingSubject);

        // Validar reglas de negocio antes de actualizar
        validateUpdateBusinessRules(existingSubject, editDto);

        // Persistir los cambios
        Subject updatedSubject = updateRepository.update(existingSubject);

        log.info("Asignatura actualizada exitosamente. ID: {}, Nombre: {}",
                updatedSubject.getId(), updatedSubject.getName());

        return mapper.toOutputDto(updatedSubject);
    }

    /**
     * Actualiza el estado activo/inactivo de una asignatura.
     *
     * @param id el identificador de la asignatura
     * @param active true para activar, false para desactivar
     * @return la asignatura con el estado actualizado
     */
    @Override
    @Transactional
    public SubjectOutputDto updateActiveStatus(long id, boolean active) {
        log.info("Actualizando estado de asignatura ID: {} a {}",
                id, active ? "ACTIVA" : "INACTIVA");

        // Obtener la asignatura existente
        Subject subject = readRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Intento de cambiar estado de asignatura inexistente. ID: {}", id);
                    return new SubjectNotFoundException(
                            "No se puede cambiar el estado. No existe asignatura con ID: " + id
                    );
                });

        // Validar el cambio de estado
        validateStatusChange(subject, active);

        // Actualizar el estado
        subject.setActive(active);

        // Persistir el cambio
        Subject updatedSubject = updateRepository.update(subject);

        log.info("Estado de asignatura actualizado. ID: {}, Nuevo estado: {}",
                id, active ? "ACTIVA" : "INACTIVA");

        return mapper.toOutputDto(updatedSubject);
    }

    /**
     * Valida las reglas de negocio para la actualización de una asignatura.
     *
     * @param subject la asignatura con los datos a actualizar
     * @param editDto los datos nuevos del DTO
     */
    private void validateUpdateBusinessRules(Subject subject, SubjectEditInputDto editDto) {
        // Validar que no se esté cambiando a una combinación que afecte la integridad académica

        // Ejemplo: No permitir cambio de año/cuatrimestre si hay grupos activos
        // Este tipo de validación requeriría acceso a otros repositorios
        // Por ahora solo validamos cambios básicos

        // Validar que el nombre no esté vacío después del trim
        if (subject.getName() != null) {
            String trimmedName = subject.getName().trim();
            if (trimmedName.isEmpty()) {
                throw new SubjectBusinessException(
                        "El nombre de la asignatura no puede estar vacío"
                );
            }
            subject.setName(trimmedName);
        }

        // Aquí podrías agregar más validaciones como:
        // - No permitir cambiar de carrera si hay estudiantes inscritos
        // - No permitir cambiar el cuatrimestre durante el período activo
        // - Validar que el cambio no genere conflictos de horario

        log.debug("Validaciones de negocio completadas para actualización de asignatura ID: {}",
                subject.getId());
    }

    /**
     * Valida el cambio de estado activo/inactivo de una asignatura.
     *
     * @param subject la asignatura a validar
     * @param newStatus el nuevo estado deseado
     */
    private void validateStatusChange(Subject subject, boolean newStatus) {
        // Verificar si el estado ya es el deseado
        if (subject.isActive() == newStatus) {
            String statusText = newStatus ? "activa" : "inactiva";
            throw new SubjectBusinessException(
                    String.format("La asignatura ya está %s", statusText)
            );
        }

        // Si se intenta desactivar, validar que no tenga dependencias activas
        if (!newStatus && subject.isActive()) {
            // Aquí deberías verificar:
            // - No hay grupos activos para esta asignatura
            // - No hay inscripciones en curso
            // - No está en período de evaluación

            // Por ahora solo logueamos la acción
            log.warn("Desactivando asignatura ID: {}. Verificar dependencias manualmente.",
                    subject.getId());
        }

        // Si se intenta activar, validar prerrequisitos
        if (newStatus && !subject.isActive()) {
            // Aquí podrías verificar:
            // - Que tenga profesor asignado
            // - Que tenga horario definido
            // - Que no exceda el límite de asignaturas activas

            log.info("Reactivando asignatura ID: {}", subject.getId());
        }
    }
}