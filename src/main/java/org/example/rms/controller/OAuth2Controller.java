package org.example.rms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/google/")
public class OAuth2Controller {
    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@RequestParam("code") String code) {
        System.out.println("Get " + code + " from Google");
    }

}
