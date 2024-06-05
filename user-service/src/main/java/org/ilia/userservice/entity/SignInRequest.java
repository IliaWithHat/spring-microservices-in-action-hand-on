package org.ilia.userservice.entity;

import lombok.Data;

@Data
public class SignInRequest {

    private String email;
    private String password;
}
