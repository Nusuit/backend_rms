package io.d4tzz.newrms.controller.application;

import io.d4tzz.newrms.dto.ApiResponse;
import io.d4tzz.newrms.dto.application.ApplicationDto;
import io.d4tzz.newrms.dto.application.UpdateApplicationRequest;
import io.d4tzz.newrms.service.application.CandidateApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/applicant") // Đã đổi từ "api/candidate"
@RequiredArgsConstructor
public class CandidateApplicationController {
    private final CandidateApplicationService candidateApplicationService;

    @GetMapping("/applications")
    public ApiResponse<?> getApplications(Pageable pageable) {
        Page<ApplicationDto> applicationDtoPage = candidateApplicationService.getApplications(pageable);

        return ApiResponse.success(applicationDtoPage);
    }
}

