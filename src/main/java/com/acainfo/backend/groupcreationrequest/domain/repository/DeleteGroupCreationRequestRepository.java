package com.acainfo.backend.groupcreationrequest.domain.repository;

/**
 * Interfaz que define las operaciones de eliminación para la entidad GroupCreationRequest.
 *
 * Las eliminaciones pueden ser físicas o lógicas según la estrategia adoptada.
 * Se debe validar que la solicitud pueda ser eliminada según su estado.
 */
public interface DeleteGroupCreationRequestRepository {

    /**
     * Elimina una solicitud por su ID.
     *
     * @param id el identificador de la solicitud a eliminar
     * @return true si se eliminó correctamente, false si no existía
     */
    boolean deleteById(Long id);

    /**
     * Elimina todas las solicitudes canceladas o rechazadas más antiguas
     * que el número de días especificado.
     *
     * @param daysOld número de días de antigüedad
     * @return número de solicitudes eliminadas
     */
    int deleteOldProcessedRequests(int daysOld);

    /**
     * Verifica si una solicitud puede ser eliminada.
     * Por ejemplo, las solicitudes pendientes no deberían eliminarse.
     *
     * @param id el identificador de la solicitud
     * @return true si puede ser eliminada, false en caso contrario
     */
    boolean canBeDeleted(Long id);
}