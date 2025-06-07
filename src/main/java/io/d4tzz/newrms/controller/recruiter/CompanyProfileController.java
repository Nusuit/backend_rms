package io.d4tzz.newrms.controller.recruiter;

import io.d4tzz.newrms.dto.companyprofile.CompanyProfileDto;
import io.d4tzz.newrms.payload.ApiResponse;
import io.d4tzz.newrms.service.companyprofile.CompanyProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/recruiter/company-profile")
public class CompanyProfileController {

    private final CompanyProfileService companyProfileService;

    @Autowired
    public CompanyProfileController(CompanyProfileService companyProfileService) {
        this.companyProfileService = companyProfileService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CompanyProfileDto>> getCompanyProfile() {
        CompanyProfileDto profile = companyProfileService.getCompanyProfile();
        if (profile != null) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Company profile retrieved successfully.", profile));
        } else {
            return ResponseEntity.ok(new ApiResponse<>(false, "Company profile not found. Please create one.", null));
        }
    }

    @PutMapping
    public ResponseEntity<ApiResponse<CompanyProfileDto>> updateCompanyProfile(@RequestBody CompanyProfileDto companyProfileDto) {
        CompanyProfileDto updatedProfile = companyProfileService.updateCompanyProfile(companyProfileDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Company profile updated successfully.", updatedProfile));
    }

    @PostMapping("/upload-logo")
    public ResponseEntity<ApiResponse<CompanyProfileDto>> uploadCompanyLogo(@RequestParam("logo") MultipartFile logoFile) throws IOException {
        if (logoFile.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Logo file is empty.", null));
        }
        CompanyProfileDto updatedProfile = companyProfileService.uploadCompanyLogo(logoFile);
        return ResponseEntity.ok(new ApiResponse<>(true, "Company logo uploaded successfully.", updatedProfile));
    }
} 