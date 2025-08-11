package com.acainfo.backend.groupcreationrequest.domain.entity;


import com.acainfo.backend.groupcreationrequest.domain.value.RequestStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class GroupCreationRequest {
    private long id;
    private LocalDateTime requestedAt;
    private long studentId;
    private long subjectId;
    private RequestStatus status;
}
