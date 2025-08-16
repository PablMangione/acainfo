package com.acainfo.backend.enrollment.infrastructure.repository.imp;

import com.acainfo.backend.enrollment.domain.entity.Enrollment;
import com.acainfo.backend.enrollment.domain.entity.EnrollmentId;
import com.acainfo.backend.enrollment.domain.repository.ReadEnrollmentRepository;
import com.acainfo.backend.enrollment.domain.value.EnrollmentStatus;
import com.acainfo.backend.enrollment.infrastructure.repository.jpa.EnrollmentJpaRepository;
import com.acainfo.backend.enrollment.infrastructure.repository.jpa.entity.EnrollmentJpa;
import com.acainfo.backend.enrollment.infrastructure.repository.mapper.EnrollmentJpaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del repositorio de lectura de inscripciones.
 * Adapta las consultas del dominio a la infraestructura JPA.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReadEnrollmentRepositoryImp implements ReadEnrollmentRepository {

    private final EnrollmentJpaRepository jpaRepository;
    private final EnrollmentJpaMapper mapper;

    @Override
    public Optional<Enrollment> findById(EnrollmentId id) {
        log.debug("Buscando inscripción con ID: [{}, {}]",
                id.getStudentId(), id.getGroupId());

        return jpaRepository.findByStudentIdAndGroupId(
                id.getStudentId(),
                id.getGroupId()
        ).map(mapper::toDomain);
    }

    @Override
    public List<Enrollment> findAll() {
        log.debug("Obteniendo todas las inscripciones");

        List<EnrollmentJpa> jpaEntities = jpaRepository.findAll();
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public boolean existsById(EnrollmentId id) {
        log.debug("Verificando existencia de inscripción con ID: [{}, {}]",
                id.getStudentId(), id.getGroupId());

        return jpaRepository.existsByStudentIdAndGroupId(
                id.getStudentId(),
                id.getGroupId()
        );
    }

    @Override
    public List<Enrollment> findByStudentId(Long studentId) {
        log.debug("Buscando inscripciones del estudiante: {}", studentId);

        List<EnrollmentJpa> jpaEntities =
                jpaRepository.findByStudentIdOrderByEnrolledAtDesc(studentId);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<Enrollment> findByGroupId(Long groupId) {
        log.debug("Buscando inscripciones del grupo: {}", groupId);

        List<EnrollmentJpa> jpaEntities =
                jpaRepository.findByGroupIdOrderByEnrolledAtDesc(groupId);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<Enrollment> findByStatus(EnrollmentStatus status) {
        log.debug("Buscando inscripciones con estado: {}", status);

        List<EnrollmentJpa> jpaEntities =
                jpaRepository.findByStatusOrderByEnrolledAtDesc(status);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<Enrollment> findByStudentIdAndStatus(Long studentId, EnrollmentStatus status) {
        log.debug("Buscando inscripciones del estudiante {} con estado: {}",
                studentId, status);

        List<EnrollmentJpa> jpaEntities =
                jpaRepository.findByStudentIdAndStatusOrderByEnrolledAtDesc(studentId, status);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<Enrollment> findByGroupIdAndStatus(Long groupId, EnrollmentStatus status) {
        log.debug("Buscando inscripciones del grupo {} con estado: {}",
                groupId, status);

        List<EnrollmentJpa> jpaEntities =
                jpaRepository.findByGroupIdAndStatusOrderByEnrolledAtDesc(groupId, status);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public long countByGroupIdAndStatus(Long groupId, EnrollmentStatus status) {
        log.debug("Contando inscripciones del grupo {} con estado: {}",
                groupId, status);

        return jpaRepository.countByGroupIdAndStatus(groupId, status);
    }

    @Override
    public boolean existsByStudentIdAndGroupId(Long studentId, Long groupId) {
        log.debug("Verificando si el estudiante {} está inscrito en el grupo {}",
                studentId, groupId);

        return jpaRepository.existsByStudentIdAndGroupId(studentId, groupId);
    }

    @Override
    public List<Enrollment> findActiveEnrollmentsByStudentId(Long studentId) {
        log.debug("Buscando inscripciones activas del estudiante: {}", studentId);

        List<EnrollmentJpa> jpaEntities =
                jpaRepository.findActiveEnrollmentsByStudentId(studentId);
        return mapper.toDomainList(jpaEntities);
    }

    @Override
    public List<Enrollment> findPendingPaymentEnrollments() {
        log.debug("Buscando inscripciones pendientes de pago");

        List<EnrollmentJpa> jpaEntities =
                jpaRepository.findPendingPaymentEnrollments();
        return mapper.toDomainList(jpaEntities);
    }
}