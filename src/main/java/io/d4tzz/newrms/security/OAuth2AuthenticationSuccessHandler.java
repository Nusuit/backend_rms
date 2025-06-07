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

            // Biến để lưu thông tin người dùng được xác định sau khi kiểm tra vai trò
            String userRoleName;
            String userIdForJwt;
            String userEmailForJwt;
            String userStatusForJwt;
            String refreshTokenForJwt; // Để lưu refresh token vào đúng bảng

            // 1. Kiểm tra xem email này có phải là Recruiter đã tồn tại không
            Optional<RecruiterAuth> existingRecruiterAuth = recruiterAuthRepository.findByEmail(email);

            if (existingRecruiterAuth.isPresent()) {
                // Nếu email là của Recruiter đã tồn tại, gán vai trò RECRUITER
                RecruiterAuth recruiterAuth = existingRecruiterAuth.get();
                userRoleName = recruiterAuth.getRole().getName().toString();
                userIdForJwt = String.valueOf(recruiterAuth.getId());
                userEmailForJwt = recruiterAuth.getEmail();
                userStatusForJwt = recruiterAuth.getStatus().toString();

                // Cập nhật trạng thái ACTIVE nếu chưa
                if (recruiterAuth.getStatus() != UserStatus.ACTIVE) {
                    recruiterAuth.setStatus(UserStatus.ACTIVE);
                    recruiterAuthRepository.save(recruiterAuth);
                }

                // Luôn tạo refresh token mới và lưu vào bảng RecruiterAuth
                refreshTokenForJwt = jwtService.refreshToken()
                        .subject(userIdForJwt)
                        .email(userEmailForJwt)
                        .role(userRoleName)
                        .jwt();
                recruiterAuth.setRefreshToken(refreshTokenForJwt);
                recruiterAuthRepository.save(recruiterAuth);

                // Ghi chú: Cấu trúc DB hiện tại của bạn chỉ liên kết OAuth2 với CandidateAuth.
                // Nếu bạn muốn lưu thông tin liên kết OAuth2 cho Recruiter, bạn cần thay đổi schema:
                // - Hoặc thêm recruiter_auth_id (nullable) vào bảng oauth2.
                // - Hoặc tạo một bảng oauth2_recruiters riêng.
                // Hiện tại, OAuth2 entry sẽ chỉ được tạo cho CandidateAuth.
                // Nếu một recruiter đăng nhập OAuth2, bản ghi OAuth2 của họ sẽ không được lưu TẠI ĐÂY
                // theo cấu trúc hiện tại của bảng OAuth2. Logic này chỉ dùng email để nhận diện vai trò.

            } else {
                // 2. Nếu không phải Recruiter, kiểm tra xem đã là Candidate chưa
                Optional<CandidateAuth> optionalCandidateAuth = candidateAuthRepository.findByEmail(email);
                CandidateAuth candidateAuth;

                if (optionalCandidateAuth.isPresent()) {
                    candidateAuth = optionalCandidateAuth.get();
                    if (candidateAuth.getStatus() != UserStatus.ACTIVE) {
                        candidateAuth.setStatus(UserStatus.ACTIVE);
                        candidateAuthRepository.save(candidateAuth);
                    }
                    // Cập nhật hoặc tạo OAuth2 nếu chưa tồn tại cho candidateAuth này
                    Optional<OAuth2> existingOAuth2 = oAuth2Repository.findByProviderUserIdAndProviderName(providerUserId, providerName);
                    if (!existingOAuth2.isPresent()) {
                        OAuth2 newOAuth2 = new OAuth2();
                        newOAuth2.setProviderUserId(providerUserId);
                        newOAuth2.setProviderName(providerName);
                        newOAuth2.setCandidateAuth(candidateAuth);
                        oAuth2Repository.save(newOAuth2);
                    }
                } else {
                    // 3. Nếu chưa phải Recruiter cũng chưa là Candidate, tạo CandidateAuth và Candidate mới
                    candidateAuth = new CandidateAuth();
                    candidateAuth.setEmail(email);
                    candidateAuth.setStatus(UserStatus.ACTIVE);
                    candidateAuth.setRole(roleRepository.findByName(RoleName.APPLICANT)); // Mặc định là APPLICANT
                    candidateAuth = candidateAuthRepository.save(candidateAuth);

                    candidateAuth = candidateAuthRepository.findById(candidateAuth.getAuthId()).orElseThrow(
                            () -> new IllegalStateException("CandidateAuth not found after saving, this should not happen."));

                    Candidate candidate = new Candidate();
                    candidate.setAuth(candidateAuth);
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

                // Luôn tạo refresh token mới và lưu vào bảng CandidateAuth
                refreshTokenForJwt = jwtService.refreshToken()
                        .subject(userIdForJwt)
                        .email(userEmailForJwt)
                        .role(userRoleName)
                        .jwt();
                candidateAuth.setRefreshToken(refreshTokenForJwt);
                candidateAuthRepository.save(candidateAuth);
            }

            // Tạo JWT Access Token
            String accessToken = jwtService.accessToken()
                    .subject(userIdForJwt)
                    .email(userEmailForJwt)
                    .role(userRoleName)
                    .jwt();

            // Chuẩn bị context cho Thymeleaf template
            Context context = new Context();
            context.setVariable("accessToken", accessToken);
            context.setVariable("refreshToken", refreshTokenForJwt); // Gửi refresh token (dù khuyến nghị dùng HTTP-only cookie)
            context.setVariable("userRole", userRoleName);
            context.setVariable("frontendUrl", FRONTEND_URL);

            // Render template và gửi về client
            String htmlContent = templateEngine.process("oauth2_callback", context);

            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter writer = resp.getWriter();
            writer.write(htmlContent);
            writer.flush();
        }
    }
}