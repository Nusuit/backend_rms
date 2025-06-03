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

    private static final String SUPER_RECRUITER_USERNAME = "hacnguyet108@gmail.com";

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

        // Kiểm tra tài khoản đặc biệt để đặt cờ isSuperRecruiter
        boolean isSuperRecruiter = request.getUsername().equals(SUPER_RECRUITER_USERNAME);

        return RecruiterLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .isSuperRecruiter(isSuperRecruiter) // Đặt giá trị cho trường mới
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

    @Override
    @Transactional
    public RecruiterSignupResponse createRecruiter(RecruiterSignupRequest request) {
        // Kiểm tra xem username đã tồn tại chưa
        Optional<RecruiterAuth> existingRecruiter = recruiterAuthRepository.findByUsername(request.getUsername());
        if (existingRecruiter.isPresent()) {
            throw new InvalidRequestException("Username already exists");
        }

        // Lấy vai trò RECRUITER
        Role recruiterRole = roleRepository.findByName(RoleName.RECRUITER);
        if (recruiterRole == null) {
            throw new ResourceNotFoundException("Recruiter role not found");
        }

        // Tạo RecruiterAuth
        RecruiterAuth recruiterAuth = new RecruiterAuth();
        recruiterAuth.setUsername(request.getUsername());
        recruiterAuth.setPassword(passwordEncoder.encode(request.getPassword()));
        recruiterAuth.setRole(recruiterRole);
        recruiterAuth.setStatus(UserStatus.ACTIVE);
        recruiterAuth = recruiterAuthRepository.save(recruiterAuth);

        // Tạo Recruiter profile (liên kết với RecruiterAuth)
        // Đảm bảo rằng Recruiter entity được tạo và lưu trữ đúng cách
        Recruiter recruiter = new Recruiter();
        recruiter.setId(recruiterAuth.getId()); // ID của Recruiter là ID của RecruiterAuth
        recruiter.setAuth(recruiterAuth); // Thiết lập mối quan hệ @OneToOne
        recruiter.setName(request.getFirstName() + " " + request.getLastName());
        recruiter.setDescription("Recruiter account created by admin.");
        recruiterRepository.save(recruiter); // Lưu Recruiter entity

        return RecruiterSignupResponse.builder()
                .id(recruiterAuth.getId())
                .username(recruiterAuth.getUsername())
                .role(recruiterAuth.getRole().getName().toString())
                .name(recruiter.getName())
                .build();
    }


    private boolean isPasswordMatched(String rawPassword, String encryptedPassword) {
        return passwordEncoder.matches(rawPassword, encryptedPassword);
    }
}
