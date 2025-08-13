package com.acainfo.backend.subjectgroup.domain.entity;

import com.acainfo.backend.subjectgroup.domain.value.GroupStatus;
import com.acainfo.backend.subjectgroup.domain.value.GroupType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
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
    private Integer maxCapacity;
    private GroupType type;
    private BigDecimal price;
    private Integer currentEnrollments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
