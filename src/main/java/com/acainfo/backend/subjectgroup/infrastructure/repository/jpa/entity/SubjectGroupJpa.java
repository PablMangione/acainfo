package com.acainfo.backend.subjectgroup.infrastructure.repository.jpa.entity;

import com.acainfo.backend.enrollment.infrastructure.repository.jpa.entity.EnrollmentJpa;
import com.acainfo.backend.groupsession.infrastructure.repository.jpa.entity.GroupSessionJpa;
import com.acainfo.backend.subjectgroup.domain.value.GroupStatus;
import com.acainfo.backend.subjectgroup.domain.value.GroupType;
import com.acainfo.backend.subject.infrastructure.repository.jpa.entity.SubjectJpa;
import com.acainfo.backend.teacher.infrastructure.repository.jpa.entity.TeacherJpa;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
@ToString(exclude = {"subject", "teacher", "enrollments","sessions"})
public class SubjectGroupJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false
    , foreignKey = @ForeignKey(name = "fk_group_subject"))
    private SubjectJpa subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false,
            foreignKey = @ForeignKey(name="fk_group_teacher"))
    private TeacherJpa teacher;

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

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<EnrollmentJpa> enrollments = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private Set<GroupSessionJpa> sessions = new HashSet<>();

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