package com.acainfo.backend.groupcreationrequest.domain.repository;

import com.acainfo.backend.groupcreationrequest.domain.entity.GroupCreationRequest;
import com.acainfo.backend.groupcreationrequest.domain.value.RequestStatus;

/**
 * Interfaz que define las operaciones de actualización para la entidad GroupCreationRequest.
 *
 * Las validaciones de integridad se manejan mediante constraints en la base de datos.
 * Las validaciones de formato se realizan en los DTOs de entrada.
 */
public interface UpdateGroupCreationRequestRepository {

    /**
     * Actualiza una solicitud existente.
     *
     * @param groupCreationRequest la solicitud con los datos actualizados
     * @return la solicitud actualizada
     */
    GroupCreationRequest update(GroupCreationRequest groupCreationRequest);

    /**
     * Actualiza el estado de una solicitud.
     *
     * @param id el ID de la solicitud
     * @param newStatus el nuevo estado
     * @return true si se actualizó correctamente
     */
    boolean updateStatus(Long id, RequestStatus newStatus);

    /**
     * Verifica si una solicitud existe antes de intentar actualizarla.
     *
     * @param id el identificador a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsById(Long id);
}