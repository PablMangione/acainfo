package com.acainfo.backend.enrollment.infrastructure.repository.imp;

import com.acainfo.backend.enrollment.domain.entity.EnrollmentId;
import com.acainfo.backend.enrollment.domain.repository.DeleteEnrollmentRepository;
import com.acainfo.backend.enrollment.infrastructure.repository.jpa.EnrollmentJpaRepository;
import com.acainfo.backend.enrollment.infrastructure.repository.jpa.entity.EnrollmentJpa;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
            // Crear el ID embebido
            EnrollmentJpa.EnrollmentId embeddedId = new EnrollmentJpa.EnrollmentId();
            embeddedId.setStudentId(id.getStudentId());
            embeddedId.setGroupId(id.getGroupId());

            // Verificar si existe antes de eliminar
            if (!jpaRepository.existsById(embeddedId)) {
                log.warn("No se encontró inscripción con ID: [{}, {}] para eliminar",
                        id.getStudentId(), id.getGroupId());
                return false;
            }

            // Eliminar usando el ID compuesto
            jpaRepository.deleteById(embeddedId);

            log.info("Inscripción eliminada exitosamente con ID: [{}, {}]",
                    id.getStudentId(), id.getGroupId());
            return true;

        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad al eliminar inscripción: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al eliminar inscripción: {}", e.getMessage(), e);
            throw new RuntimeException("Error al eliminar la inscripción", e);
        }
    }

    @Override
    public int deleteByStudentId(Long studentId) {
        log.info("Eliminando todas las inscripciones del estudiante: {}", studentId);

        try {
            int deletedCount = jpaRepository.deleteByStudent_Id(studentId);
            log.info("Se eliminaron {} inscripciones del estudiante: {}",
                    deletedCount, studentId);
            return deletedCount;

        } catch (Exception e) {
            log.error("Error al eliminar inscripciones del estudiante {}: {}",
                    studentId, e.getMessage(), e);
            throw new RuntimeException("Error al eliminar inscripciones del estudiante", e);
        }
    }

    @Override
    public int deleteByGroupId(Long groupId) {
        log.info("Eliminando todas las inscripciones del grupo: {}", groupId);

        try {
            int deletedCount = jpaRepository.deleteByGroup_Id(groupId);
            log.info("Se eliminaron {} inscripciones del grupo: {}",
                    deletedCount, groupId);
            return deletedCount;

        } catch (Exception e) {
            log.error("Error al eliminar inscripciones del grupo {}: {}",
                    groupId, e.getMessage(), e);
            throw new RuntimeException("Error al eliminar inscripciones del grupo", e);
        }
    }

    @Override
    public void deleteAll() {
        log.warn("Eliminando TODAS las inscripciones del sistema");

        try {
            jpaRepository.deleteAll();
            log.info("Todas las inscripciones han sido eliminadas");

        } catch (Exception e) {
            log.error("Error al eliminar todas las inscripciones: {}", e.getMessage(), e);
            throw new RuntimeException("Error al eliminar todas las inscripciones", e);
        }
    }
}