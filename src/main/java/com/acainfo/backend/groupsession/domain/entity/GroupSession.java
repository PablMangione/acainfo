package com.acainfo.backend.groupsession.domain.entity;

import com.acainfo.backend.groupsession.domain.value.Classroom;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class GroupSession {
    private long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private DayOfWeek dayOfWeek;
    private Classroom classroom;
    private long groupId;
}
