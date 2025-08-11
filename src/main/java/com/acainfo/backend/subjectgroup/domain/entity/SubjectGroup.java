package com.acainfo.backend.subjectgroup.domain.entity;

import com.acainfo.backend.subjectgroup.domain.value.GroupStatus;
import com.acainfo.backend.subjectgroup.domain.value.GroupType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class SubjectGroup {
    private long id;
    private String name;
    private long subjectId;
    private long teacherId;
    private GroupStatus status;
    private int maxCapacity;
    private GroupType type;
    private BigDecimal price;
}
