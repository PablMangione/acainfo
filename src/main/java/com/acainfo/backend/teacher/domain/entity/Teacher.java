package com.acainfo.backend.teacher.domain.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Teacher {
    private long id;
    private String name;
    private boolean isAdmin;
    private String email;
    private String password;
    private String phoneNumber;
    private LocalDateTime registeredAt;
}
