package io.d4tzz.newrms.security;

import io.d4tzz.newrms.entity.Admin;
import io.d4tzz.newrms.repository.AdminRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UsernamePasswordAuthService implements UserDetailsService {
    private final AdminRepository adminRepository;

    public UsernamePasswordAuthService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        String role = admin.getRole().toString();

        return new UsernamePasswordUserPrinciple(
                admin.getUsername(),
                admin.getPassword(),
                admin.getAdminId(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }
}
