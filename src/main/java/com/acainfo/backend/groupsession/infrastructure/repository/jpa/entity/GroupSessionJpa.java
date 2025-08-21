package com.acainfo.backend.groupsession.infrastructure.repository.jpa.entity;

import com.acainfo.backend.groupsession.domain.value.Classroom;
import com.acainfo.backend.groupsession.domain.value.SessionType;
import com.acainfo.backend.subjectgroup.infrastructure.repository.jpa.entity.SubjectGroupJpa;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "group_sessions",
        indexes = {
                @Index(name = "idx_session_group", columnList = "group_id"),
                @Index(name = "idx_session_day", columnList = "day_of_week"),
                @Index(name = "idx_session_classroom", columnList = "classroom"),
                @Index(name = "idx_session_type", columnList = "type"),
                @Index(name = "idx_session_group_day", columnList = "group_id, day_of_week")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_session_group_day_time",
                        columnNames = {"group_id", "day_of_week", "start_time"}
                ),
                @UniqueConstraint(
                        name = "uk_classroom_day_time",
                        columnNames = {"classroom", "day_of_week", "start_time", "end_time"}
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "group")
public class GroupSessionJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_session_group"))
    private SubjectGroupJpa group;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 20)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "classroom", nullable = false, length = 30)
    private Classroom classroom;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    @Builder.Default
    private SessionType type = SessionType.IN_PERSON;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (type == null) {
            type = SessionType.IN_PERSON;
        }
    }
}