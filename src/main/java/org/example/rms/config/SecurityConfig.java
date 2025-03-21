package org.example.rms.config;

import org.example.rms.security.RmsAuthenticationEntryPoint;
import org.example.rms.security.JwtAuthenticationFilter;
import org.example.rms.security.JwtAuthenticationProvider;
import org.example.rms.security.RmsAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class SecurityConfig {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    public SecurityConfig(JwtAuthenticationProvider jwtAuthenticationProvider, HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests.requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/signup/**", "/api/auth/refresh").permitAll()
                                .requestMatchers("/swagger-ui/index.html", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**").permitAll()
                                .requestMatchers("/api/auth/google/**").permitAll()
                                .anyRequest().hasRole("CANDIDATE")


                )
                .addFilterAfter(new JwtAuthenticationFilter(authenticationManager()), SecurityContextHolderFilter.class)
                .exceptionHandling(exceptionHandler
                        -> {
                    exceptionHandler.authenticationEntryPoint(this.RmsAuthenticationEntryPoint());
                    exceptionHandler.accessDeniedHandler(this.RmsAccessDeniedHandler());
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

}
