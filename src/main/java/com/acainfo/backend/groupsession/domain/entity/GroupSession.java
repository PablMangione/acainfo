package com.acainfo.backend.groupsession.domain.entity;

import com.acainfo.backend.groupsession.domain.value.Classroom;
import com.acainfo.backend.groupsession.domain.value.SessionType;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class GroupSession {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private DayOfWeek dayOfWeek;
    private Classroom classroom;
    private Long groupId;
    private SessionType type;
}
