package com.acainfo.backend.groupcreationrequest.infrastructure.repository.imp;

import com.acainfo.backend.groupcreationrequest.domain.entity.GroupCreationRequest;
import com.acainfo.backend.groupcreationrequest.domain.repository.ReadGroupCreationRequestRepository;
import com.acainfo.backend.groupcreationrequest.domain.value.RequestStatus;
import com.acainfo.backend.groupcreationrequest.infrastructure.repository.jpa.GroupCreationRequestJpaRepository;
import com.acainfo.backend.groupcreationrequest.infrastructure.repository.mapper.GroupCreationRequestJpaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del repositorio de lectura de solicitudes de creación de grupo.
 * Adapta las consultas del dominio a la infraestructura JPA.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReadGroupCreationRequestRepositoryImp implements ReadGroupCreationRequestRepository {

    private final GroupCreationRequestJpaRepository jpaRepository;
    private final GroupCreationRequestJpaMapper mapper;

    @Override
    public Optional<GroupCreationRequest> findById(Long id) {
        log.debug("Buscando solicitud de grupo con ID: {}", id);
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<GroupCreationRequest> findAll() {
        log.debug("Obteniendo todas las solicitudes de grupo");
        var requests = jpaRepository.findAll();
        return mapper.toDomainList(requests);
    }

    @Override
    public List<GroupCreationRequest> findByStudentId(Long studentId) {
        log.debug("Buscando solicitudes del estudiante: {}", studentId);
        var requests = jpaRepository.findByStudentIdOrderByRequestedAtDesc(studentId);
        return mapper.toDomainList(requests);
    }

    @Override
    public List<GroupCreationRequest> findBySubjectId(Long subjectId) {
        log.debug("Buscando solicitudes para la asignatura: {}", subjectId);
        var requests = jpaRepository.findBySubjectIdOrderByRequestedAtDesc(subjectId);
        return mapper.toDomainList(requests);
    }

    @Override
    public List<GroupCreationRequest> findByStatus(RequestStatus status) {
        log.debug("Buscando solicitudes con estado: {}", status);
        var requests = jpaRepository.findByStatusOrderByRequestedAtAsc(status);
        return mapper.toDomainList(requests);
    }

    @Override
    public List<GroupCreationRequest> findBySubjectIdAndStatus(Long subjectId, RequestStatus status) {
        log.debug("Buscando solicitudes de la asignatura {} con estado {}", subjectId, status);

        var requests  =
                jpaRepository.findBySubjectIdAndStatus(subjectId, status);

        return mapper.toDomainList(requests);
    }

    @Override
    public List<GroupCreationRequest> findPendingByStudentAndSubject(Long studentId, Long subjectId) {
        log.debug("Buscando solicitudes pendientes del estudiante {} para asignatura {}",
                studentId, subjectId);
        var requests = jpaRepository.findPendingByStudentAndSubject(studentId, subjectId);
        return mapper.toDomainList(requests);
    }

    @Override
    public boolean existsById(Long id) {
        log.debug("Verificando existencia de solicitud con ID: {}", id);
        return jpaRepository.existsById(id);
    }

    @Override
    public long countPendingBySubject(Long subjectId) {
        log.debug("Contando solicitudes pendientes para asignatura: {}", subjectId);
        return jpaRepository.countBySubjectIdAndStatus(subjectId, RequestStatus.PENDING);
    }
}