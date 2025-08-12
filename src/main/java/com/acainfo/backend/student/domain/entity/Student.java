package com.acainfo.backend.student.domain.entity;

import com.acainfo.backend.globalenum.Major;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Student {
    private long id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private Major major;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
    private boolean isActive;
}
