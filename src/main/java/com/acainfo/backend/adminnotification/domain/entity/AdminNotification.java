package com.acainfo.backend.adminnotification.domain.entity;

import com.acainfo.backend.adminnotification.domain.value.AdminNotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class AdminNotification {
    private long id;
    private long teacherId;
    private LocalDateTime notifiedAt;
    private AdminNotificationType type;
}
