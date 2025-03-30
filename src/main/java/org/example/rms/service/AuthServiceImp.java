package org.example.rms.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.rms.dto.authentication.*;
import org.example.rms.entity.*;
import org.example.rms.exception.*;
import org.example.rms.repo.*;
import org.example.rms.utils.JwtUtils;
import org.example.rms.utils.OtpGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CandidateRepository candidateRepository;
    private final RecruiterRepository recruiterRepository;
    private final UnverifiedUserRepository unverifiedUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TransactionTemplate transactionTemplate;


    private static final String FROM = "rms.jobbridge@gmail.com";
    private static final String VERIFIED_SUBJECT = "Email verification for Job Bridge";

    @Value("${otp-expiration-time-in-second:300}")
    private int OTP_EXPIRATION_TIME_IN_SECOND;

    @Value("${duration-between-otp-request-in-second:60}")
    private int DURATION_BETWEEN_OTP_REQUEST_IN_SECOND ;

    @Value("${duration-between-otp-interval-in-second:3600}")
    private int DURATION_BETWEEN_OTP_INTERVAL_IN_SECOND;

    @Value("${the-maximum-number-of-otp-requests-between-interval:5}")
    private int THE_MAXIMUM_NUMBER_OF_OTP_REQUESTS_BETWEEN_INTERVAL;

    public static final String REFRESH_TOKEN = "refresh_token";

    @Override
    public LoginResponse login(LoginRequest request, HttpServletResponse httpResponse) throws LoginException {
        User user = checkExistUserEmail(request.getEmail());
        if (user == null) {
            throw new LoginException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        String requestedPassword = request.getPassword();
        String storedPassword = user.getPassword();

        if(!isPasswordMatched(requestedPassword, storedPassword)) {
            throw new LoginException(HttpStatus.UNAUTHORIZED, "Password not match");
        }

        String accessToken = JwtUtils.generateAccessToken(user);
        String refreshToken = JwtUtils.generateRefreshToken(user);

        createAndSetRefreshTokenCookie(httpResponse, refreshToken);

        updateUserRefreshToken(user, refreshToken);

        return new LoginResponse(accessToken, refreshToken, "Bearer");
    }

    public SignupResponse signup(SignupRequest request, Role role) throws SignupException{
        User user = checkExistUserEmail(request.getEmail());
        if (user != null) {
            throw new SignupException(HttpStatus.CONFLICT, "Email already exists");
        }

        String otp = OtpGenerator.generateOtp();

        UnverifiedUser unverifiedUser = UnverifiedUser
                .builder()
                    .email(request.getEmail())
                    .password(encryptPassword(request.getPassword()))
                    .otp(otp)
                    .role(role)
                .build();

        // Rollback transaction một khi gửi email lỗi
        transactionTemplate.executeWithoutResult(status -> {
            // Neu user voi email da ton tai trong db, loi se duoc nem ra
            try {
                unverifiedUserRepository.save(unverifiedUser);
            } catch (DataIntegrityViolationException e) {
                throw new SignupException(HttpStatus.BAD_REQUEST, "This email has already been registered for verification");
            }

            try {
                sendOtpThroughEmail(otp, unverifiedUser.getEmail());
            } catch (MessagingException e) {
                status.setRollbackOnly();
                throw new SignupException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot send otp");
            }
        });


        return SignupResponse
                .builder()
                    .email(request.getEmail())
                    .role(role.getRoleName())
                .build();
    }

    @Override
    public SignupResponse signupCandidate(SignupRequest request) throws SignupException {
        return signup(request, roleRepository.getCandidateRole());
    }

    @Override
    public SignupResponse signupRecruiter(SignupRequest request) throws SignupException {
        return signup(request, roleRepository.getRecruiterRole());
    }

    @Override
    public RefreshResponse refreshToken(String refreshToken) throws RefreshTokenException {
        Jwt<?, ?> jwt;
        try {
            jwt = JwtUtils.validateRefreshToken(refreshToken);
        } catch(JwtException e) {
            throw new RefreshTokenException(HttpStatus.BAD_REQUEST, "Invalid refresh token");
        }

        Claims claims = (Claims) jwt.getPayload();
        String userId = claims.getSubject();

        Optional<User> optionalUser = findUserById(Long.valueOf(userId));
        User user = optionalUser.orElseThrow(() -> new RefreshTokenException(HttpStatus.UNAUTHORIZED, "User in token not found"));

        if (!isRefreshTokenMatched(refreshToken, user.getRefreshToken())) {
            throw new RefreshTokenException(HttpStatus.BAD_REQUEST, "Refresh token not matched");
        }

        String newRefreshToken = JwtUtils.generateRefreshToken(user);
        String newAccessToken = JwtUtils.generateAccessToken(user);

        updateUserRefreshToken(user, refreshToken);

        return new RefreshResponse(newRefreshToken, newAccessToken, "Bearer");
    }

    @Override
    @Transactional
    public ResendOtpResponse resendOtp(ResendOtpRequest request) throws ResendOtpException {
        UnverifiedUser user = checkExistUnverifiedUserEmail(request.getEmail());
        if (user == null) {
            throw new ResendOtpException(HttpStatus.NOT_FOUND, "Email not found");
        }

        if (canResetOtpCount(user)) {
            user.setOtpRequestCount(0);
            user.setLatestOtpInterval(LocalDateTime.now());
            unverifiedUserRepository.save(user);
        }

        long waitingTimeInSeconds;
         if (user.getOtpRequestCount() == THE_MAXIMUM_NUMBER_OF_OTP_REQUESTS_BETWEEN_INTERVAL) {
             waitingTimeInSeconds = DURATION_BETWEEN_OTP_INTERVAL_IN_SECOND -
                                            Duration.between(user.getLatestOtpInterval(), LocalDateTime.now()).toSeconds();

             throw new ResendOtpException(HttpStatus.TOO_MANY_REQUESTS,
                     "Reach the maximum number of OTP requests in one hour, waiting " + waitingTimeInSeconds + " seconds",
                     waitingTimeInSeconds);
         } else {
             waitingTimeInSeconds = DURATION_BETWEEN_OTP_REQUEST_IN_SECOND - Duration.between(user.getOtpCreatedAt(), LocalDateTime.now()).toSeconds();
             if (waitingTimeInSeconds <= 0) {
                 String newOtp = OtpGenerator.generateOtp();
                 user.setOtp(newOtp);
                 user.setOtpRequestCount(user.getOtpRequestCount() + 1);
                 user.setOtpCreatedAt(LocalDateTime.now());
                 unverifiedUserRepository.save(user);

                 try {
                     sendOtpThroughEmail(newOtp, user.getEmail());
                 } catch (MessagingException e) {
                     throw new ResendOtpException(HttpStatus.INTERNAL_SERVER_ERROR, "Problem in sending otp through email");
                 }

             } else {
                 throw new ResendOtpException(HttpStatus.TOO_MANY_REQUESTS,
                         "waiting " + waitingTimeInSeconds + " seconds",
                         waitingTimeInSeconds);
             }
         }


        return new ResendOtpResponse();
    }

    @Override
    public VerifyResponse verify(VerifyRequest request) {
        UnverifiedUser unverifiedUser = checkExistUnverifiedUserEmail(request.getEmail());
        if (unverifiedUser == null) {
            throw new VerifyEmailException(HttpStatus.NOT_FOUND, "Email not found");
        }

        if (isOtpExpired(unverifiedUser)) {
            throw new VerifyEmailException(HttpStatus.BAD_REQUEST, "OTP expired");
        }

        if (!isOtpMatched(request.getOtp(), unverifiedUser.getOtp())) {
            throw new VerifyEmailException(HttpStatus.BAD_REQUEST, "OTP not matched");
        }

        User user = new User();
        user.setEmail(unverifiedUser.getEmail());
        user.setRole(unverifiedUser.getRole());
        user.setPassword(unverifiedUser.getPassword());
        user.setStatus(UserStatus.ACTIVE);

        if (isRecruiterUser(user)) {
            Recruiter recruiter = new Recruiter();
            recruiter.setUser(user);
            recruiterRepository.save(recruiter);
        } else {
            Candidate candidate = new Candidate();
            candidate.setUser(user);
            candidateRepository.save(candidate);
        }

        unverifiedUserRepository.delete(unverifiedUser);

        return new VerifyResponse();
    }

    private boolean isRecruiterUser(User user) {
        return user.getRole().getRoleId() == roleRepository.getRecruiterRole().getRoleId();
    }

    private boolean canResetOtpCount(UnverifiedUser user) {
        return Duration.between(user.getLatestOtpInterval(), LocalDateTime.now()).toSeconds() >= DURATION_BETWEEN_OTP_INTERVAL_IN_SECOND;
    }

    private boolean isOtpExpired(UnverifiedUser user) {
        return (Duration.between(user.getOtpCreatedAt(), LocalDateTime.now()).toSeconds()
                >= OTP_EXPIRATION_TIME_IN_SECOND);
    }

    private boolean isOtpMatched(String requestedOtp, String storedOtp) {
        return storedOtp.equals(requestedOtp);
    }

    private User checkExistUserEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    private UnverifiedUser checkExistUnverifiedUserEmail(String email) {
        return unverifiedUserRepository.findByEmail(email).orElse(null);
    }

    private Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    private boolean isPasswordMatched(String requestedPassword, String storedPassword) {
        return passwordEncoder.matches(requestedPassword, storedPassword);
    }

    private String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private void createAndSetRefreshTokenCookie(HttpServletResponse httpResponse, String refreshToken) {
        Cookie cookie = new Cookie(REFRESH_TOKEN, refreshToken);
        cookie.setPath("/api/auth/refresh");
        cookie.setHttpOnly(true);
        httpResponse.addCookie(cookie);
    }

    private void updateUserRefreshToken(User user, String refreshToken) {
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }

    private boolean isRefreshTokenMatched(String requestedToken, String storedToken) {
        return storedToken.equals(requestedToken);
    }

    private void sendOtpThroughEmail(String otp, String to) throws MessagingException {
        MimeMessage message = emailService.createMimeMessage(FROM, to, VERIFIED_SUBJECT, otp);
        emailService.sendEmail(message);
    }
}
