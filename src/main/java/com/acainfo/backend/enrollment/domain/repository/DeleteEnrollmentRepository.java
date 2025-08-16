package com.acainfo.backend.enrollment.domain.repository;

import com.acainfo.backend.enrollment.domain.entity.EnrollmentId;

/**
 * Interfaz que define las operaciones de eliminación para la entidad Enrollment.
 *
 * Las restricciones de integridad referencial se manejan mediante
 * foreign keys y constraints en la base de datos.
 */
public interface DeleteEnrollmentRepository {

    /**
     * Elimina una inscripción por su ID compuesto.
     *
     * @param id el identificador compuesto de la inscripción
     * @return true si fue eliminada, false si no existía
     */
    boolean deleteById(EnrollmentId id);

    /**
     * Elimina todas las inscripciones de un estudiante.
     * Usar con precaución, principalmente cuando se elimina un estudiante.
     *
     * @param studentId el identificador del estudiante
     * @return el número de inscripciones eliminadas
     */
    int deleteByStudentId(Long studentId);

    /**
     * Elimina todas las inscripciones de un grupo.
     * Usar con precaución, principalmente cuando se elimina un grupo.
     *
     * @param groupId el identificador del grupo
     * @return el número de inscripciones eliminadas
     */
    int deleteByGroupId(Long groupId);

    /**
     * Elimina todas las inscripciones.
     * Usar con precaución, principalmente para tests.
     */
    void deleteAll();
}