package io.d4tzz.newrms.service.companyprofile;

import io.d4tzz.newrms.dto.companyprofile.CompanyProfileDto;
import io.d4tzz.newrms.entity.CompanyProfile;
import io.d4tzz.newrms.mapper.companyprofile.CompanyProfileMapper;
import io.d4tzz.newrms.repository.CompanyProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class CompanyProfileService {

    private final CompanyProfileRepository companyProfileRepository;
    private final CompanyProfileMapper companyProfileMapper;

    @Autowired
    public CompanyProfileService(CompanyProfileRepository companyProfileRepository, CompanyProfileMapper companyProfileMapper) {
        this.companyProfileRepository = companyProfileRepository;
        this.companyProfileMapper = companyProfileMapper;
    }

    @Transactional(readOnly = true)
    public CompanyProfileDto getCompanyProfile() {
        // Assuming there's only one company profile or fetching the first one
        Optional<CompanyProfile> profileOptional = companyProfileRepository.findAll().stream().findFirst();
        return profileOptional.map(companyProfileMapper::toCompanyProfileDto).orElse(null);
    }

    @Transactional
    public CompanyProfileDto updateCompanyProfile(CompanyProfileDto companyProfileDto) {
        Optional<CompanyProfile> existingProfileOptional = companyProfileRepository.findAll().stream().findFirst();
        CompanyProfile companyProfile;

        if (existingProfileOptional.isPresent()) {
            companyProfile = existingProfileOptional.get();
            companyProfileMapper.updateCompanyProfileFromDto(companyProfileDto, companyProfile);
        } else {
            companyProfile = companyProfileMapper.toCompanyProfile(companyProfileDto);
        }
        CompanyProfile savedProfile = companyProfileRepository.save(companyProfile);
        return companyProfileMapper.toCompanyProfileDto(savedProfile);
    }

    @Transactional
    public CompanyProfileDto uploadCompanyLogo(MultipartFile logoFile) throws IOException {
        // In a real application, you would save this file to a cloud storage (e.g., S3, Google Cloud Storage)
        // and return the URL. For simplicity, we'll just mock the URL generation.

        Optional<CompanyProfile> existingProfileOptional = companyProfileRepository.findAll().stream().findFirst();
        CompanyProfile companyProfile;

        if (existingProfileOptional.isPresent()) {
            companyProfile = existingProfileOptional.get();
        } else {
            companyProfile = new CompanyProfile(); // Create a new profile if none exists
        }

        // Mocking logo URL for now. Replace with actual file storage logic.
        String mockLogoUrl = "/assets/uploads/company_logos/" + logoFile.getOriginalFilename();
        companyProfile.setLogoUrl(mockLogoUrl);

        CompanyProfile savedProfile = companyProfileRepository.save(companyProfile);
        return companyProfileMapper.toCompanyProfileDto(savedProfile);
    }
} 