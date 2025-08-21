package com.acainfo.backend.groupcreationrequest.infrastructure.repository.jpa;

import com.acainfo.backend.groupcreationrequest.domain.value.RequestStatus;
import com.acainfo.backend.groupcreationrequest.infrastructure.repository.jpa.entity.GroupCreationRequestJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para la entidad GroupCreationRequest.
 * Proporciona operaciones CRUD y consultas personalizadas.
 */
@Repository
public interface GroupCreationRequestJpaRepository extends JpaRepository<GroupCreationRequestJpa, Long> {

    /**
     * Busca solicitudes por ID de estudiante ordenadas por fecha descendente.
     */
    List<GroupCreationRequestJpa> findByStudentIdOrderByRequestedAtDesc(Long studentId);

    /**
     * Busca solicitudes por ID de asignatura ordenadas por fecha descendente.
     */
    List<GroupCreationRequestJpa> findBySubjectIdOrderByRequestedAtDesc(Long subjectId);

    /**
     * Busca solicitudes por estado ordenadas por fecha ascendente (las más antiguas primero).
     */
    List<GroupCreationRequestJpa> findByStatusOrderByRequestedAtAsc(RequestStatus status);

    /**
     * Busca solicitudes por asignatura y estado.
     */
    List<GroupCreationRequestJpa> findBySubjectIdAndStatus(Long subjectId, RequestStatus status);

    /**
     * Busca solicitudes por estudiante y estado.
     */
    List<GroupCreationRequestJpa> findByStudentIdAndStatus(Long studentId, RequestStatus status);

    /**
     * Busca solicitudes pendientes de un estudiante para una asignatura específica.
     * Útil para evitar solicitudes duplicadas.
     */
    @Query("SELECT gcr FROM GroupCreationRequestJpa gcr " +
            "WHERE gcr.studentId = :studentId " +
            "AND gcr.subjectId = :subjectId " +
            "AND gcr.status = 'PENDING' " +
            "ORDER BY gcr.requestedAt DESC")
    List<GroupCreationRequestJpa> findPendingByStudentAndSubject(
            @Param("studentId") Long studentId,
            @Param("subjectId") Long subjectId
    );

    /**
     * Verifica si existe una solicitud pendiente para un estudiante y asignatura.
     * Útil para validar antes de crear una nueva solicitud.
     */
    boolean existsByStudentIdAndSubjectIdAndStatus(
            Long studentId,
            Long subjectId,
            RequestStatus status
    );

    /**
     * Cuenta las solicitudes por asignatura y estado.
     * Útil para estadísticas y decisiones de creación de grupos.
     */
    long countBySubjectIdAndStatus(Long subjectId, RequestStatus status);

    /**
     * Cuenta todas las solicitudes pendientes.
     * Útil para el dashboard de administradores.
     */
    long countByStatus(RequestStatus status);

    /**
     * Actualiza solo el estado de una solicitud.
     * Usado cuando se aprueba, rechaza o cancela una solicitud.
     */
    @Modifying
    @Query("UPDATE GroupCreationRequestJpa gcr " +
            "SET gcr.status = :status, " +
            "gcr.updatedAt = :updatedAt " +
            "WHERE gcr.id = :id")
    int updateStatus(
            @Param("id") Long id,
            @Param("status") RequestStatus status,
            @Param("updatedAt") LocalDateTime updatedAt
    );

    /**
     * Elimina solicitudes antiguas que ya fueron procesadas.
     * Útil para limpieza de base de datos.
     */
    @Modifying
    @Query("DELETE FROM GroupCreationRequestJpa gcr " +
            "WHERE gcr.status IN ('REJECTED', 'CANCELLED') " +
            "AND gcr.updatedAt < :cutoffDate")
    int deleteOldProcessedRequests(@Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Busca todas las solicitudes pendientes ordenadas por antigüedad.
     * Las más antiguas primero para procesamiento FIFO.
     */
    @Query("SELECT gcr FROM GroupCreationRequestJpa gcr " +
            "WHERE gcr.status = 'PENDING' " +
            "ORDER BY gcr.requestedAt ASC")
    List<GroupCreationRequestJpa> findAllPendingOrderByOldest();

    /**
     * Busca solicitudes por múltiples estados.
     * Útil para obtener todas las solicitudes procesadas o activas.
     */
    @Query("SELECT gcr FROM GroupCreationRequestJpa gcr " +
            "WHERE gcr.status IN :statuses " +
            "ORDER BY gcr.requestedAt DESC")
    List<GroupCreationRequestJpa> findByStatusIn(@Param("statuses") List<RequestStatus> statuses);

}