package com.acainfo.backend.enrollment.infrastructure.repository.jpa.entity;

import com.acainfo.backend.enrollment.domain.value.EnrollmentStatus;
import com.acainfo.backend.student.infrastructure.repository.jpa.entity.StudentJpa;

import com.acainfo.backend.subjectgroup.infrastructure.repository.jpa.entity.SubjectGroupJpa;
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
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"student", "group"})
@ToString(exclude = {"student", "group"})
public class EnrollmentJpa {
    @EmbeddedId
    private EnrollmentId id;

    @MapsId("studentId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false
    , foreignKey = @ForeignKey(name = "fk_enrollment_student"))
    private StudentJpa student;

    @MapsId("groupId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false
    , foreignKey = @ForeignKey(name = "fk_enrollment_group"))
    private SubjectGroupJpa group;

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
    @Embeddable
    @Getter @Setter @EqualsAndHashCode
    public static class EnrollmentId implements Serializable {
        @Column(name = "student_id")
        private Long studentId;

        @Column(name = "group_id")
        private Long groupId;
    }
}