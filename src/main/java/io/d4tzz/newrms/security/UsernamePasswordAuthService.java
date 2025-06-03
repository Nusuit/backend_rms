package io.d4tzz.newrms.security;

import io.d4tzz.newrms.entity.RecruiterAuth; // Thay đổi từ Admin sang RecruiterAuth
import io.d4tzz.newrms.repository.RecruiterAuthRepository; // Thay đổi từ AdminRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UsernamePasswordAuthService implements UserDetailsService {
    private final RecruiterAuthRepository recruiterAuthRepository; // Thay đổi repository

    public UsernamePasswordAuthService(RecruiterAuthRepository recruiterAuthRepository) { // Thay đổi constructor
        this.recruiterAuthRepository = recruiterAuthRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tìm RecruiterAuth theo username
        RecruiterAuth recruiterAuth = recruiterAuthRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        String role = recruiterAuth.getRole().getName().toString(); // Lấy tên role từ RecruiterAuth

        return new UsernamePasswordUserPrinciple(
                recruiterAuth.getUsername(),
                recruiterAuth.getPassword(),
                recruiterAuth.getId(), // Lấy ID của RecruiterAuth
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }
}

