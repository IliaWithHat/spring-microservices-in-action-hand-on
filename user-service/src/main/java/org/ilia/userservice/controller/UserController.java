package org.ilia.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.ilia.userservice.entity.SignInRequest;
import org.ilia.userservice.entity.SignInResponse;
import org.ilia.userservice.entity.SignUpRequest;
import org.ilia.userservice.entity.SignUpResponse;
import org.ilia.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signUp(signUpRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<SignInResponse> login(@RequestBody SignInRequest signInRequest) {
        return ResponseEntity.ok().body(userService.login(signInRequest));
    }
}
