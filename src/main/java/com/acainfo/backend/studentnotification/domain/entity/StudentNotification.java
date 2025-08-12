package com.acainfo.backend.studentnotification.domain.entity;

import com.acainfo.backend.studentnotification.domain.value.StudentNotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class StudentNotification {
    private Long id;
    private Long studentId;
    private LocalDateTime notifiedAt;
    private StudentNotificationType type;
    String message;
}
