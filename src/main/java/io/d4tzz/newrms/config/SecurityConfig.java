package io.d4tzz.newrms.config;

import io.d4tzz.newrms.security.*;
import io.d4tzz.newrms.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UsernamePasswordAuthService usernamePasswordAuthService;
    private final JwtService jwtService;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Value("${rms.frontend.url}")
    private String FRONTEND_URL;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/css/**", "/js/**", "/favicon.ico").permitAll()
                                // Public endpoints for OAuth2
                                .requestMatchers("/oauth2/authorization/**").permitAll()
                                
                                // Public candidate endpoints
                                .requestMatchers("/api/auth/applicant/**").permitAll()

                                // Public recruiter login endpoints
                                .requestMatchers("/api/auth/recruiter/login", "/api/auth/recruiter/login/refresh").permitAll()
                                
                                // Authenticated user endpoint 
                                .requestMatchers("/api/auth/me").authenticated()

                                // Recruiter endpoints
                                .requestMatchers("/api/recruiter/**").hasRole("RECRUITER")
                                
                                // Protected applicant endpoints 
                                .requestMatchers("/api/applicant/saved-jobs", "/api/applicant/saved-jobs/**").hasRole("APPLICANT")
                                .requestMatchers("/api/applicant/jobs/*/applications", "/api/applicant/applications/**").hasRole("APPLICANT")
                                
                                .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandler -> {
                    exceptionHandler.authenticationEntryPoint(authenticationEntryPoint());
                    exceptionHandler.accessDeniedHandler(accessDeniedHandler());
                })
                .oauth2Login(oauth2 -> {
                    oauth2
                            .redirectionEndpoint(redirection -> redirection
                                    .baseUri("/api/oauth2/login/**")
                            )
                            .successHandler(oAuth2AuthenticationSuccessHandler);
                })
                .addFilterAfter(jwtAuthenticationFilter(), SecurityContextHolderFilter.class)
        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(usernamePasswordAuthService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(jwtService);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(daoAuthenticationProvider(), jwtAuthenticationProvider());
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(authenticationManager());
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return new SimpleAccessDeniedHandler(handlerExceptionResolver);
    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return new SimpleAuthenticationEntryPoint(handlerExceptionResolver);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(FRONTEND_URL));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
