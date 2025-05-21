package io.d4tzz.newrms.service.application;

import io.d4tzz.newrms.dto.application.ApplicationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CandidateApplicationService {

    Page<ApplicationDto> getApplications(Pageable pageable);
}
