package io.d4tzz.newrms.controller.application;

import io.d4tzz.newrms.dto.ApiResponse;
import io.d4tzz.newrms.dto.application.ApplicationDto;
import io.d4tzz.newrms.service.application.RecruiterApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/recruiter")
@RequiredArgsConstructor
public class RecruiterApplicationController {

    private final RecruiterApplicationService recruiterApplicationService;

    @GetMapping("/applications")
    public ApiResponse<Page<ApplicationDto>> getAllApplications(
            @RequestParam(required = false) Long jobId,
            @RequestParam(required = false) String status,
            Pageable pageable
    ) {
        Page<ApplicationDto> applicationsPage = recruiterApplicationService.getApplications(jobId, status, pageable);
        return ApiResponse.success(applicationsPage);
    }
} 