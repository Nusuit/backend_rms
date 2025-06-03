package io.d4tzz.newrms.service.auth;

import io.d4tzz.newrms.dto.auth.RecruiterLoginRequest;
import io.d4tzz.newrms.dto.auth.RecruiterLoginResponse;
import io.d4tzz.newrms.dto.auth.RecruiterSignupRequest;
import io.d4tzz.newrms.dto.auth.RecruiterSignupResponse;
import io.d4tzz.newrms.dto.auth.ResetAccessTokenResponse;
import io.d4tzz.newrms.entity.Recruiter;
import io.d4tzz.newrms.entity.RecruiterAuth;
import io.d4tzz.newrms.entity.Role;
import io.d4tzz.newrms.entity.enums.RoleName;
import io.d4tzz.newrms.entity.enums.UserStatus;
import io.d4tzz.newrms.exception.InvalidRequestException;
import io.d4tzz.newrms.exception.PasswordNotMatchedException;
import io.d4tzz.newrms.exception.ResourceNotFoundException;
import io.d4tzz.newrms.exception.UserNotActiveException;
import io.d4tzz.newrms.exception.UserNotFoundException;
import io.d4tzz.newrms.repository.RecruiterAuthRepository;
import io.d4tzz.newrms.repository.RecruiterRepository;
import io.d4tzz.newrms.repository.RoleRepository;
import io.d4tzz.newrms.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecruiterAuthServiceImpl implements RecruiterAuthService {
    private final RecruiterAuthRepository recruiterAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final RecruiterRepository recruiterRepository;

    private static final String SUPER_RECRUITER_EMAIL = "hacnguyet108@gmail.com"; // Đã đổi từ USERNAME thành EMAIL

    @Override
    public RecruiterLoginResponse login(RecruiterLoginRequest request) {
        RecruiterAuth recruiterAuth = recruiterAuthRepository.findByEmail(request.getEmail()).orElseThrow( // Đã đổi findByUsername thành findByEmail
                () -> new UserNotFoundException("Email not found") // Đã đổi Username thành Email
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
                .email(recruiterAuth.getEmail()) // Thêm email vào JWT
                .role(recruiterAuth.getRole().getName().toString())
                .jwt();

        String refreshToken = jwtService.refreshToken()
                .subject(String.valueOf(recruiterAuth.getId()))
                .email(recruiterAuth.getEmail()) // Thêm email vào JWT
                .role(recruiterAuth.getRole().getName().toString())
                .jwt();

        recruiterAuth.setRefreshToken(refreshToken);
        recruiterAuthRepository.save(recruiterAuth);

        // Kiểm tra tài khoản đặc biệt để đặt cờ isSuperRecruiter
        boolean isSuperRecruiter = request.getEmail().equals(SUPER_RECRUITER_EMAIL); // Đã đổi username thành email

        return RecruiterLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .isSuperRecruiter(isSuperRecruiter)
                .build();
    }

    @Override
    public ResetAccessTokenResponse resetAccessToken(String refreshToken) {
        Map<String, String> payload = jwtService.validateRefreshToken(refreshToken);

        Long id = Long.parseLong(payload.get("sub"));
        String email = payload.get("email"); // Lấy email từ payload

        RecruiterAuth recruiterAuth = recruiterAuthRepository.findByEmail(email).orElseThrow( // Tìm theo email
                () -> new UserNotFoundException("User not found")
        );

        String newAccessToken = jwtService.accessToken()
                .subject(String.valueOf(recruiterAuth.getId()))
                .email(recruiterAuth.getEmail())
                .role(recruiterAuth.getRole().getName().toString())
                .jwt();

        String newRefreshToken = jwtService.refreshToken()
                .subject(String.valueOf(recruiterAuth.getId()))
                .email(recruiterAuth.getEmail())
                .role(recruiterAuth.getRole().getName().toString())
                .jwt();

        recruiterAuth.setRefreshToken(newRefreshToken);
        recruiterAuthRepository.save(recruiterAuth);

        return ResetAccessTokenResponse.builder()
                .accessToken(newAccessToken).refreshToken(newRefreshToken).tokenType("Bearer")
                .build();
    }

    @Override
    @Transactional
    public RecruiterSignupResponse createRecruiter(RecruiterSignupRequest request) {
        // Kiểm tra xem email đã tồn tại chưa
        Optional<RecruiterAuth> existingRecruiter = recruiterAuthRepository.findByEmail(request.getEmail()); // Đã đổi findByUsername thành findByEmail
        if (existingRecruiter.isPresent()) {
            throw new InvalidRequestException("Email already exists"); // Đã đổi Username thành Email
        }

        // Lấy vai trò RECRUITER
        Role recruiterRole = roleRepository.findByName(RoleName.RECRUITER);
        if (recruiterRole == null) {
            throw new ResourceNotFoundException("Recruiter role not found");
        }

        // Tạo RecruiterAuth
        RecruiterAuth recruiterAuth = new RecruiterAuth();
        recruiterAuth.setEmail(request.getEmail()); // Đã đổi setUsername thành setEmail
        recruiterAuth.setPassword(passwordEncoder.encode(request.getPassword()));
        recruiterAuth.setRole(recruiterRole);
        recruiterAuth.setStatus(UserStatus.ACTIVE);
        recruiterAuth = recruiterAuthRepository.save(recruiterAuth);

        // Tạo Recruiter profile (liên kết với RecruiterAuth)
        Recruiter recruiter = new Recruiter();
        recruiter.setId(recruiterAuth.getId());
        recruiter.setAuth(recruiterAuth);
        recruiter.setName(request.getFirstName() + " " + request.getLastName());
        recruiter.setDescription("Recruiter account created by admin.");
        recruiterRepository.save(recruiter);

        return RecruiterSignupResponse.builder()
                .id(recruiterAuth.getId())
                .email(recruiterAuth.getEmail()) // Đã đổi username thành email
                .role(recruiterAuth.getRole().getName().toString())
                .name(recruiter.getName())
                .build();
    }


    private boolean isPasswordMatched(String rawPassword, String encryptedPassword) {
        return passwordEncoder.matches(rawPassword, encryptedPassword);
    }
}
