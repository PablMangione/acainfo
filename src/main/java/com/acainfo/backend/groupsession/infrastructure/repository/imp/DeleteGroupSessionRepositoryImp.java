package com.acainfo.backend.groupsession.infrastructure.repository.imp;

import com.acainfo.backend.groupsession.domain.exception.InvalidGroupSessionDataException;
import com.acainfo.backend.groupsession.domain.repository.DeleteGroupSessionRepository;
import com.acainfo.backend.groupsession.infrastructure.repository.jpa.GroupSessionJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del repositorio de eliminación de sesiones de grupo.
 * Adapta las operaciones de eliminación del dominio a la infraestructura JPA.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class DeleteGroupSessionRepositoryImp implements DeleteGroupSessionRepository {

    private final GroupSessionJpaRepository jpaRepository;

    /**
     * Elimina una sesión por su ID.
     *
     * @param id el identificador de la sesión
     * @return true si fue eliminada, false si no existía
     * @throws InvalidGroupSessionDataException si hay restricciones de integridad referencial
     */
    @Override
    @Transactional
    public boolean deleteById(Long id) {
        log.debug("Iniciando eliminación de sesión con ID: {}", id);

        // Verificar si existe antes de intentar eliminar
        if (!jpaRepository.existsById(id)) {
            log.warn("Intento de eliminar sesión inexistente. ID: {}", id);
            return false;
        }

        try {
            jpaRepository.deleteById(id);
            log.info("Sesión eliminada exitosamente. ID: {}", id);
            return true;

        } catch (DataIntegrityViolationException e) {
            log.error("No se puede eliminar la sesión con ID: {} debido a restricciones de integridad", id);

            throw new InvalidGroupSessionDataException(
                    "No se puede eliminar la sesión porque tiene datos relacionados"
            );
        }
    }

    /**
     * Elimina todas las sesiones de un grupo.
     *
     * @param groupId el identificador del grupo
     */
    @Override
    @Transactional
    public void deleteByGroupId(Long groupId) {
        log.debug("Eliminando todas las sesiones del grupo ID: {}", groupId);

        try {
            long count = jpaRepository.countByGroupId(groupId);
            jpaRepository.deleteByGroupId(groupId);
            log.info("Se eliminaron {} sesiones del grupo ID: {}", count, groupId);

        } catch (DataIntegrityViolationException e) {
            log.error("Error al eliminar las sesiones del grupo ID: {}", groupId);

            throw new InvalidGroupSessionDataException(
                    "No se pueden eliminar las sesiones del grupo"
            );
        }
    }

    /**
     * Elimina todas las sesiones.
     * Usar con precaución, principalmente para tests.
     *
     * @throws InvalidGroupSessionDataException si hay restricciones de integridad referencial
     */
    @Override
    @Transactional
    public void deleteAll() {
        log.warn("Iniciando eliminación de TODAS las sesiones");

        try {
            long count = jpaRepository.count();
            jpaRepository.deleteAll();
            log.info("Se eliminaron {} sesiones", count);

        } catch (DataIntegrityViolationException e) {
            log.error("No se pueden eliminar las sesiones debido a restricciones de integridad");

            throw new InvalidGroupSessionDataException(
                    "No se pueden eliminar las sesiones porque existen datos relacionados"
            );
        }
    }
}