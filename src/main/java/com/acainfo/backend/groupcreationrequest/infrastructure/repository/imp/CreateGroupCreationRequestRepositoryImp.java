package com.acainfo.backend.groupcreationrequest.infrastructure.repository.imp;

import com.acainfo.backend.groupcreationrequest.domain.entity.GroupCreationRequest;
import com.acainfo.backend.groupcreationrequest.domain.repository.CreateGroupCreationRequestRepository;
import com.acainfo.backend.groupcreationrequest.infrastructure.repository.jpa.GroupCreationRequestJpaRepository;
import com.acainfo.backend.groupcreationrequest.infrastructure.repository.mapper.GroupCreationRequestJpaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del repositorio de creación de solicitudes de creación de grupo.
 * Adapta las operaciones del dominio a la infraestructura JPA.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CreateGroupCreationRequestRepositoryImp implements CreateGroupCreationRequestRepository {

    private final GroupCreationRequestJpaRepository jpaRepository;
    private final GroupCreationRequestJpaMapper mapper;

    @Override
    public GroupCreationRequest save(GroupCreationRequest groupCreationRequest) {
        log.debug("Creando nueva solicitud de grupo para estudiante {} y asignatura {}",
                groupCreationRequest.getStudentId(),
                groupCreationRequest.getSubjectId());

        var jpaEntity = mapper.toJpa(groupCreationRequest);
        var savedEntity = jpaRepository.save(jpaEntity);
        var result = mapper.toDomain(savedEntity);

        log.info("Solicitud de grupo creada con ID: {} para estudiante: {} y asignatura: {}",
                result.getId(),
                result.getStudentId(),
                result.getSubjectId());

        return result;
    }
}