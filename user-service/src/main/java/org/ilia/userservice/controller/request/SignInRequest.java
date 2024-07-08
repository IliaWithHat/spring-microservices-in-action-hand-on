package org.ilia.userservice.controller.request;

import lombok.Data;

@Data
public class SignInRequest {

    private String email;
    private String password;
}
