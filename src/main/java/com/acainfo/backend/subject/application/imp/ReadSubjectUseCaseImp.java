package com.acainfo.backend.subject.application.imp;

import com.acainfo.backend.globalenum.Major;
import com.acainfo.backend.subject.application.ReadSubjectUseCase;
import com.acainfo.backend.subject.domain.entity.Subject;
import com.acainfo.backend.subject.domain.exception.SubjectNotFoundException;
import com.acainfo.backend.subject.domain.repository.ReadSubjectRepository;
import com.acainfo.backend.subject.domain.value.CourseYear;
import com.acainfo.backend.subject.domain.value.Quarter;
import com.acainfo.backend.subjectgroup.domain.entity.SubjectGroup;
import com.acainfo.backend.subjectgroup.domain.repository.ReadSubjectGroupRepository;
import com.acainfo.backend.subjectgroup.domain.value.GroupStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del caso de uso para la lectura de asignaturas.
 *
 * La mayoría de las operaciones son delegaciones directas al repositorio,
 * con validaciones mínimas de parámetros.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReadSubjectUseCaseImp implements ReadSubjectUseCase {

    private final ReadSubjectRepository readSubjectRepository;
    private final ReadSubjectGroupRepository readSubjectGroupRepository;

    @Override
    public Optional<Subject> findById(Long id) {
        log.debug("Buscando asignatura con ID: {}", id);

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que 0");
        }

        return readSubjectRepository.findById(id);
    }

    @Override
    public List<Subject> findAll() {
        log.debug("Obteniendo todas las asignaturas");
        return readSubjectRepository.findAll();
    }

    @Override
    public List<Subject> findAllActive() {
        log.debug("Obteniendo asignaturas activas");

        return readSubjectRepository.findAll().stream()
                .filter(Subject::getIsActive)
                .collect(Collectors.toList());
    }

    @Override
    public List<Subject> findByMajor(Major major) {
        log.debug("Buscando asignaturas de la carrera: {}", major);

        if (major == null) {
            throw new IllegalArgumentException("La carrera no puede ser null");
        }

        return readSubjectRepository.findByMajor(major);
    }

    @Override
    public List<Subject> findByMajorActive(Major major) {
        log.debug("Buscando asignaturas activas de la carrera: {}", major);

        if (major == null) {
            throw new IllegalArgumentException("La carrera no puede ser null");
        }

        return readSubjectRepository.findByMajor(major).stream()
                .filter(Subject::getIsActive)
                .collect(Collectors.toList());
    }

    @Override
    public List<Subject> findByMajorAndCourseYear(Major major, CourseYear courseYear) {
        log.debug("Buscando asignaturas de {} - {}", major, courseYear);

        if (major == null || courseYear == null) {
            throw new IllegalArgumentException("Los parámetros no pueden ser null");
        }

        return readSubjectRepository.findByMajorAndCourseYear(major, courseYear);
    }

    @Override
    public List<Subject> findByMajorAndCourseYearAndQuarter(
            Major major, CourseYear courseYear, Quarter quarter) {
        log.debug("Buscando asignaturas de {} - {} - {}", major, courseYear, quarter);

        if (major == null || courseYear == null || quarter == null) {
            throw new IllegalArgumentException("Los parámetros no pueden ser null");
        }

        return readSubjectRepository.findByMajorAndCourseYearAndQuarter(major, courseYear, quarter);
    }

    @Override
    public List<Subject> searchByName(String name) {
        log.debug("Buscando asignaturas por nombre: {}", name);

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de búsqueda no puede estar vacío");
        }

        return readSubjectRepository.findByNameContainingIgnoreCase(name.trim());
    }

    @Override
    public boolean existsById(Long id) {
        log.debug("Verificando existencia de asignatura con ID: {}", id);

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que 0");
        }

        return readSubjectRepository.existsById(id);
    }

    @Override
    public long countAll() {
        log.debug("Contando total de asignaturas");
        return readSubjectRepository.findAll().size();
    }

    @Override
    public long countActive() {
        log.debug("Contando asignaturas activas");

        return readSubjectRepository.findAll().stream()
                .filter(Subject::getIsActive)
                .count();
    }

    @Override
    public List<Subject> findPaginated(int page, int size) {
        log.debug("Obteniendo asignaturas paginadas - página: {}, tamaño: {}", page, size);

        if (page < 0) {
            throw new IllegalArgumentException("El número de página no puede ser negativo");
        }

        if (size <= 0 || size > 100) {
            throw new IllegalArgumentException("El tamaño debe estar entre 1 y 100");
        }

        // Usar paginación nativa del repositorio
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Subject> pageResult = readSubjectRepository.findAll(pageRequest);

        return pageResult.getContent();
    }

    @Override
    public List<SubjectGroup> findGroupsBySubjectId(Long subjectId) {
        log.debug("Buscando grupos de la asignatura con ID: {}", subjectId);

        if (!existsById(subjectId)) {
            throw new SubjectNotFoundException("Asignatura no encontrada con ID: " + subjectId);
        }

        return readSubjectGroupRepository.findBySubjectId(subjectId);
    }

    @Override
    public List<SubjectGroup> findGroupsBySubjectId(Long subjectId, GroupStatus status) {
        log.debug("Buscando grupos de la asignatura {} con estado: {}", subjectId, status);

        if (!existsById(subjectId)) {
            throw new SubjectNotFoundException("Asignatura no encontrada con ID: " + subjectId);
        }

        if (status == null) {
            return findGroupsBySubjectId(subjectId);
        }

        return readSubjectGroupRepository.findBySubjectIdAndStatus(subjectId, status);
    }
}