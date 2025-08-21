package com.acainfo.backend.subject.infrastructure.repository.imp;

import com.acainfo.backend.subject.domain.exception.InvalidSubjectDataException;
import com.acainfo.backend.subject.domain.repository.DeleteSubjectRepository;
import com.acainfo.backend.subject.infrastructure.repository.jpa.SubjectJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del repositorio de eliminación de asignaturas.
 * Adapta las operaciones de eliminación del dominio a la infraestructura JPA.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class DeleteSubjectRepositoryImp implements DeleteSubjectRepository {

    private final SubjectJpaRepository jpaRepository;

    /**
     * Elimina una asignatura por su ID.
     *
     * @param id el identificador de la asignatura
     * @return true si fue eliminada, false si no existía
     * @throws InvalidSubjectDataException si hay restricciones de integridad referencial
     */
    @Override
    @Transactional
    public boolean deleteById(Long id) {
        log.debug("Iniciando eliminación de asignatura con ID: {}", id);

        // Verificar si existe antes de intentar eliminar
        if (!jpaRepository.existsById(id)) {
            log.warn("Intento de eliminar asignatura inexistente. ID: {}", id);
            return false;
        }

        try {
            jpaRepository.deleteById(id);
            log.info("Asignatura eliminada exitosamente. ID: {}", id);
            return true;

        } catch (DataIntegrityViolationException e) {
            log.error("No se puede eliminar la asignatura con ID: {} debido a restricciones de integridad", id);

            throw new InvalidSubjectDataException(
                    "No se puede eliminar la asignatura porque tiene datos relacionados (grupos, inscripciones, etc.)"
            );
        }
    }

    /**
     * Elimina todas las asignaturas.
     * Usar con precaución, principalmente para tests.
     *
     * @throws InvalidSubjectDataException si hay restricciones de integridad referencial
     */
    @Override
    @Transactional
    public void deleteAll() {
        log.warn("Iniciando eliminación de TODAS las asignaturas");

        try {
            long count = jpaRepository.count();
            jpaRepository.deleteAll();
            log.info("Se eliminaron {} asignaturas", count);

        } catch (DataIntegrityViolationException e) {
            log.error("No se pueden eliminar las asignaturas debido a restricciones de integridad");

            throw new InvalidSubjectDataException(
                    "No se pueden eliminar las asignaturas porque existen datos relacionados"
            );
        }
    }
}