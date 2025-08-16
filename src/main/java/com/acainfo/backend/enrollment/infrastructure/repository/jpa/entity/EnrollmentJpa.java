package com.acainfo.backend.enrollment.infrastructure.repository.jpa.entity;

import com.acainfo.backend.enrollment.domain.value.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments",
        indexes = {
                @Index(name = "idx_enrollment_student", columnList = "student_id"),
                @Index(name = "idx_enrollment_group", columnList = "group_id"),
                @Index(name = "idx_enrollment_status", columnList = "status"),
                @Index(name = "idx_enrollment_student_status", columnList = "student_id, status"),
                @Index(name = "idx_enrollment_group_status", columnList = "group_id, status")
        }
)
@IdClass(EnrollmentJpa.EnrollmentIdJpa.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"studentId", "groupId"})
@ToString
public class EnrollmentJpa {

    @Id
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Id
    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @CreationTimestamp
    @Column(name = "enrolled_at", nullable = false, updatable = false)
    private LocalDateTime enrolledAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.PENDING_PAYMENT;

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = EnrollmentStatus.PENDING_PAYMENT;
        }
    }

    /**
     * Clase interna para la clave primaria compuesta.
     * Necesaria cuando se usa @IdClass.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class EnrollmentIdJpa implements Serializable {
        private Long studentId;
        private Long groupId;
    }
}