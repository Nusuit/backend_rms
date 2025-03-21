package org.example.rms.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.example.rms.dto.authentication.*;
import org.example.rms.entity.Role;
import org.example.rms.entity.User;
import org.example.rms.exception.LoginException;
import org.example.rms.exception.SignupException;
import org.example.rms.repo.RoleRepository;
import org.example.rms.repo.UserRepository;
import org.example.rms.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.util.Optional;

@Component
@AllArgsConstructor
public class AuthServiceImp implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public LoginResponse login(LoginRequest request, HttpServletResponse httpResponse) throws LoginException {
        User user = checkExistEmail(request.getEmail());
        if (user == null) {
            throw new LoginException("User not found");
        }


        String requestedPassword = request.getPassword();
        String storedPassword = user.getPassword();

        if(!checkPassword(requestedPassword, storedPassword)) {
            throw new LoginException("Password does not match");
        }

        String accessToken = JwtUtils.generateAccessToken(user);
        String refreshToken = JwtUtils.generateRefreshToken(user);

        createAndSetRefreshTokenCookie(httpResponse, refreshToken);

        updateUserRefreshToken(user, refreshToken);


        return new LoginResponse(accessToken, refreshToken, "Bearer");
    }


    public SignupResponse signup(SignupRequest request, Role role) throws SignupException {
        SignupException exception = new SignupException();

        User user = checkExistEmail(request.getEmail());
        if (user != null) {
            exception.addError("email", "Email already in use");
        }

        if(exception.hasErrors()) {
            throw exception;
        }

        String encryptedPassword = encryptPassword(request.getPassword());

        user = createUserVerification(new User(request.getEmail(), encryptedPassword, role));

        // Xac thuc email trong tuong lai

        return new SignupResponse(user.getUserId(), user.getEmail(), user.getRole().getRoleName());
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
    public RefreshResponse refreshToken(String refreshToken) throws JwtException {
        Jwt<?, ?> jwt = JwtUtils.validateRefreshToken(refreshToken);
        Claims claims = (Claims) jwt.getPayload();
        String userId = claims.getSubject();

        Optional<User> optionalUser = findUserById(Long.valueOf(userId));
        User user = optionalUser.orElseThrow(() -> new JwtException("User not found"));
        if(!checkRefreshToken(refreshToken, user.getRefreshToken())) {
            throw new JwtException("Refresh token not matched");
        }

        refreshToken = JwtUtils.generateRefreshToken(user);
        String accessToken = JwtUtils.generateAccessToken(user);

        updateUserRefreshToken(user, refreshToken);

        return new RefreshResponse(refreshToken, accessToken, "Bearer");
    }

    private Optional<User> findUserByEmail(String username) {
        return userRepository.findByEmail(username);
    }

    private User checkExistEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    private Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    private boolean checkPassword(String requestedPassword, String storedPassword) {
        return passwordEncoder.matches(requestedPassword, storedPassword);
    }

    private User createUserVerification(User user) {
        return userRepository.save(user);
    }



    private String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private void createAndSetRefreshTokenCookie(HttpServletResponse httpResponse, String refreshToken) {
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setPath("/api/auth/refresh");
        cookie.setHttpOnly(true);
        httpResponse.addCookie(cookie);
    }

    private void updateUserRefreshToken(User user, String refreshToken) {
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }

    private boolean checkRefreshToken(String requestedToken, String storedToken) {
        return storedToken.equals(requestedToken);
    }


}
