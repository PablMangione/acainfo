package com.acainfo.backend.teacher.infrastructure.repository.jpa.entity;

import com.acainfo.backend.subjectgroup.infrastructure.repository.jpa.entity.SubjectGroupJpa;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teachers",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_teacher_email",
                        columnNames = {"email"}
                )
        },
        indexes = {
                @Index(name = "idx_teacher_email", columnList = "email"),
                @Index(name = "idx_teacher_is_admin", columnList = "is_admin"),
                @Index(name = "idx_teacher_name", columnList = "name")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"password", "groups"})
public class TeacherJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "is_admin", nullable = false)
    @Builder.Default
    private Boolean isAdmin = false;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @CreationTimestamp
    @Column(name = "registered_at", nullable = false, updatable = false)
    private LocalDateTime registeredAt;

    @Builder.Default
    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    private Set<SubjectGroupJpa> groups = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        if (isAdmin == null) {
            isAdmin = false;
        }
    }
}