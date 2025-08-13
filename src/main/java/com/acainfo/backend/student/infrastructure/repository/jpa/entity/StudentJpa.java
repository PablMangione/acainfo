package com.acainfo.backend.student.infrastructure.repository.jpa.entity;

import com.acainfo.backend.globalenum.Major;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "students",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_email",
                        columnNames = {"email"}
                )
        },
        indexes = {
                @Index(name = "idx_student_email", columnList = "email"),
                @Index(name = "idx_student_major", columnList = "major"),
                @Index(name = "idx_student_is_active", columnList = "is_active"),
                @Index(name = "idx_student_name", columnList = "name"),
                @Index(name = "idx_student_last_name", columnList = "last_name"),
                @Index(name = "idx_student_major_active", columnList = "major, is_active")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "password")
public class StudentJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "major", nullable = false, length = 20)
    private Major major;

    @CreationTimestamp
    @Column(name = "registered_at", nullable = false, updatable = false)
    private LocalDateTime registeredAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        if (isActive == null) {
            isActive = true;
        }
    }
}