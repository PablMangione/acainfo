package com.acainfo.backend.subjectgroup.domain.entity;

import com.acainfo.backend.subjectgroup.domain.value.GroupStatus;
import com.acainfo.backend.subjectgroup.domain.value.GroupType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class SubjectGroup {
    private Long id;
    private String name;
    private Long subjectId;
    private Long teacherId;
    private GroupStatus status;
    private int maxCapacity;
    private GroupType type;
    private BigDecimal price;
    private int currentEnrollments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
