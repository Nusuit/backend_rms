package io.d4tzz.newrms.security;

import io.d4tzz.newrms.entity.Candidate;
import io.d4tzz.newrms.entity.CandidateAuth;
import io.d4tzz.newrms.entity.OAuth2;
import io.d4tzz.newrms.entity.RecruiterAuth; // THÊM IMPORT NÀY
import io.d4tzz.newrms.entity.enums.RoleName;
import io.d4tzz.newrms.entity.enums.UserStatus;
import io.d4tzz.newrms.repository.CandidateAuthRepository;
import io.d4tzz.newrms.repository.CandidateRepository;
import io.d4tzz.newrms.repository.OAuth2Repository;
import io.d4tzz.newrms.repository.RecruiterAuthRepository; // THÊM IMPORT NÀY
import io.d4tzz.newrms.repository.RoleRepository;
import io.d4tzz.newrms.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final OAuth2Repository oAuth2Repository;
    private final RoleRepository roleRepository;
    private final CandidateAuthRepository candidateAuthRepository;
    private final CandidateRepository candidateRepository;
    private final RecruiterAuthRepository recruiterAuthRepository; // THÊM VÀO CONSTRUCTOR
    private final JwtService jwtService;
    private final SpringTemplateEngine templateEngine;

    @Value("${rms.frontend.url}")
    private String FRONTEND_URL;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication)
            throws IOException {
        if (authentication instanceof OAuth2AuthenticationToken oauth2Token) {
            String providerName = oauth2Token.getAuthorizedClientRegistrationId();
            OAuth2User oauth2User = oauth2Token.getPrincipal();
            String email = oauth2User.getAttribute("email");
            String providerUserId = oauth2User.getName();
            String fullName = oauth2User.getAttribute("name"); // Get full name from Google
            String givenName = oauth2User.getAttribute("given_name"); // Get first name
            String familyName = oauth2User.getAttribute("family_name"); // Get last name

            // Variables to store user information after role check
            String userRoleName;
            String userIdForJwt;
            String userEmailForJwt;
            String userStatusForJwt;
            String refreshTokenForJwt;

            // 1. Check if email belongs to an existing Recruiter
            Optional<RecruiterAuth> existingRecruiterAuth = recruiterAuthRepository.findByEmail(email);
            
            if (existingRecruiterAuth.isPresent()) {
                // Handle recruiter login
                RecruiterAuth recruiterAuth = existingRecruiterAuth.get();
                userRoleName = recruiterAuth.getRole().getName().toString();
                userIdForJwt = String.valueOf(recruiterAuth.getId());
                userEmailForJwt = recruiterAuth.getEmail();
                userStatusForJwt = recruiterAuth.getStatus().toString();

                refreshTokenForJwt = jwtService.refreshToken()
                        .subject(userIdForJwt)
                        .email(userEmailForJwt)
                        .role(userRoleName)
                        .jwt();
                recruiterAuth.setRefreshToken(refreshTokenForJwt);
                recruiterAuthRepository.save(recruiterAuth);
            } else {
                // 2. If not a recruiter, handle as candidate
                CandidateAuth candidateAuth;
                Optional<OAuth2> existingOAuth2 = oAuth2Repository.findByProviderUserIdAndProviderName(providerUserId, providerName);

                if (existingOAuth2.isPresent()) {
                    // Existing OAuth2 user
                    candidateAuth = existingOAuth2.get().getCandidateAuth();
                    
                    // Update candidate information if needed
                    Candidate candidate = candidateRepository.findById(candidateAuth.getAuthId())
                            .orElseThrow(() -> new IllegalStateException("Candidate not found for CandidateAuth"));
                    if (candidate.getName() == null || candidate.getName().isEmpty()) {
                        candidate.setName(fullName);
                        candidateRepository.save(candidate);
                    }
                } else {
                    // Create new candidate
                    candidateAuth = new CandidateAuth();
                    candidateAuth.setEmail(email);
                    candidateAuth.setStatus(UserStatus.ACTIVE);
                    candidateAuth.setRole(roleRepository.findByName(RoleName.APPLICANT));
                    candidateAuth = candidateAuthRepository.save(candidateAuth);

                    candidateAuth = candidateAuthRepository.findById(candidateAuth.getAuthId()).orElseThrow(
                            () -> new IllegalStateException("CandidateAuth not found after saving"));

                    Candidate candidate = new Candidate();
                    candidate.setAuth(candidateAuth);
                    candidate.setName(fullName);
                    candidateRepository.save(candidate);

                    OAuth2 oAuth2 = new OAuth2();
                    oAuth2.setProviderUserId(providerUserId);
                    oAuth2.setProviderName(providerName);
                    oAuth2.setCandidateAuth(candidateAuth);
                    oAuth2Repository.save(oAuth2);
                }

                userRoleName = candidateAuth.getRole().getName().toString();
                userIdForJwt = String.valueOf(candidateAuth.getAuthId());
                userEmailForJwt = candidateAuth.getEmail();
                userStatusForJwt = candidateAuth.getStatus().toString();

                refreshTokenForJwt = jwtService.refreshToken()
                        .subject(userIdForJwt)
                        .email(userEmailForJwt)
                        .role(userRoleName)
                        .jwt();
                candidateAuth.setRefreshToken(refreshTokenForJwt);
                candidateAuthRepository.save(candidateAuth);
            }

            // Create JWT Access Token
            String accessToken = jwtService.accessToken()
                    .subject(userIdForJwt)
                    .email(userEmailForJwt)
                    .role(userRoleName)
                    .jwt();

            // Prepare context for Thymeleaf template
            Context context = new Context();
            context.setVariable("accessToken", accessToken);
            context.setVariable("refreshToken", refreshTokenForJwt);
            context.setVariable("userRole", userRoleName);
            context.setVariable("frontendUrl", FRONTEND_URL);

            // Render template and send to client
            String htmlContent = templateEngine.process("oauth2_callback", context);

            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter writer = resp.getWriter();
            writer.write(htmlContent);
            writer.flush();
        }
    }
}