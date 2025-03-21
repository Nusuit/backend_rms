package org.example.rms.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.example.rms.dto.authentication.*;
import org.example.rms.dto.response.ApiResponse;
import org.example.rms.exception.LoginException;
import org.example.rms.exception.SignupException;
import org.example.rms.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {
    private final AuthService authService ;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse httpResponse)
            throws LoginException {
        LoginResponse response = authService.login(loginRequest, httpResponse);

        return ApiResponse
                .builder()
                    .code(200)
                    .success(true)
                    .message("Login successful")
                    .payload(response)
                .build();
    }


    @PostMapping("/signup/candidate")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<?> candidateSignup(@Valid @RequestBody SignupRequest request) throws SignupException {
        SignupResponse response = authService.signupCandidate(request);
        return ApiResponse
                .builder()
                    .code(201)
                    .success(true)
                    .message("Signup successful")
                    .payload(response)
                .build();
    }

    @PostMapping("/signup/recruiter")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<?> recruiterSignup(@Valid @RequestBody SignupRequest request) throws SignupException {
        SignupResponse response = authService.signupRecruiter(request);
        return ApiResponse
                .builder()
                    .code(201)
                    .success(true)
                    .message("Signup successful")
                    .payload(response)
                .build();
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> refresh(@CookieValue(name = "refresh_token") String refreshToken) {
        System.out.println("Refresh token: " + refreshToken);
        RefreshResponse refreshResponse = authService.refreshToken(refreshToken);
        return ApiResponse
                .builder()
                    .code(200)
                    .success(true)
                    .message("Refresh token successful")
                    .payload(refreshResponse)
                .build();
    }
}
