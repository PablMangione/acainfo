package com.acainfo.backend.subject.application;

import com.acainfo.backend.subject.domain.entity.Subject;

/**
 * Caso de uso para la creación de asignaturas.
 *
 * Esta interfaz define el contrato para la creación de nuevas asignaturas en el sistema.
 * Trabaja exclusivamente con entidades de dominio, manteniendo la independencia
 * de la capa de aplicación respecto a los detalles de infraestructura (DTOs).
 *
 * La conversión entre DTOs y entidades de dominio se realiza en la capa de infraestructura
 * (controladores), no en los casos de uso.
 */
public interface CreateSubjectUseCase {

    /**
     * Crea una nueva asignatura en el sistema.
     *
     * Responsabilidades:
     * - Validar las reglas de negocio
     * - Verificar que no existan duplicados
     * - Aplicar transformaciones de negocio necesarias
     * - Persistir la asignatura a través del repositorio
     *
     * @param subject la asignatura a crear (sin ID, será generado)
     * @return la asignatura creada con su ID generado y timestamps
     *
     * @throws com.acainfo.backend.subject.domain.exception.DuplicateSubjectException
     *         si ya existe una asignatura con la misma combinación única
     *         (nombre + carrera + año + cuatrimestre)
     *
     * @throws com.acainfo.backend.subject.domain.exception.InvalidSubjectDataException
     *         si los datos proporcionados no cumplen con las reglas de negocio
     *
     * @throws IllegalArgumentException
     *         si el subject es null o tiene un ID ya asignado
     */
    Subject create(Subject subject);
}