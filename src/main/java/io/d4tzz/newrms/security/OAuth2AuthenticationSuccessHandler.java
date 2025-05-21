package io.d4tzz.newrms.security;

import io.d4tzz.newrms.entity.Candidate;
import io.d4tzz.newrms.entity.CandidateAuth;
import io.d4tzz.newrms.entity.OAuth2;
import io.d4tzz.newrms.entity.enums.RoleName;
import io.d4tzz.newrms.entity.enums.UserStatus;
import io.d4tzz.newrms.repository.CandidateAuthRepository;
import io.d4tzz.newrms.repository.CandidateRepository;
import io.d4tzz.newrms.repository.OAuth2Repository;
import io.d4tzz.newrms.repository.RoleRepository;
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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final OAuth2Repository oAuth2Repository;
    private final RoleRepository roleRepository;
    private final CandidateAuthRepository candidateAuthRepository;
    private final CandidateRepository candidateRepository;

    @Value("${rms.frontend.url}")
    private String FRONTEND_URL;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication)
            throws java.io.IOException {
        if (authentication instanceof OAuth2AuthenticationToken oauth2Token) {
            String providerName = oauth2Token.getAuthorizedClientRegistrationId();

            OAuth2User oauth2User = oauth2Token.getPrincipal();
            String providerUserId = oauth2User.getName();
            String email = oauth2User.getAttribute("email");

            OAuth2 oAuth2 = null;
            Optional<OAuth2> optionalOAuth2 = oAuth2Repository.findByProviderUserIdAndProviderName(providerUserId, providerName);
            if (optionalOAuth2.isPresent()) {
                oAuth2 = optionalOAuth2.get();
            } else {
                CandidateAuth candidateAuth = new CandidateAuth();
                candidateAuth.setEmail(email);
                candidateAuth.setStatus(UserStatus.ACTIVE);
                candidateAuth.setRole(roleRepository.findByName(RoleName.CANDIDATE));
                candidateAuth = candidateAuthRepository.save(candidateAuth);

                candidateAuth = candidateAuthRepository.findById(candidateAuth.getAuthId()).orElseThrow();

                Candidate candidate = new Candidate();
                candidate.setAuth(candidateAuth);
                candidateRepository.save(candidate); // loi o day

                oAuth2 = new OAuth2();
                oAuth2.setProviderUserId(providerUserId);
                oAuth2.setProviderName(providerName);
                oAuth2.setCandidateAuth(candidateAuth);
                oAuth2Repository.save(oAuth2);
            }

            oAuth2.setCode(UUID.randomUUID().toString());
            oAuth2.setCodeCreatedAt(LocalDateTime.now());
            oAuth2Repository.save(oAuth2);


            resp.sendRedirect(FRONTEND_URL + "/oauth2" + "?" +"code=" + oAuth2.getCode());
        }
    }
}
