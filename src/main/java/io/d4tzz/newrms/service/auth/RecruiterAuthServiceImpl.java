// Path: src/main/java/io/d4tzz/newrms/service/auth/RecruiterAuthServiceImpl.java
package io.d4tzz.newrms.service.auth;

import io.d4tzz.newrms.dto.auth.RecruiterLoginRequest;
import io.d4tzz.newrms.dto.auth.RecruiterLoginResponse;
// Import cho RecruiterSignupRequest và RecruiterSignupResponse đã được xóa
import io.d4tzz.newrms.dto.auth.ResetAccessTokenResponse;
import io.d4tzz.newrms.entity.RecruiterAuth;
import io.d4tzz.newrms.entity.enums.UserStatus;
import io.d4tzz.newrms.exception.PasswordNotMatchedException;
import io.d4tzz.newrms.exception.UserNotActiveException;
import io.d4tzz.newrms.exception.UserNotFoundException;
import io.d4tzz.newrms.repository.RecruiterAuthRepository;
// Các import không còn cần thiết cho signup đã được xóa (ví dụ: RoleRepository, RecruiterRepository nếu chỉ dùng cho signup)
// Tuy nhiên, nếu chúng được sử dụng ở các phần khác (ví dụ: Admin tạo Recruiter), thì cần giữ lại.
// import io.d4tzz.newrms.repository.RecruiterRepository;
// import io.d4tzz.newrms.repository.RoleRepository;
import io.d4tzz.newrms.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional; // Xóa nếu không còn phương thức nào cần @Transactional

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecruiterAuthServiceImpl implements RecruiterAuthService {
    private final RecruiterAuthRepository recruiterAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    // private final RoleRepository roleRepository; // Giữ lại nếu Admin cần tạo Recruiter
    // private final RecruiterRepository recruiterRepository; // Giữ lại nếu Admin cần tạo Recruiter

    @Override
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

    @Override
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

        recruiterAuth.setRefreshToken(newRefreshToken);
        recruiterAuthRepository.save(recruiterAuth);

        return ResetAccessTokenResponse.builder()
                .accessToken(newAccessToken).refreshToken(newRefreshToken).tokenType("Bearer")
                .build();
    }

    // Toàn bộ phương thức signup(RecruiterSignupRequest request) đã được XÓA khỏi đây.
    // Logic tạo Recruiter mới sẽ được xử lý bởi Admin, ví dụ:
    // - Admin thêm trực tiếp vào database.
    // - Admin sử dụng một giao diện quản trị (nếu có) để gọi một service khác (không phải RecruiterAuthService).

    private boolean isPasswordMatched(String rawPassword, String encryptedPassword) {
        return passwordEncoder.matches(rawPassword, encryptedPassword);
    }
}
