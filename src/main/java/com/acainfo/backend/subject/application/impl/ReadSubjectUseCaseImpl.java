package com.acainfo.backend.subject.application.impl;

import com.acainfo.backend.globalenum.Major;
import com.acainfo.backend.subject.application.ReadSubjectUseCase;
import com.acainfo.backend.subject.application.mapper.SubjectMapper;
import com.acainfo.backend.subject.domain.entity.Subject;
import com.acainfo.backend.subject.domain.exception.SubjectNotFoundException;
import com.acainfo.backend.subject.domain.repository.ReadSubjectRepository;
import com.acainfo.backend.subject.domain.value.CourseYear;
import com.acainfo.backend.subject.domain.value.Quarter;
import com.acainfo.backend.subject.infrastructure.controller.dto.SubjectOutputDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del caso de uso para consultar asignaturas.
 * Coordina las operaciones de lectura y aplica filtros de negocio.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReadSubjectUseCaseImpl implements ReadSubjectUseCase {

    private final ReadSubjectRepository readRepository;
    private final SubjectMapper mapper;

    @Override
    public SubjectOutputDto findById(long id) {
        log.debug("Buscando asignatura por ID: {}", id);

        Subject subject = readRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Asignatura no encontrada con ID: {}", id);
                    return new SubjectNotFoundException(
                            "No se encontró la asignatura con ID: " + id
                    );
                });

        return mapper.toOutputDto(subject);
    }

    @Override
    public List<SubjectOutputDto> findAll() {
        log.debug("Obteniendo todas las asignaturas");

        List<Subject> subjects = readRepository.findAll();

        log.info("Se encontraron {} asignaturas en total", subjects.size());

        return mapper.toOutputDtoList(subjects);
    }

    @Override
    public List<SubjectOutputDto> findAllActive() {
        log.debug("Obteniendo asignaturas activas");

        List<Subject> subjects = readRepository.findAll()
                .stream()
                .filter(Subject::isActive)
                .collect(Collectors.toList());

        log.info("Se encontraron {} asignaturas activas", subjects.size());

        return mapper.toOutputDtoList(subjects);
    }

    @Override
    public List<SubjectOutputDto> findByMajor(Major major) {
        log.debug("Buscando asignaturas por carrera: {}", major);

        List<Subject> subjects = readRepository.findByMajor(major);

        log.info("Se encontraron {} asignaturas para {}", subjects.size(), major);

        return mapper.toOutputDtoList(subjects);
    }

    @Override
    public List<SubjectOutputDto> findByMajorActive(Major major) {
        log.debug("Buscando asignaturas activas por carrera: {}", major);

        List<Subject> subjects = readRepository.findByMajor(major)
                .stream()
                .filter(Subject::isActive)
                .sorted((s1, s2) -> {
                    // Ordenar por año, luego cuatrimestre, luego nombre
                    int yearCompare = s1.getCourseYear().compareTo(s2.getCourseYear());
                    if (yearCompare != 0) return yearCompare;

                    int quarterCompare = s1.getQuarter().compareTo(s2.getQuarter());
                    if (quarterCompare != 0) return quarterCompare;

                    return s1.getName().compareTo(s2.getName());
                })
                .collect(Collectors.toList());

        log.info("Se encontraron {} asignaturas activas para {}", subjects.size(), major);

        return mapper.toOutputDtoList(subjects);
    }

    @Override
    public List<SubjectOutputDto> findByMajorAndCourseYear(Major major, CourseYear courseYear) {
        log.debug("Buscando asignaturas por carrera: {} y año: {}", major, courseYear);

        List<Subject> subjects = readRepository.findByMajorAndCourseYear(major, courseYear);

        log.info("Se encontraron {} asignaturas para {} - {} año",
                subjects.size(), major, courseYear);

        return mapper.toOutputDtoList(subjects);
    }

    @Override
    public List<SubjectOutputDto> findByMajorAndCourseYearAndQuarter(
            Major major,
            CourseYear courseYear,
            Quarter quarter) {
        log.debug("Buscando asignaturas por carrera: {}, año: {} y cuatrimestre: {}",
                major, courseYear, quarter);

        List<Subject> subjects = readRepository.findByMajorAndCourseYearAndQuarter(
                major, courseYear, quarter);

        log.info("Se encontraron {} asignaturas para {} - {} año - {} cuatrimestre",
                subjects.size(), major, courseYear, quarter);

        return mapper.toOutputDtoList(subjects);
    }

    @Override
    public List<SubjectOutputDto> searchByName(String name) {
        log.debug("Buscando asignaturas por nombre que contenga: '{}'", name);

        // Validación de entrada
        if (name == null || name.trim().isEmpty()) {
            log.warn("Búsqueda con nombre vacío o null");
            return List.of();
        }

        // Limpiar el término de búsqueda
        String searchTerm = name.trim();

        // Si el término es muy corto, podríamos requerir coincidencia exacta
        if (searchTerm.length() < 3) {
            log.debug("Término de búsqueda muy corto, requiriendo al menos 3 caracteres");
            return List.of();
        }

        List<Subject> subjects = readRepository.findByNameContainingIgnoreCase(searchTerm);

        log.info("Se encontraron {} asignaturas que contienen '{}'",
                subjects.size(), searchTerm);

        return mapper.toOutputDtoList(subjects);
    }

    @Override
    public boolean existsById(long id) {
        log.debug("Verificando existencia de asignatura con ID: {}", id);

        boolean exists = readRepository.existsById(id);

        log.debug("Asignatura con ID {} existe: {}", id, exists);

        return exists;
    }
}