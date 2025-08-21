package com.acainfo.backend.subject.infrastructure.repository.jpa.entity;

import com.acainfo.backend.globalenum.Major;
import com.acainfo.backend.subject.domain.value.CourseYear;
import com.acainfo.backend.subject.domain.value.Quarter;
import com.acainfo.backend.subjectgroup.infrastructure.repository.jpa.entity.SubjectGroupJpa;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subjects",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_subject_name_major_year_quarter",
                        columnNames = {"name", "major", "course_year", "quarter"}
                )
        },
        indexes = {
                @Index(name = "idx_subject_major", columnList = "major"),
                @Index(name = "idx_subject_major_year", columnList = "major, course_year"),
                @Index(name = "idx_subject_active", columnList = "is_active")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "groups")
public class SubjectJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "major", nullable = false, length = 20)
    private Major major;

    @Enumerated(EnumType.STRING)
    @Column(name = "course_year", nullable = false, length = 20)
    private CourseYear courseYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "quarter", nullable = false, length = 20)
    private Quarter quarter;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Builder.Default
    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    private Set<SubjectGroupJpa> groups = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        if (isActive == null) {
            isActive = true;
        }
    }
}