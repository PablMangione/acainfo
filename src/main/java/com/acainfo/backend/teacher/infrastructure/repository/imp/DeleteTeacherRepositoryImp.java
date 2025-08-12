package com.acainfo.backend.teacher.infrastructure.repository.imp;

import com.acainfo.backend.teacher.domain.exception.InvalidTeacherDataException;
import com.acainfo.backend.teacher.domain.repository.DeleteTeacherRepository;
import com.acainfo.backend.teacher.infrastructure.repository.jpa.TeacherJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del repositorio de eliminación de profesores.
 * Adapta las operaciones de eliminación del dominio a la infraestructura JPA.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class DeleteTeacherRepositoryImp implements DeleteTeacherRepository {

    private final TeacherJpaRepository jpaRepository;

    /**
     * Elimina un profesor por su ID.
     *
     * @param id el identificador del profesor
     * @return true si fue eliminado, false si no existía
     * @throws InvalidTeacherDataException si hay restricciones de integridad referencial
     */
    @Override
    @Transactional
    public boolean deleteById(Long id) {
        log.debug("Iniciando eliminación de profesor con ID: {}", id);

        // Verificar si existe antes de intentar eliminar
        if (!jpaRepository.existsById(id)) {
            log.warn("Intento de eliminar profesor inexistente. ID: {}", id);
            return false;
        }

        try {
            jpaRepository.deleteById(id);
            log.info("Profesor eliminado exitosamente. ID: {}", id);
            return true;

        } catch (DataIntegrityViolationException e) {
            log.error("No se puede eliminar el profesor con ID: {} debido a restricciones de integridad", id);

            throw new InvalidTeacherDataException(
                    "No se puede eliminar el profesor porque tiene datos relacionados (grupos asignados, etc.)"
            );
        }
    }

    /**
     * Elimina todos los profesores.
     * Usar con precaución, principalmente para tests.
     *
     * @throws InvalidTeacherDataException si hay restricciones de integridad referencial
     */
    @Override
    @Transactional
    public void deleteAll() {
        log.warn("Iniciando eliminación de TODOS los profesores");

        try {
            Long count = jpaRepository.count();
            jpaRepository.deleteAll();
            log.info("Se eliminaron {} profesores", count);

        } catch (DataIntegrityViolationException e) {
            log.error("No se pueden eliminar los profesores debido a restricciones de integridad");

            throw new InvalidTeacherDataException(
                    "No se pueden eliminar los profesores porque existen datos relacionados"
            );
        }
    }
}