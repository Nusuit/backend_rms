package org.example.rms.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.rms.dto.authentication.*;
import org.example.rms.dto.ApiResponse;
import org.example.rms.exception.LoginException;
import org.example.rms.exception.ResendOtpException;
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
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse httpResponse)
            throws LoginException {
        LoginResponse response = authService.login(loginRequest, httpResponse);

        return ApiResponse
                .<LoginResponse>builder()
                    .success(true)
                    .message("Login successful")
                    .payload(response)
                .build();
    }


    @PostMapping("/signup/candidate")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> candidateSignup(@Valid @RequestBody SignupRequest request) throws SignupException {
        SignupResponse response = authService.signupCandidate(request);
        return ApiResponse
                .builder()
                    .success(true)
                    .message("Registration successful! Please verify the email to complete the process")
                    .payload(response)
                .build();
    }

    @PostMapping("/signup/recruiter")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> recruiterSignup(@Valid @RequestBody SignupRequest request) throws SignupException {
        SignupResponse response = authService.signupRecruiter(request);
        return ApiResponse
                .builder()
                    .success(true)
                    .message("Registration successful! Please verify the email to complete the process")
                    .payload(response)
                .build();
    }

    @PostMapping("/signup/verify")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> verifySignup(@Valid @RequestBody VerifyRequest request) throws SignupException {
        authService.verify(request);
        return ApiResponse
                .builder()
                    .success(true)
                    .message("Verify successful")
                .build();
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> refresh(@CookieValue(name = "refresh_token") String refreshToken) {
        RefreshResponse refreshResponse = authService.refreshToken(refreshToken);
        return ApiResponse
                .builder()
                    .success(true)
                    .message("Refresh token successful")
                    .payload(refreshResponse)
                .build();
    }

    @PostMapping("/resend-otp")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> resendOtp(@Valid @RequestBody ResendOtpRequest request) throws ResendOtpException {
        authService.resendOtp(request);
        return ApiResponse
                .builder()
                    .success(true)
                    .message("Resend otp successful")
                .build();
    }

    @GetMapping("/oauth2/token")
    public ApiResponse<?> oauth2GetToken(@RequestParam String code, HttpServletResponse httpResponse) {
        LoginResponse response = authService.oauth2GetToken(code, httpResponse);

        return ApiResponse.successBuild(response);
    }
}
