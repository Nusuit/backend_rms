package io.d4tzz.newrms.service.auth;


import io.d4tzz.newrms.dto.auth.*;
import io.d4tzz.newrms.entity.CandidateAuth;
import io.d4tzz.newrms.entity.Candidate;
import io.d4tzz.newrms.entity.OAuth2;
import io.d4tzz.newrms.entity.Role;
import io.d4tzz.newrms.entity.enums.OtpType;
import io.d4tzz.newrms.entity.enums.RoleName;
import io.d4tzz.newrms.entity.enums.UserStatus;
import io.d4tzz.newrms.exception.*;
import io.d4tzz.newrms.repository.CandidateAuthRepository;
import io.d4tzz.newrms.repository.CandidateRepository;
import io.d4tzz.newrms.repository.OAuth2Repository;
import io.d4tzz.newrms.repository.RoleRepository;
import io.d4tzz.newrms.service.EmailService;
import io.d4tzz.newrms.service.JwtService;
import io.d4tzz.newrms.utils.OtpGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CandidateAuthServiceImpl implements CandidateAuthService {
    private static final String FROM = "rms.jobbridge@gmail.com";
    private final JwtService jwtService;

    @Value("${rms.otp.duration}")
    private int OTP_EXPIRATION_TIME_SECONDS;

    @Value("${rms.otp.interval}")
    private int OTP_INTERVAL_SECONDS;

    @Value("${rms.otp.window}")
    private int OTP_WINDOW_SECONDS ;

    @Value("${rms.otp.max.attempt}")
    private int MAX_ATTEMPT_SECONDS;

    @Value("${rms.oauth2.code.duration}")
    private int OAUTH2_CODE_EXPIRATION_TIME_SECONDS;

    private final EmailService emailService;
    private final CandidateAuthRepository candidateAuthRepository;
    private final RoleRepository roleRepository;
    private final SpringTemplateEngine templateEngine;
    private final CandidateRepository candidateRepository;
    private final PasswordEncoder passwordEncoder;
    private final OAuth2Repository oAuth2Repository;


    @Override
    public CandidateSignupResponse signup(CandidateSignupRequest request) {
        if (candidateAuthRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        String otp = OtpGenerator.generateOtp();
        Role role = roleRepository.findByName(RoleName.APPLICANT); // Đã đổi từ CANDIDATE
        CandidateAuth authUser = createNewAuthUser(request, otp, role);
        candidateAuthRepository.save(authUser);

        sendOtpEmail(request.getEmail(), otp);

        return CandidateSignupResponse.builder()
                .email(request.getEmail())
                .role(role.getName().toString())
                .build();
    }

    @Override
    @Transactional
    public void verifyEmail(VerifyEmailRequest request) {
        CandidateAuth candidateAuth = getUserByEmail(request.getEmail());

        if (candidateAuth.getStatus() != UserStatus.INACTIVE) {
            throw new EmailAlreadyVerifiedException("Email already verified");
        }
        if (isOtpExpired(candidateAuth.getOtpCreatedAt())) {
            throw new OtpExpiredException("Otp expired");
        }
        if (!candidateAuth.getOtp().equals(request.getOtp())) {
            throw new OtpNotMatchedException("OTP does not match");
        }

        candidateAuth.setStatus(UserStatus.ACTIVE);

        Candidate candidate = new Candidate();
        candidate.setAuth(candidateAuth);
        candidateRepository.save(candidate);

        candidateAuthRepository.save(candidateAuth);
    }

    @Override
    public void resendOtp(ResendOtpRequest request) {
        CandidateAuth candidateAuth = getUserByEmail(request.getEmail());

        if (candidateAuth.getStatus() != UserStatus.INACTIVE) {
            throw new UserAlreadyVerifiedException("User already verified, cannot resend otp");
        }

        if (canResetOtpAttempt(candidateAuth.getLatestOtpWindow())) {
            candidateAuth.setOtpAttempt(0);
            candidateAuth.setLatestOtpWindow(LocalDateTime.now());
            candidateAuthRepository.save(candidateAuth);
        }

        if (candidateAuth.getOtpAttempt() >= MAX_ATTEMPT_SECONDS) {
            long waitingTimeInSeconds = calculateWaitTimeInSeconds(candidateAuth.getLatestOtpWindow(), OTP_WINDOW_SECONDS);
            throw new TooManyOtpRequestException("Too many OTP requests", waitingTimeInSeconds);
        }

        long waitingTimeInSeconds = calculateWaitTimeInSeconds(candidateAuth.getOtpCreatedAt(), OTP_INTERVAL_SECONDS);
        if (waitingTimeInSeconds > 0) {
            throw new TooManyOtpRequestException("Too many OTP requests", waitingTimeInSeconds);
        }

        String otp = OtpGenerator.generateOtp();
        candidateAuth.setOtp(otp);
        candidateAuth.setOtpCreatedAt(LocalDateTime.now());
        candidateAuth.setOtpAttempt(candidateAuth.getOtpAttempt() + 1);
        candidateAuthRepository.save(candidateAuth);

        sendOtpEmail(request.getEmail(), otp);
    }

    @Override
    public CandidateLoginResponse login(CandidateLoginRequest request) {
        CandidateAuth candidateAuth = getUserByEmail(request.getEmail());

        if (candidateAuth.getStatus() != UserStatus.ACTIVE) {
            throw new UserNotActiveException("User not active");
        }

        String rawPassword = request.getPassword();
        String encryptedPassword = candidateAuth.getPassword();
        if (!isPasswordMatched(rawPassword, encryptedPassword)) {
            throw new PasswordNotMatchedException("Password does not match");
        }

        String accessToken = jwtService.accessToken()
                .subject(String.valueOf(candidateAuth.getAuthId()))
                .email(candidateAuth.getEmail())
                .role(candidateAuth.getRole().getName().toString())
                .jwt();

        String refreshToken = jwtService.refreshToken()
                .subject(String.valueOf(candidateAuth.getAuthId()))
                .email(candidateAuth.getEmail())
                .role(candidateAuth.getRole().getName().toString())
                .jwt();

        candidateAuth.setRefreshToken(refreshToken);
        candidateAuthRepository.save(candidateAuth);

        return CandidateLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public ResetAccessTokenResponse resetAccessToken(String refreshToken) {
        Map<String, String> payload = jwtService.validateRefreshToken(refreshToken);

        String email = payload.get("email");
        CandidateAuth candidateAuth = getUserByEmail(email);

        if (!isRefreshTokenMatched(refreshToken, candidateAuth.getRefreshToken())) {
            throw new InvalidJsonWebTokenException("Refresh token is invalid");
        }

        String newAccessToken = jwtService.accessToken()
                .subject(String.valueOf(candidateAuth.getAuthId()))
                .email(candidateAuth.getEmail())
                .role(candidateAuth.getRole().getName().toString())
                .jwt();

        String newRefreshToken = jwtService.refreshToken()
                .subject(String.valueOf(candidateAuth.getAuthId()))
                .email(candidateAuth.getEmail())
                .role(candidateAuth.getRole().getName().toString())
                .jwt();

        candidateAuth.setRefreshToken(newRefreshToken);
        candidateAuthRepository.save(candidateAuth);

        return ResetAccessTokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public CandidateLoginResponse oAuth2Login(String code) {
        OAuth2 oAuth2 = oAuth2Repository.findByCode(code).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );

        if (Duration.between(oAuth2.getCodeCreatedAt(), LocalDateTime.now()).toSeconds() > OAUTH2_CODE_EXPIRATION_TIME_SECONDS) {
            throw new OAuth2CodeExpirationException("Code expired");
        }

        CandidateAuth candidateAuth = oAuth2.getCandidateAuth();

        String accessToken = jwtService.accessToken()
                .subject(String.valueOf(candidateAuth.getAuthId()))
                .email(candidateAuth.getEmail())
                .role(candidateAuth.getRole().getName().toString())
                .jwt();

        String refreshToken = jwtService.refreshToken()
                .subject(String.valueOf(candidateAuth.getAuthId()))
                .email(candidateAuth.getEmail())
                .role(candidateAuth.getRole().getName().toString())
                .jwt();

        candidateAuth.setRefreshToken(refreshToken);
        candidateAuthRepository.save(candidateAuth);

        return CandidateLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public void logout(Long userId) {
        CandidateAuth candidateAuth = candidateAuthRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );

        candidateAuth.setRefreshToken(null);

        candidateAuthRepository.save(candidateAuth);
    }

    private CandidateAuth createNewAuthUser(CandidateSignupRequest request, String otp, Role role) {
        CandidateAuth authUser = new CandidateAuth();
        authUser.setEmail(request.getEmail());
        authUser.setPassword(encryptPassword(request.getPassword()));
        authUser.setOtpType(OtpType.VERIFY_EMAIL);
        authUser.setOtp(otp);
        authUser.setOtpCreatedAt(LocalDateTime.now());
        authUser.setLatestOtpWindow(LocalDateTime.now());
        authUser.setRole(role);
        authUser.setStatus(UserStatus.INACTIVE);
        return authUser;
    }

    private boolean isOtpExpired(LocalDateTime otpCreatedAt) {
        return Duration.between(otpCreatedAt, LocalDateTime.now()).toSeconds() > OTP_EXPIRATION_TIME_SECONDS;
    }

    private boolean canResetOtpAttempt(LocalDateTime latestOtpInterval) {
        return Duration.between(latestOtpInterval, LocalDateTime.now()).toSeconds() > OTP_WINDOW_SECONDS;
    }

    private long calculateWaitTimeInSeconds(LocalDateTime referenceTime, int durationInSeconds) {
        long elapsed = Duration.between(referenceTime, LocalDateTime.now()).toSeconds();
        return elapsed < durationInSeconds ? durationInSeconds - elapsed : 0;
    }

    private CandidateAuth getUserByEmail(String email) {
        return candidateAuthRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotExistsException("Email does not exist"));
    }

    private String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private boolean isPasswordMatched(String rawPassword, String encryptedPassword) {
        return passwordEncoder.matches(rawPassword, encryptedPassword);
    }

    private void sendOtpEmail(String to, String otp) {
        Context context = new Context();
        context.setVariable("otp", otp);
        context.setVariable("to", to);
        String content = templateEngine.process("email_verification", context);
        emailService.sendEmail(FROM, to, "OTP code: " + otp, content);
    }

    private boolean isRefreshTokenMatched(String rawRefreshToken, String storedRefreshToken) {
        return rawRefreshToken.equals(storedRefreshToken);
    }
}
