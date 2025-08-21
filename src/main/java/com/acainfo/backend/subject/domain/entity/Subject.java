package com.acainfo.backend.subject.domain.entity;

import com.acainfo.backend.subject.domain.value.CourseYear;
import com.acainfo.backend.globalenum.Major;
import com.acainfo.backend.subject.domain.value.Quarter;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Subject {
    private Long id;
    private String name;
    private Major major;
    private CourseYear courseYear;
    private Quarter quarter;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
}
