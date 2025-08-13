package com.acainfo.backend.groupsession.infrastructure.repository.imp;

import com.acainfo.backend.groupsession.domain.entity.GroupSession;
import com.acainfo.backend.groupsession.domain.exception.DuplicateGroupSessionException;
import com.acainfo.backend.groupsession.domain.exception.GroupSessionNotFoundException;
import com.acainfo.backend.groupsession.domain.exception.InvalidGroupSessionDataException;
import com.acainfo.backend.groupsession.domain.repository.UpdateGroupSessionRepository;
import com.acainfo.backend.groupsession.infrastructure.repository.jpa.GroupSessionJpaRepository;
import com.acainfo.backend.groupsession.infrastructure.repository.jpa.entity.GroupSessionJpa;
import com.acainfo.backend.groupsession.infrastructure.repository.mapper.GroupSessionJpaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del repositorio de actualización de sesiones de grupo.
 * Adapta las operaciones de actualización del dominio a la infraestructura JPA.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class UpdateGroupSessionRepositoryImp implements UpdateGroupSessionRepository {

    private final GroupSessionJpaRepository jpaRepository;
    private final GroupSessionJpaMapper mapper;

    /**
     * Actualiza una sesión existente.
     * Las validaciones de formato y negocio se realizan en capas superiores.
     *
     * @param groupSession la sesión con los datos actualizados
     * @return la sesión actualizada
     * @throws GroupSessionNotFoundException si la sesión no existe
     * @throws DuplicateGroupSessionException si la actualización viola constraints únicos
     */
    @Override
    @Transactional
    public GroupSession update(GroupSession groupSession) {
        log.debug("Iniciando actualización de sesión con ID: {}", groupSession.getId());

        // Validación defensiva mínima
        if (groupSession == null) {
            throw new InvalidGroupSessionDataException("La sesión no puede ser null");
        }

        if (groupSession.getId() == null || groupSession.getId() == 0L) {
            throw new InvalidGroupSessionDataException("El ID de la sesión es requerido para actualizar");
        }

        try {
            // Buscar la entidad existente
            GroupSessionJpa existingEntity = jpaRepository.findById(groupSession.getId())
                    .orElseThrow(() -> {
                        log.error("No se encontró sesión con ID: {}", groupSession.getId());
                        return new GroupSessionNotFoundException(
                                "No se encontró la sesión con ID: " + groupSession.getId()
                        );
                    });

            // Actualizar los campos usando el mapper
            mapper.updateJpaFromDomain(groupSession, existingEntity);

            // Persistir los cambios
            GroupSessionJpa updatedEntity = jpaRepository.save(existingEntity);

            log.info("Sesión actualizada exitosamente. ID: {}", updatedEntity.getId());

            // Convertir de vuelta a dominio y retornar
            return mapper.toDomain(updatedEntity);

        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad al actualizar sesión: {}", e.getMessage());

            // Verificar si es un error de constraint único de horario
            if (e.getMessage() != null && e.getMessage().contains("uk_session_group_day_time")) {
                throw new DuplicateGroupSessionException(
                        "Ya existe otra sesión para el grupo en ese día y hora"
                );
            }

            // Verificar si es un error de constraint único de aula
            if (e.getMessage() != null && e.getMessage().contains("uk_classroom_day_time")) {
                throw new DuplicateGroupSessionException(
                        "El aula ya está ocupada en ese horario"
                );
            }

            throw new InvalidGroupSessionDataException("Error de integridad en los datos");
        }
    }

    @Override
    public boolean existsById(Long id) {
        log.debug("Verificando existencia de sesión con ID: {}", id);
        return jpaRepository.existsById(id);
    }
}