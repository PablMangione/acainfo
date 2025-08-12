package com.acainfo.backend.teacher.domain.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Teacher {
    private Long id;
    private String name;
    private boolean isAdmin;
    private String email;
    private String password;
    private String phoneNumber;
    private LocalDateTime registeredAt;
}
