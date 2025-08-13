package com.acainfo.backend.subjectgroup.infrastructure.repository.jpa.entity;

import com.acainfo.backend.subjectgroup.domain.value.GroupStatus;
import com.acainfo.backend.subjectgroup.domain.value.GroupType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "subject_groups",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_group_name",
                        columnNames = {"name"}
                )
        },
        indexes = {
                @Index(name = "idx_group_subject", columnList = "subject_id"),
                @Index(name = "idx_group_teacher", columnList = "teacher_id"),
                @Index(name = "idx_group_status", columnList = "status"),
                @Index(name = "idx_group_type", columnList = "type"),
                @Index(name = "idx_group_subject_status", columnList = "subject_id, status"),
                @Index(name = "idx_group_teacher_status", columnList = "teacher_id, status")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class SubjectGroupJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "subject_id", nullable = false)
    private Long subjectId;

    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private GroupStatus status = GroupStatus.PLANNED;

    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private GroupType type;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "current_enrollments", nullable = false)
    @Builder.Default
    private Integer currentEnrollments = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = GroupStatus.PLANNED;
        }
        if (currentEnrollments == null) {
            currentEnrollments = 0;
        }
    }
}