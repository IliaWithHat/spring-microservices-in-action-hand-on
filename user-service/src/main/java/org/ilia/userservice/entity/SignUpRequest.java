package org.ilia.userservice.entity;

import lombok.Data;

@Data
public class SignUpRequest {

    private String email;
    private String username;
    private String password;
}
