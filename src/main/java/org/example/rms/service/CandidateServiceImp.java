package org.example.rms.service;

import lombok.RequiredArgsConstructor;
import org.example.rms.dto.candidate.*;
import org.example.rms.entity.*;
import org.example.rms.exception.DuplicateApplicationException;
import org.example.rms.exception.ResourceNotFoundException;
import org.example.rms.mapper.candidate.*;
import org.example.rms.repo.ApplicationRepository;
import org.example.rms.repo.CandidateRepository;
import org.example.rms.repo.JobRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CandidateServiceImp {
    private final JobRepository jobRepository;
    private final CandidateRepository candidateRepository;
    private final ApplicationRepository applicationRepository;

    private final ApplyJobMapper applyJobMapper;
    private final CandidateJobMapper candidateJobMapper;
    private final CandidateProfileMapper candidateProfileMapper;
    private final ModifyApplicationMapper modifyApplicationMapper;
    private final UpdateCandidateProfileMapper updateCandidateProfileMapper;
    private final CandidateApplicationMapper candidateApplicationMapper;

    private final CloudService cloudService;

    public Page<CandidateJobResponse> getJobs(Long candidateId, Pageable pageable) {
        List<Application> applications = applicationRepository.findByCandidateId(candidateId);

        Set<Long> appliedJobIds = new HashSet<>();
        for (Application application : applications) {
            appliedJobIds.add(application.getJob().getId());
        }

        Page<Job> jobPage = jobRepository.findByStatus(JobStatus.OPEN, pageable);

        return jobPage.map(job -> {
            CandidateJobResponse jobResponse = candidateJobMapper.toCandidateJobResponse(job);
            boolean applicable = !appliedJobIds.contains(job.getId());
            return jobResponse.toBuilder().applicable(applicable).build();
        });
    }

    public ApplyJobResponse applyJob(Long candidateId, Long jobId, ApplyJobRequest request) {
        Application application = applicationRepository.findByJobIdAndCandidateId(jobId, candidateId).orElse(null);
        if (application != null) {
            throw new DuplicateApplicationException(HttpStatus.CONFLICT, "Application already applied");
        }

        application = applyJobMapper.toApplication(request);
        application.setStatus(ApplicationStatus.APPLIED);
        application.setJob(jobRepository.getReferenceById(jobId));
        application.setCandidate(candidateRepository.getReferenceById(candidateId));
        application = applicationRepository.save(application);

        return applyJobMapper.toApplyJobResponse(application);
    }

    public CandidateApplicationResponse getApplication(Long candidateId, Long applicationId) {
        Application application = applicationRepository.findByIdAndCandidateId(applicationId, candidateId).orElseThrow(
                () -> new ResourceNotFoundException("Application not found or not owned")
        );

        return candidateApplicationMapper.toCandidateApplicationResponse(application);
    }

    public Page<CandidateApplicationResponse> getApplications(Long candidateId, Pageable pageable) {
        return applicationRepository.findByCandidateId(candidateId, pageable)
                .map(candidateApplicationMapper::toCandidateApplicationResponse);
    }

    public void modifyApplication(Long candidateId, Long applicationId, ModifyApplicationRequest request) {
        Application application = applicationRepository.findByIdAndCandidateId(applicationId, candidateId).orElseThrow(
                () -> new ResourceNotFoundException("Application not found or not owned")
        );

        modifyApplicationMapper.updateApplication(request, application);

        applicationRepository.save(application);
    }

    public void deleteApplication(Long candidateId, Long applicationId) {
        Application application = applicationRepository.findByIdAndCandidateId(applicationId, candidateId).orElseThrow(
                () -> new ResourceNotFoundException("Application not found or not owned")
        );

        applicationRepository.delete(application);
    }

    public List<Gender> getGenders() {
        return List.of(Gender.MALE, Gender.FEMALE, Gender.OTHER);
    }

    public CandidateProfileResponse getProfile(Long candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
                () -> new ResourceNotFoundException("Candidate not found")
        );
        return candidateProfileMapper.toCandidateProfileResponse(candidate);
    }

    public UpdateCandidateProfileResponse updateProfile(Long candidateId, UpdateCandidateProfileRequest request) {
        Candidate candidate = updateCandidateProfileMapper.toCandidate(request);
        candidate.setId(candidateId);
        candidate = candidateRepository.save(candidate);

        return updateCandidateProfileMapper.toUpdateCandidateProfileResponse(candidate);
    }

    public UpdateCvResponse updateCv(Long candidateId, MultipartFile cvFile) {
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
                () -> new ResourceNotFoundException("Candidate not found")
        );

        String url = cloudService.urlUploadFile(cvFile);
        candidate.setCvUrl(url);
        candidateRepository.save(candidate);

        return UpdateCvResponse.builder().cvUrl(url).build();
    }

    public UpdateProfilePictureResponse updateProfilePicture(Long candidateId, MultipartFile imgFile) {
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
                () -> new ResourceNotFoundException("Candidate not found")
        );

        String url = cloudService.urlUploadFile(imgFile);
        candidate.setProfilePictureUrl(url);
        candidateRepository.save(candidate);

        return UpdateProfilePictureResponse.builder().profilePictureUrl(url).build();
    }
}
