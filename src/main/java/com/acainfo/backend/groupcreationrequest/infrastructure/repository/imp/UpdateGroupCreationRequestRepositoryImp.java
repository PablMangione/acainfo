package com.acainfo.backend.groupcreationrequest.infrastructure.repository.imp;

import com.acainfo.backend.groupcreationrequest.domain.entity.GroupCreationRequest;
import com.acainfo.backend.groupcreationrequest.domain.repository.UpdateGroupCreationRequestRepository;
import com.acainfo.backend.groupcreationrequest.domain.value.RequestStatus;
import com.acainfo.backend.groupcreationrequest.infrastructure.repository.jpa.GroupCreationRequestJpaRepository;
import com.acainfo.backend.groupcreationrequest.infrastructure.repository.mapper.GroupCreationRequestJpaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Implementación del repositorio de actualización de solicitudes de creación de grupo.
 * Adapta las operaciones de actualización del dominio a la infraestructura JPA.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UpdateGroupCreationRequestRepositoryImp implements UpdateGroupCreationRequestRepository {

    private final GroupCreationRequestJpaRepository jpaRepository;
    private final GroupCreationRequestJpaMapper mapper;

    @Override
    public GroupCreationRequest update(GroupCreationRequest groupCreationRequest) {
        log.debug("Actualizando solicitud de grupo con ID: {}", groupCreationRequest.getId());

        var existingEntity = jpaRepository.findById(groupCreationRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se puede actualizar. Solicitud no encontrada con ID: " + groupCreationRequest.getId()
                ));

        mapper.updateJpaFromDomain(groupCreationRequest, existingEntity);
        var updatedEntity = jpaRepository.save(existingEntity);
        var result = mapper.toDomain(updatedEntity);

        log.info("Solicitud de grupo actualizada con ID: {}", result.getId());
        return result;
    }

    @Override
    public boolean updateStatus(Long id, RequestStatus newStatus) {
        log.debug("Actualizando estado de solicitud {} a {}", id, newStatus);

        int updatedRows = jpaRepository.updateStatus(id, newStatus, LocalDateTime.now());
        boolean success = updatedRows > 0;

        if (success) {
            log.info("Estado de solicitud {} actualizado a {}", id, newStatus);
        } else {
            log.warn("No se pudo actualizar el estado de la solicitud {}", id);
        }

        return success;
    }

    @Override
    public boolean existsById(Long id) {
        log.debug("Verificando existencia de solicitud con ID: {}", id);
        return jpaRepository.existsById(id);
    }
}