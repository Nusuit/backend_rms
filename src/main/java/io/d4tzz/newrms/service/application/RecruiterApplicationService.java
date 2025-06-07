package io.d4tzz.newrms.service.application;

import io.d4tzz.newrms.dto.application.ApplicationDto;
import io.d4tzz.newrms.mapper.ApplicationMapper;
import io.d4tzz.newrms.repository.ApplicationRepository;
import io.d4tzz.newrms.security.JwtUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class RecruiterApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;

    public Page<ApplicationDto> getApplications(Long jobId, String status, Pageable pageable) {
        JwtUserPrincipal principal = (JwtUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long recruiterId = principal.getIdentity();

        Specification<io.d4tzz.newrms.entity.Application> spec = Specification.where(null);

        // Filter by recruiter's jobs
        spec = spec.and((root, query, cb) -> cb.equal(root.get("job").get("recruiter").get("id"), recruiterId));

        if (jobId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("job").get("id"), jobId));
        }

        if (StringUtils.hasText(status) && !"ALL".equalsIgnoreCase(status)) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        return applicationRepository.findAll(spec, pageable).map(applicationMapper::toApplicationDto);
    }
} 