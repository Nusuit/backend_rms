package io.d4tzz.newrms.security;

import io.d4tzz.newrms.entity.RecruiterAuth;
import io.d4tzz.newrms.repository.RecruiterAuthRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UsernamePasswordAuthService implements UserDetailsService {
    private final RecruiterAuthRepository recruiterAuthRepository;

    public UsernamePasswordAuthService(RecruiterAuthRepository recruiterAuthRepository) {
        this.recruiterAuthRepository = recruiterAuthRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { // Đã đổi username thành email
        // Tìm RecruiterAuth theo email
        RecruiterAuth recruiterAuth = recruiterAuthRepository.findByEmail(email).orElseThrow( // Đã đổi findByUsername thành findByEmail
                () -> new UsernameNotFoundException("User not found")
        );

        String role = recruiterAuth.getRole().getName().toString();

        return new UsernamePasswordUserPrinciple(
                recruiterAuth.getEmail(), // Đã đổi username thành email
                recruiterAuth.getPassword(),
                recruiterAuth.getId(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }
}
