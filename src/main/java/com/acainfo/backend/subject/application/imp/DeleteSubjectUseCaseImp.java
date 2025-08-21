package com.acainfo.backend.subject.application.imp;

import com.acainfo.backend.subject.application.DeleteSubjectUseCase;
import com.acainfo.backend.subject.application.exception.SubjectBusinessException;
import com.acainfo.backend.subject.domain.entity.Subject;
import com.acainfo.backend.subject.domain.exception.SubjectNotFoundException;
import com.acainfo.backend.subject.domain.repository.DeleteSubjectRepository;
import com.acainfo.backend.subject.domain.repository.ReadSubjectRepository;
import com.acainfo.backend.subject.domain.repository.UpdateSubjectRepository;
import com.acainfo.backend.subjectgroup.domain.repository.ReadSubjectGroupRepository;
import com.acainfo.backend.groupcreationrequest.domain.repository.ReadGroupCreationRequestRepository;
import com.acainfo.backend.groupcreationrequest.domain.value.RequestStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementación del caso de uso para la eliminación de asignaturas.
 *
 * Prioriza el soft delete (desactivación) sobre la eliminación física
 * para mantener la integridad histórica del sistema.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteSubjectUseCaseImp implements DeleteSubjectUseCase {

    private final DeleteSubjectRepository deleteSubjectRepository;
    private final ReadSubjectRepository readSubjectRepository;
    private final UpdateSubjectRepository updateSubjectRepository;
    private final ReadSubjectGroupRepository readSubjectGroupRepository;
    private final ReadGroupCreationRequestRepository readGroupCreationRequestRepository;

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        log.warn("Eliminación física de asignatura solicitada para ID: {}", id);

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que 0");
        }

        // Verificar que existe
        if (!readSubjectRepository.existsById(id)) {
            log.warn("Intento de eliminar asignatura inexistente: {}", id);
            return false;
        }

        // Verificar que no tiene dependencias
        if (!canBeDeleted(id)) {
            throw new SubjectBusinessException(
                    "No se puede eliminar la asignatura porque tiene datos relacionados"
            );
        }

        // Eliminar físicamente
        boolean deleted = deleteSubjectRepository.deleteById(id);

        if (deleted) {
            log.info("Asignatura eliminada físicamente: ID={}", id);
        }

        return deleted;
    }

    @Override
    @Transactional
    public Subject softDelete(Long id) {
        log.info("Desactivación (soft delete) de asignatura solicitada para ID: {}", id);

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que 0");
        }

        // Obtener la asignatura
        Subject subject = readSubjectRepository.findById(id)
                .orElseThrow(() -> new SubjectNotFoundException(
                        "No se encontró la asignatura con ID: " + id
                ));

        // Verificar que no está ya desactivada
        if (Boolean.FALSE.equals(subject.getIsActive())) {
            log.info("La asignatura {} ya está desactivada", id);
            return subject;
        }

        // Verificar que puede ser desactivada
        if (!canBeSoftDeleted(id)) {
            throw new SubjectBusinessException(
                    "No se puede desactivar la asignatura porque tiene grupos activos o solicitudes pendientes"
            );
        }

        // Desactivar
        subject.setIsActive(false);
        Subject updatedSubject = updateSubjectRepository.update(subject);

        log.info("Asignatura desactivada exitosamente: ID={}", id);
        return updatedSubject;
    }

    @Override
    public boolean canBeDeleted(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que 0");
        }

        try {
            // Verificar que existe
            if (!readSubjectRepository.existsById(id)) {
                return false;
            }

            // Verificar que NO tiene grupos (activos o inactivos)
            var groups = readSubjectGroupRepository.findBySubjectId(id);
            if (!groups.isEmpty()) {
                log.debug("La asignatura {} tiene {} grupos asociados", id, groups.size());
                return false;
            }

            // Verificar que NO tiene solicitudes de creación de grupo
            var requests = readGroupCreationRequestRepository.findBySubjectId(id);
            if (!requests.isEmpty()) {
                log.debug("La asignatura {} tiene {} solicitudes asociadas", id, requests.size());
                return false;
            }

            // En el futuro, aquí se verificarían inscripciones históricas, etc.

            return true;

        } catch (Exception e) {
            log.error("Error al verificar si se puede eliminar: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean canBeSoftDeleted(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que 0");
        }

        try {
            // Verificar que existe y está activa
            Subject subject = readSubjectRepository.findById(id).orElse(null);
            if (subject == null || Boolean.FALSE.equals(subject.getIsActive())) {
                return false;
            }

            // Verificar que NO tiene grupos ACTIVOS
            var activeGroups = readSubjectGroupRepository.findBySubjectIdAndStatus(
                    id,
                    com.acainfo.backend.subjectgroup.domain.value.GroupStatus.ACTIVE
            );
            if (!activeGroups.isEmpty()) {
                log.debug("La asignatura {} tiene {} grupos activos", id, activeGroups.size());
                return false;
            }

            // Verificar que NO tiene solicitudes PENDIENTES
            var pendingRequests = readGroupCreationRequestRepository
                    .findBySubjectIdAndStatus(id, RequestStatus.PENDING);
            if (!pendingRequests.isEmpty()) {
                log.debug("La asignatura {} tiene {} solicitudes pendientes",
                        id, pendingRequests.size());
                return false;
            }

            return true;

        } catch (Exception e) {
            log.error("Error al verificar si se puede desactivar: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public int purgeInactiveSubjects(int daysInactive) {
        log.info("Iniciando purga de asignaturas inactivas por más de {} días", daysInactive);

        if (daysInactive <= 0) {
            throw new IllegalArgumentException("Los días de inactividad deben ser mayor que 0");
        }

        // Calcular fecha límite
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysInactive);
        int deletedCount = 0;

        // Obtener todas las asignaturas inactivas
        List<Subject> inactiveSubjects = readSubjectRepository.findAll().stream()
                .filter(s -> Boolean.FALSE.equals(s.getIsActive()))
                .filter(s -> s.getUpdatedAt() != null && s.getUpdatedAt().isBefore(cutoffDate))
                .toList();

        log.info("Encontradas {} asignaturas candidatas para purga", inactiveSubjects.size());

        // Intentar eliminar cada una
        for (Subject subject : inactiveSubjects) {
            if (canBeDeleted(subject.getId())) {
                try {
                    if (deleteSubjectRepository.deleteById(subject.getId())) {
                        deletedCount++;
                        log.debug("Asignatura purgada: ID={}, Nombre={}",
                                subject.getId(), subject.getName());
                    }
                } catch (Exception e) {
                    log.error("Error al purgar asignatura {}: {}",
                            subject.getId(), e.getMessage());
                }
            } else {
                log.debug("Asignatura {} no puede ser purgada debido a dependencias",
                        subject.getId());
            }
        }

        log.info("Purga completada: {} asignaturas eliminadas de {} candidatas",
                deletedCount, inactiveSubjects.size());

        return deletedCount;
    }
}