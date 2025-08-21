package com.acainfo.backend.groupcreationrequest.infrastructure.repository.jpa.entity;

import com.acainfo.backend.groupcreationrequest.domain.value.RequestStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entidad JPA para la persistencia de solicitudes de creación de grupo.
 * Mapea la tabla 'group_creation_requests' en la base de datos.
 */
@Entity
@Table(name = "group_creation_requests",
        indexes = {
                @Index(name = "idx_gcr_student", columnList = "student_id"),
                @Index(name = "idx_gcr_subject", columnList = "subject_id"),
                @Index(name = "idx_gcr_status", columnList = "status"),
                @Index(name = "idx_gcr_student_subject_status", columnList = "student_id,subject_id,status")
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class GroupCreationRequestJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @CreatedDate
    @Column(name = "requested_at", nullable = false, updatable = false)
    private LocalDateTime requestedAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "subject_id", nullable = false)
    private Long subjectId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private RequestStatus status;

    /**
     * Callback para establecer valores por defecto antes de persistir.
     */
    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = RequestStatus.PENDING;
        }
    }
}