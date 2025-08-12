package com.acainfo.backend.enrollment.domain.entity;

import com.acainfo.backend.enrollment.domain.value.EnrollmentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Enrollment {
    private EnrollmentId id;
    private LocalDateTime enrolledAt;
    private LocalDateTime updatedAt;
    private EnrollmentStatus status;
}
