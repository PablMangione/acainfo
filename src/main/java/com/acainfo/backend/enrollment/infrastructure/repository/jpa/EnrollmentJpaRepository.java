package com.acainfo.backend.enrollment.infrastructure.repository.jpa;

import com.acainfo.backend.enrollment.domain.value.EnrollmentStatus;
import com.acainfo.backend.enrollment.infrastructure.repository.jpa.entity.EnrollmentJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentJpaRepository extends JpaRepository<EnrollmentJpa, EnrollmentJpa.EnrollmentId> {

    // Métodos de búsqueda por componentes individuales del ID
    List<EnrollmentJpa> findByStudent_IdOrderByEnrolledAtDesc(Long studentId);

    List<EnrollmentJpa> findByGroup_IdOrderByEnrolledAtDesc(Long groupId);

    List<EnrollmentJpa> findByStatusOrderByEnrolledAtDesc(EnrollmentStatus status);

    List<EnrollmentJpa> findByStudent_IdAndStatusOrderByEnrolledAtDesc(
            Long studentId,
            EnrollmentStatus status
    );

    List<EnrollmentJpa> findByGroup_IdAndStatusOrderByEnrolledAtDesc(
            Long groupId,
            EnrollmentStatus status
    );

    long countByGroup_IdAndStatus(Long groupId, EnrollmentStatus status);

    boolean existsByStudent_IdAndGroup_Id(Long studentId, Long groupId);

    // Método para eliminar por componentes del ID
    void deleteByStudent_IdAndGroup_Id(Long studentId, Long groupId);

    int deleteByStudent_Id(Long studentId);

    int deleteByGroup_Id(Long groupId);

    // Queries personalizadas
    @Query("SELECT e FROM EnrollmentJpa e WHERE e.student.id = :studentId " +
            "AND e.status = 'ACTIVE' ORDER BY e.enrolledAt DESC")
    List<EnrollmentJpa> findActiveEnrollmentsByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT e FROM EnrollmentJpa e WHERE e.status = 'PENDING_PAYMENT' " +
            "ORDER BY e.enrolledAt ASC")
    List<EnrollmentJpa> findPendingPaymentEnrollments();

    @Query("SELECT e FROM EnrollmentJpa e " +
            "JOIN e.group g " +
            "WHERE e.student.id = :studentId " +
            "AND g.subject.id = :subjectId " +
            "AND e.status IN ('ACTIVE', 'PENDING_PAYMENT')")
    List<EnrollmentJpa> findByStudentIdAndSubjectId(
            @Param("studentId") Long studentId,
            @Param("subjectId") Long subjectId
    );

    @Query("SELECT COUNT(e) FROM EnrollmentJpa e " +
            "WHERE e.group.id = :groupId " +
            "AND e.status = 'ACTIVE'")
    long countActiveEnrollmentsByGroupId(@Param("groupId") Long groupId);
}