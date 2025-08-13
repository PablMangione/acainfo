package com.acainfo.backend.groupsession.infrastructure.repository.imp;

import com.acainfo.backend.groupsession.domain.entity.GroupSession;
import com.acainfo.backend.groupsession.domain.exception.DuplicateGroupSessionException;
import com.acainfo.backend.groupsession.domain.exception.InvalidGroupSessionDataException;
import com.acainfo.backend.groupsession.domain.repository.CreateGroupSessionRepository;
import com.acainfo.backend.groupsession.infrastructure.repository.jpa.GroupSessionJpaRepository;
import com.acainfo.backend.groupsession.infrastructure.repository.jpa.entity.GroupSessionJpa;
import com.acainfo.backend.groupsession.infrastructure.repository.mapper.GroupSessionJpaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del repositorio de creación de sesiones de grupo.
 * Adapta la interfaz del dominio a la infraestructura de persistencia.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class CreateGroupSessionRepositoryImp implements CreateGroupSessionRepository {

    private final GroupSessionJpaRepository jpaRepository;
    private final GroupSessionJpaMapper mapper;

    /**
     * Persiste una nueva sesión de grupo en el sistema.
     * Las validaciones de formato y negocio se realizan en capas superiores.
     *
     * @param groupSession la sesión a crear
     * @return la sesión creada con su ID generado
     * @throws DuplicateGroupSessionException si ya existe una sesión con el mismo horario
     * @throws InvalidGroupSessionDataException si ocurre un error de persistencia
     */
    @Override
    @Transactional
    public GroupSession save(GroupSession groupSession) {
        log.debug("Iniciando creación de sesión para grupo ID: {}", groupSession.getGroupId());

        // Validación defensiva mínima
        if (groupSession == null) {
            throw new InvalidGroupSessionDataException("La sesión no puede ser null");
        }

        try {
            // Convertir de dominio a JPA
            GroupSessionJpa jpaEntity = mapper.toJpa(groupSession);

            // Persistir en base de datos
            GroupSessionJpa savedEntity = jpaRepository.save(jpaEntity);

            log.info("Sesión creada exitosamente con ID: {}", savedEntity.getId());

            // Convertir de vuelta a dominio y retornar
            return mapper.toDomain(savedEntity);

        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad al crear sesión: {}", e.getMessage());

            // Verificar si es un error de constraint único de horario del grupo
            if (e.getMessage() != null && e.getMessage().contains("uk_session_group_day_time")) {
                throw new DuplicateGroupSessionException(
                        String.format("Ya existe una sesión para el grupo en el día %s a las %s",
                                groupSession.getDayOfWeek(), groupSession.getStartTime())
                );
            }

            // Verificar si es un error de constraint único de aula
            if (e.getMessage() != null && e.getMessage().contains("uk_classroom_day_time")) {
                throw new DuplicateGroupSessionException(
                        String.format("El aula %s ya está ocupada el %s en ese horario",
                                groupSession.getClassroom(), groupSession.getDayOfWeek())
                );
            }

            // Verificar integridad referencial (grupo no existe)
            if (e.getMessage() != null &&
                    (e.getMessage().contains("fk_session_group") ||
                            e.getMessage().contains("group_id"))) {
                throw new InvalidGroupSessionDataException(
                        "El grupo especificado no existe"
                );
            }

            throw new InvalidGroupSessionDataException("Error de integridad en los datos");
        }
    }
}