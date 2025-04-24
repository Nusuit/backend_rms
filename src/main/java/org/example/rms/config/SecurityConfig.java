package org.example.rms.config;

import org.example.rms.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
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
public class SecurityConfig {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    public SecurityConfig(JwtAuthenticationProvider jwtAuthenticationProvider,
                          HandlerExceptionResolver handlerExceptionResolver,
                          OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/swagger-ui/index.html", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                                .requestMatchers("/api/candidate/**").hasRole("CANDIDATE")
                                .requestMatchers("/api/recruiter/**").hasRole("RECRUITER")
                                .requestMatchers("/test/**", "//login/oauth2/code/**").permitAll()
                                .anyRequest().authenticated()

                )
                .addFilterAfter(new JwtAuthenticationFilter(authenticationManager()), SecurityContextHolderFilter.class)
                .exceptionHandling(exceptionHandler
                        -> {
                    exceptionHandler.authenticationEntryPoint(this.RmsAuthenticationEntryPoint());
                    exceptionHandler.accessDeniedHandler(this.RmsAccessDeniedHandler());
                })
                .oauth2Login(oauth2 -> {
                    oauth2
                            .authorizationEndpoint(authorization -> authorization
                                    .baseUri("/oauth2/authorize")
                            )
                            .redirectionEndpoint(redirection -> redirection
                                    .baseUri("/api/auth/**")
                            )
                            .successHandler(oAuth2AuthenticationSuccessHandler);
                })
                ;

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(jwtAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint RmsAuthenticationEntryPoint() {
        return new RmsAuthenticationEntryPoint(handlerExceptionResolver);
    }

    @Bean
    public AccessDeniedHandler RmsAccessDeniedHandler() {
        return new RmsAccessDeniedHandler(handlerExceptionResolver);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.addAllowedHeader("*");
//        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
