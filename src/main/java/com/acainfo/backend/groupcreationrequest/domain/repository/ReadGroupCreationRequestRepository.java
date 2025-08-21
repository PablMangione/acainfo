package com.acainfo.backend.groupcreationrequest.domain.repository;

import com.acainfo.backend.groupcreationrequest.domain.entity.GroupCreationRequest;
import com.acainfo.backend.groupcreationrequest.domain.value.RequestStatus;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones de lectura para la entidad GroupCreationRequest.
 *
 * Proporciona métodos para consultar solicitudes por diferentes criterios.
 * Todas las consultas son de solo lectura y no modifican el estado.
 */
public interface ReadGroupCreationRequestRepository {

    /**
     * Busca una solicitud por su ID.
     *
     * @param id el identificador de la solicitud
     * @return un Optional con la solicitud si existe
     */
    Optional<GroupCreationRequest> findById(Long id);

    /**
     * Obtiene todas las solicitudes del sistema.
     *
     * @return lista de todas las solicitudes
     */
    List<GroupCreationRequest> findAll();

    /**
     * Busca solicitudes por estudiante.
     *
     * @param studentId el ID del estudiante
     * @return lista de solicitudes del estudiante
     */
    List<GroupCreationRequest> findByStudentId(Long studentId);

    /**
     * Busca solicitudes por asignatura.
     *
     * @param subjectId el ID de la asignatura
     * @return lista de solicitudes para la asignatura
     */
    List<GroupCreationRequest> findBySubjectId(Long subjectId);

    /**
     * Busca solicitudes por estado.
     *
     * @param status el estado de las solicitudes
     * @return lista de solicitudes con el estado especificado
     */
    List<GroupCreationRequest> findByStatus(RequestStatus status);

    /**
     * Busca solicitudes por asignatura y estado.
     *
     * @param subjectId ID de la asignatura
     * @param status estado de las solicitudes
     * @return lista de solicitudes que coinciden con los criterios
     */
    List<GroupCreationRequest> findBySubjectIdAndStatus(Long subjectId, RequestStatus status);

    /**
     * Busca solicitudes pendientes de un estudiante para una asignatura.
     *
     * @param studentId el ID del estudiante
     * @param subjectId el ID de la asignatura
     * @return lista de solicitudes pendientes
     */
    List<GroupCreationRequest> findPendingByStudentAndSubject(Long studentId, Long subjectId);

    /**
     * Verifica si existe una solicitud con el ID especificado.
     *
     * @param id el identificador a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsById(Long id);

    /**
     * Cuenta las solicitudes pendientes para una asignatura.
     *
     * @param subjectId el ID de la asignatura
     * @return número de solicitudes pendientes
     */
    long countPendingBySubject(Long subjectId);
}