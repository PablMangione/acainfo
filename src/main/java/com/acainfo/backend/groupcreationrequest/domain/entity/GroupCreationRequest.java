package com.acainfo.backend.groupcreationrequest.domain.entity;


import com.acainfo.backend.groupcreationrequest.domain.value.RequestStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class GroupCreationRequest {
    private Long id;
    private LocalDateTime requestedAt;
    private LocalDateTime updatedAt;
    private Long studentId;
    private Long subjectId;
    private RequestStatus status;
}
