package com.destructio.springbootkeycloakexample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class HelloController {

    @GetMapping("/hello")
    public String hello(Authentication authentication) {
        return "Hello " + authentication.getName() + "!";
    }

    @Secured("ROLE_admin")
    @GetMapping("/hello/admin")
    public String secure(Authentication authentication) {
        return authentication.getName() + " is admin!";
    }

}
