package com.acainfo.backend.groupcreationrequest.infrastructure.repository.imp;

import com.acainfo.backend.groupcreationrequest.domain.repository.DeleteGroupCreationRequestRepository;
import com.acainfo.backend.groupcreationrequest.domain.value.RequestStatus;
import com.acainfo.backend.groupcreationrequest.infrastructure.repository.jpa.GroupCreationRequestJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Implementación del repositorio de eliminación de solicitudes de creación de grupo.
 * Adapta las operaciones de eliminación del dominio a la infraestructura JPA.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DeleteGroupCreationRequestRepositoryImp implements DeleteGroupCreationRequestRepository {

    private final GroupCreationRequestJpaRepository jpaRepository;

    @Override
    public boolean deleteById(Long id) {
        log.debug("Eliminando solicitud de grupo con ID: {}", id);

        if (!jpaRepository.existsById(id)) {
            log.warn("Intento de eliminar solicitud inexistente con ID: {}", id);
            return false;
        }

        jpaRepository.deleteById(id);
        log.info("Solicitud de grupo eliminada con ID: {}", id);
        return true;
    }

    @Override
    public int deleteOldProcessedRequests(int daysOld) {
        log.debug("Eliminando solicitudes procesadas con más de {} días de antigüedad", daysOld);

        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
        int deletedCount = jpaRepository.deleteOldProcessedRequests(cutoffDate);

        log.info("Eliminadas {} solicitudes antiguas procesadas", deletedCount);
        return deletedCount;
    }

    @Override
    public boolean canBeDeleted(Long id) {
        log.debug("Verificando si la solicitud {} puede ser eliminada", id);

        return jpaRepository.findById(id)
                .map(request -> {
                    // Solo se pueden eliminar solicitudes que no están pendientes
                    boolean canDelete = request.getStatus() != RequestStatus.PENDING;
                    log.debug("Solicitud {} {} ser eliminada (estado: {})",
                            id,
                            canDelete ? "puede" : "no puede",
                            request.getStatus());
                    return canDelete;
                })
                .orElse(false);
    }
}