package com.acainfo.backend.enrollment.domain.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class EnrollmentId implements Serializable {
    private long studentId;
    private long groupId;
}
