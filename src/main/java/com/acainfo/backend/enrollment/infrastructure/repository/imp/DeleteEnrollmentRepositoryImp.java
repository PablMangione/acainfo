package com.acainfo.backend.enrollment.infrastructure.repository.imp;

import com.acainfo.backend.enrollment.domain.entity.EnrollmentId;
import com.acainfo.backend.enrollment.domain.repository.DeleteEnrollmentRepository;
import com.acainfo.backend.enrollment.infrastructure.repository.jpa.EnrollmentJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del repositorio de eliminación de inscripciones.
 * Adapta las operaciones de eliminación del dominio a la infraestructura JPA.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DeleteEnrollmentRepositoryImp implements DeleteEnrollmentRepository {

    private final EnrollmentJpaRepository jpaRepository;

    @Override
    public boolean deleteById(EnrollmentId id) {
        log.info("Eliminando inscripción con ID: [{}, {}]",
                id.getStudentId(), id.getGroupId());

        try {
            // Verificar si existe antes de eliminar
            boolean exists = jpaRepository.existsByStudentIdAndGroupId(
                    id.getStudentId(),
                    id.getGroupId()
            );

            if (!exists) {
                log.warn("No se encontró inscripción con ID: [{}, {}] para eliminar",
                        id.getStudentId(), id.getGroupId());
                return false;
            }

            // Eliminar la inscripción
            jpaRepository.deleteByStudentIdAndGroupId(
                    id.getStudentId(),
                    id.getGroupId()
            );

            log.info("Inscripción eliminada exitosamente con ID: [{}, {}]",
                    id.getStudentId(), id.getGroupId());
            return true;

        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad al eliminar inscripción: {}", e.getMessage());
            throw new RuntimeException(
                    "No se puede eliminar la inscripción debido a restricciones de integridad", e);
        } catch (Exception e) {
            log.error("Error inesperado al eliminar inscripción: {}", e.getMessage(), e);
            throw new RuntimeException("Error al eliminar la inscripción", e);
        }
    }

    @Override
    public int deleteByStudentId(Long studentId) {
        log.info("Eliminando todas las inscripciones del estudiante: {}", studentId);

        try {
            int deletedCount = jpaRepository.deleteByStudentId(studentId);

            log.info("Se eliminaron {} inscripciones del estudiante {}",
                    deletedCount, studentId);
            return deletedCount;

        } catch (Exception e) {
            log.error("Error al eliminar inscripciones del estudiante {}: {}",
                    studentId, e.getMessage(), e);
            throw new RuntimeException(
                    "Error al eliminar las inscripciones del estudiante", e);
        }
    }

    @Override
    public int deleteByGroupId(Long groupId) {
        log.info("Eliminando todas las inscripciones del grupo: {}", groupId);

        try {
            int deletedCount = jpaRepository.deleteByGroupId(groupId);

            log.info("Se eliminaron {} inscripciones del grupo {}",
                    deletedCount, groupId);
            return deletedCount;

        } catch (Exception e) {
            log.error("Error al eliminar inscripciones del grupo {}: {}",
                    groupId, e.getMessage(), e);
            throw new RuntimeException(
                    "Error al eliminar las inscripciones del grupo", e);
        }
    }

    @Override
    public void deleteAll() {
        log.warn("Eliminando TODAS las inscripciones del sistema");

        try {
            long countBefore = jpaRepository.count();
            jpaRepository.deleteAll();

            log.info("Se eliminaron {} inscripciones del sistema", countBefore);

        } catch (Exception e) {
            log.error("Error al eliminar todas las inscripciones: {}", e.getMessage(), e);
            throw new RuntimeException("Error al eliminar todas las inscripciones", e);
        }
    }
}