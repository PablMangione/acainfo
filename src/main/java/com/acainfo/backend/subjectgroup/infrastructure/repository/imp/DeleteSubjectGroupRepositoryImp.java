package com.acainfo.backend.subjectgroup.infrastructure.repository.imp;

import com.acainfo.backend.subjectgroup.domain.exception.InvalidSubjectGroupDataException;
import com.acainfo.backend.subjectgroup.domain.repository.DeleteSubjectGroupRepository;
import com.acainfo.backend.subjectgroup.infrastructure.repository.jpa.SubjectGroupJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del repositorio de eliminación de grupos de asignatura.
 * Adapta las operaciones de eliminación del dominio a la infraestructura JPA.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class DeleteSubjectGroupRepositoryImp implements DeleteSubjectGroupRepository {

    private final SubjectGroupJpaRepository jpaRepository;

    /**
     * Elimina un grupo por su ID.
     * Solo se permite si no tiene inscripciones activas.
     *
     * @param id el identificador del grupo
     * @return true si fue eliminado, false si no existía
     * @throws InvalidSubjectGroupDataException si hay restricciones de integridad referencial
     */
    @Override
    @Transactional
    public boolean deleteById(Long id) {
        log.debug("Iniciando eliminación de grupo con ID: {}", id);

        // Verificar si existe antes de intentar eliminar
        if (!jpaRepository.existsById(id)) {
            log.warn("Intento de eliminar grupo inexistente. ID: {}", id);
            return false;
        }

        try {
            jpaRepository.deleteById(id);
            log.info("Grupo eliminado exitosamente. ID: {}", id);
            return true;

        } catch (DataIntegrityViolationException e) {
            log.error("No se puede eliminar el grupo con ID: {} debido a restricciones de integridad", id);

            throw new InvalidSubjectGroupDataException(
                    "No se puede eliminar el grupo porque tiene datos relacionados " +
                            "(inscripciones activas, sesiones programadas, etc.)"
            );
        }
    }

    /**
     * Elimina todos los grupos.
     * Usar con precaución, principalmente para tests.
     *
     * @throws InvalidSubjectGroupDataException si hay restricciones de integridad referencial
     */
    @Override
    @Transactional
    public void deleteAll() {
        log.warn("Iniciando eliminación de TODOS los grupos");

        try {
            long count = jpaRepository.count();
            jpaRepository.deleteAll();
            log.info("Se eliminaron {} grupos", count);

        } catch (DataIntegrityViolationException e) {
            log.error("No se pueden eliminar los grupos debido a restricciones de integridad");

            throw new InvalidSubjectGroupDataException(
                    "No se pueden eliminar los grupos porque existen datos relacionados"
            );
        }
    }

    /**
     * Verifica si un grupo puede ser eliminado.
     * Un grupo no puede eliminarse si tiene inscripciones activas.
     *
     * @param id el identificador del grupo
     * @return true si puede ser eliminado
     */
    @Override
    @Transactional(readOnly = true)
    public boolean canBeDeleted(Long id) {
        log.debug("Verificando si el grupo con ID: {} puede ser eliminado", id);

        // Verificar si existe
        if (!jpaRepository.existsById(id)) {
            log.warn("Grupo con ID: {} no existe", id);
            return false;
        }

        // Verificar si tiene inscripciones
        boolean hasEnrollments = jpaRepository.hasEnrollments(id);

        if (hasEnrollments) {
            log.debug("Grupo con ID: {} tiene inscripciones activas, no puede ser eliminado", id);
            return false;
        }

        log.debug("Grupo con ID: {} puede ser eliminado", id);
        return true;
    }
}