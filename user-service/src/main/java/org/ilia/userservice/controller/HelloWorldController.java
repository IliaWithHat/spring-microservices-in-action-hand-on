package org.ilia.userservice.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello-world")
public class HelloWorldController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String helloWorld() {
        return "hello-world";
    }
}
