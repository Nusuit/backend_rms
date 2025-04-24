package org.example.rms.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.rms.entity.OAuth2;
import org.example.rms.entity.Role;
import org.example.rms.entity.User;
import org.example.rms.repo.OAuth2Repository;
import org.example.rms.repo.RoleRepository;
import org.example.rms.repo.UnverifiedUserRepository;
import org.example.rms.repo.UserRepository;
import org.example.rms.utils.JwtUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final OAuth2Repository oAuth2Repository;
    private final UnverifiedUserRepository unverifiedUserRepository;
    private final RoleRepository roleRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof OAuth2AuthenticationToken oauth2Token) {
            String providerName = oauth2Token.getAuthorizedClientRegistrationId();

            OAuth2User oauth2User = oauth2Token.getPrincipal();
            String providerUserId = oauth2User.getName();
            String email = oauth2User.getAttribute("email");

            OAuth2 oAuth2 = null;
            User user;
            Optional<OAuth2> oAuth2optional = oAuth2Repository.findByProviderUserIdAndProviderName(providerUserId, providerName);
            if (oAuth2optional.isEmpty()) {
                user = new User();
                user.setEmail(email);
                user.setRole(roleRepository.getCandidateRole());
                user = userRepository.save(user);

                oAuth2 = new OAuth2();
                oAuth2.setProviderUserId(providerUserId);
                oAuth2.setProviderName(providerName);
                oAuth2.setUser(user);
                oAuth2 = oAuth2Repository.save(oAuth2);
            } else {
                oAuth2 = oAuth2optional.get();
            }

            oAuth2.setCode(UUID.randomUUID().toString());
            oAuth2.setCodeCreatedAt(LocalDateTime.now());
            oAuth2Repository.save(oAuth2);

            response.sendRedirect("http://localhost:3000/oauth2" + "?" +"code=" + oAuth2.getCode());
        }

    }
}
