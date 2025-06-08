package io.d4tzz.newrms.service.auth;

import io.d4tzz.newrms.dto.auth.UserProfileResponse;
import io.d4tzz.newrms.entity.Candidate;
import io.d4tzz.newrms.entity.CandidateAuth;
import io.d4tzz.newrms.entity.Recruiter;
import io.d4tzz.newrms.entity.RecruiterAuth;
import io.d4tzz.newrms.exception.ResourceNotFoundException;
import io.d4tzz.newrms.repository.CandidateAuthRepository;
import io.d4tzz.newrms.repository.CandidateRepository;
import io.d4tzz.newrms.repository.RecruiterAuthRepository;
import io.d4tzz.newrms.repository.RecruiterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonAuthService {
    private final RecruiterAuthRepository recruiterAuthRepository;
    private final CandidateAuthRepository candidateAuthRepository;
    private final RecruiterRepository recruiterRepository;
    private final CandidateRepository candidateRepository;

    public UserProfileResponse getCurrentUserProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Try to find as Recruiter
        RecruiterAuth recruiterAuth = recruiterAuthRepository.findByEmail(email).orElse(null);
        if (recruiterAuth != null) {
            Recruiter recruiter = recruiterRepository.findById(recruiterAuth.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found for email: " + email));
            return buildUserProfileResponse(recruiterAuth.getId(), recruiterAuth.getEmail(), recruiter.getName(), recruiterAuth.getRole().getName().name());
        }

        // Try to find as Candidate
        CandidateAuth candidateAuth = candidateAuthRepository.findByEmail(email).orElse(null);
        if (candidateAuth != null) {
            Candidate candidate = candidateRepository.findById(candidateAuth.getAuthId())
                    .orElseThrow(() -> new ResourceNotFoundException("Candidate profile not found for email: " + email));
            return buildUserProfileResponse(candidateAuth.getAuthId(), candidateAuth.getEmail(), candidate.getName(), candidateAuth.getRole().getName().name());
        }

        throw new ResourceNotFoundException("User not found with email: " + email);
    }

    private UserProfileResponse buildUserProfileResponse(Long id, String email, String fullName, String role) {
        String firstName = fullName;
        String lastName = "";

        int lastSpaceIndex = fullName.lastIndexOf(" ");
        if (lastSpaceIndex != -1) {
            firstName = fullName.substring(0, lastSpaceIndex);
            lastName = fullName.substring(lastSpaceIndex + 1);
        }

        return UserProfileResponse.builder()
                .id(id)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .role(role)
                .build();
    }
} 