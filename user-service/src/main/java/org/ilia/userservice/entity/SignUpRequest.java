package org.ilia.userservice.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SignUpRequest {

    private String email;
    private String username;
    private String password;
    private LocalDate birthDate;
}
