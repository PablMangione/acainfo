package com.acainfo.backend.enrollment.domain.entity;

import com.acainfo.backend.enrollment.domain.value.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Enrollment {
    private EnrollmentId id;
    private LocalDateTime enrolledAt;
    private EnrollmentStatus status;
}
