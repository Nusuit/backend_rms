package io.d4tzz.newrms.service.profile;

import io.d4tzz.newrms.dto.recruiter.RecruiterDto;
import io.d4tzz.newrms.entity.Recruiter;
import io.d4tzz.newrms.exception.UserNotFoundException;
import io.d4tzz.newrms.repository.RecruiterRepository;
import io.d4tzz.newrms.service.AbstractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruiterProfileService extends AbstractService {

    private final RecruiterRepository recruiterRepository;

    public RecruiterDto getProfile(Long recruiterId) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId).orElseThrow(
                () -> new UserNotFoundException("Recruiter not found")
        );

        return RecruiterDto.builder()
                .id(recruiter.getId())
                .email(recruiter.getAuth().getEmail())
                .name(recruiter.getName())
                .description(recruiter.getDescription())
                .profilePictureUrl(recruiter.getProfilePictureUrl())
                .build();
    }
}
