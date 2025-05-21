package io.d4tzz.newrms.service.auth;

import io.d4tzz.newrms.dto.auth.RecruiterLoginRequest;
import io.d4tzz.newrms.dto.auth.RecruiterLoginResponse;
import io.d4tzz.newrms.dto.auth.ResetAccessTokenResponse;
import io.d4tzz.newrms.entity.RecruiterAuth;
import io.d4tzz.newrms.entity.enums.UserStatus;
import io.d4tzz.newrms.exception.PasswordNotMatchedException;
import io.d4tzz.newrms.exception.UserNotActiveException;
import io.d4tzz.newrms.exception.UserNotFoundException;
import io.d4tzz.newrms.repository.RecruiterAuthRepository;
import io.d4tzz.newrms.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecruiterAuthServiceImpl implements RecruiterAuthService {
    private final RecruiterAuthRepository recruiterAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public RecruiterLoginResponse login(RecruiterLoginRequest request) {
        RecruiterAuth recruiterAuth = recruiterAuthRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new UserNotFoundException("Username not found")
        );

        if (recruiterAuth.getStatus() != UserStatus.ACTIVE) {
            throw new UserNotActiveException("User not active");
        }

        String rawPassword = request.getPassword();
        String encryptedPassword = recruiterAuth.getPassword();
        if (!isPasswordMatched(rawPassword, encryptedPassword)) {
            throw new PasswordNotMatchedException("Password does not match");
        }

        String accessToken = jwtService.accessToken()
                .subject(String.valueOf(recruiterAuth.getId()))
                .role(recruiterAuth.getRole().getName().toString())
                .jwt();

        String refreshToken = jwtService.refreshToken()
                .subject(String.valueOf(recruiterAuth.getId()))
                .role(recruiterAuth.getRole().getName().toString())
                .jwt();

        recruiterAuth.setRefreshToken(refreshToken);
        recruiterAuthRepository.save(recruiterAuth);


        return RecruiterLoginResponse.builder()
                .accessToken(accessToken).refreshToken(refreshToken).tokenType("Bearer")
                .build();
    }

    public ResetAccessTokenResponse resetAccessToken(String refreshToken) {
        Map<String, String> payload = jwtService.validateRefreshToken(refreshToken);

        Long id = Long.parseLong(payload.get("sub"));
        RecruiterAuth recruiterAuth = recruiterAuthRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );

        String newAccessToken = jwtService.accessToken()
                .subject(String.valueOf(recruiterAuth.getId()))
                .role(recruiterAuth.getRole().getName().toString())
                .jwt();

        String newRefreshToken = jwtService.refreshToken()
                .subject(String.valueOf(recruiterAuth.getId()))
                .role(recruiterAuth.getRole().getName().toString())
                .jwt();

        recruiterAuth.setRefreshToken(refreshToken);
        recruiterAuthRepository.save(recruiterAuth);

        return ResetAccessTokenResponse.builder()
                .accessToken(newAccessToken).refreshToken(newRefreshToken).tokenType("Bearer")
                .build();
    }

    private boolean isPasswordMatched(String rawPassword, String encryptedPassword) {
        return passwordEncoder.matches(rawPassword, encryptedPassword);
    }
}
