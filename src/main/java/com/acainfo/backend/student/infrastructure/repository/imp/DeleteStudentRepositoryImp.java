package com.acainfo.backend.student.infrastructure.repository.imp;

import com.acainfo.backend.student.domain.exception.InvalidStudentDataException;
import com.acainfo.backend.student.domain.repository.DeleteStudentRepository;
import com.acainfo.backend.student.infrastructure.repository.jpa.StudentJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del repositorio de eliminación de estudiantes.
 * Adapta las operaciones de eliminación del dominio a la infraestructura JPA.
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class DeleteStudentRepositoryImp implements DeleteStudentRepository {

    private final StudentJpaRepository jpaRepository;

    /**
     * Elimina un estudiante por su ID.
     *
     * @param id el identificador del estudiante
     * @return true si fue eliminado, false si no existía
     * @throws InvalidStudentDataException si hay restricciones de integridad referencial
     */
    @Override
    @Transactional
    public boolean deleteById(Long id) {
        log.debug("Iniciando eliminación de estudiante con ID: {}", id);

        // Verificar si existe antes de intentar eliminar
        if (!jpaRepository.existsById(id)) {
            log.warn("Intento de eliminar estudiante inexistente. ID: {}", id);
            return false;
        }

        try {
            jpaRepository.deleteById(id);
            log.info("Estudiante eliminado exitosamente. ID: {}", id);
            return true;

        } catch (DataIntegrityViolationException e) {
            log.error("No se puede eliminar el estudiante con ID: {} debido a restricciones de integridad", id);

            throw new InvalidStudentDataException(
                    "No se puede eliminar el estudiante porque tiene datos relacionados (inscripciones, solicitudes, etc.)"
            );
        }
    }

    /**
     * Elimina todos los estudiantes.
     * Usar con precaución, principalmente para tests.
     *
     * @throws InvalidStudentDataException si hay restricciones de integridad referencial
     */
    @Override
    @Transactional
    public void deleteAll() {
        log.warn("Iniciando eliminación de TODOS los estudiantes");

        try {
            long count = jpaRepository.count();
            jpaRepository.deleteAll();
            log.info("Se eliminaron {} estudiantes", count);

        } catch (DataIntegrityViolationException e) {
            log.error("No se pueden eliminar los estudiantes debido a restricciones de integridad");

            throw new InvalidStudentDataException(
                    "No se pueden eliminar los estudiantes porque existen datos relacionados"
            );
        }
    }
}