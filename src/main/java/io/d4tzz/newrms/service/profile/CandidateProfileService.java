package io.d4tzz.newrms.service.profile;

import com.cloudinary.Cloudinary;
import io.d4tzz.newrms.dto.candidate.CandidateDto;
import io.d4tzz.newrms.dto.candidate.UpdateCandidateInfoRequest;
import io.d4tzz.newrms.entity.Candidate;
import io.d4tzz.newrms.exception.UserNotFoundException;
import io.d4tzz.newrms.mapper.CandidateMapper;
import io.d4tzz.newrms.repository.CandidateRepository;
import io.d4tzz.newrms.service.AbstractService;
import io.d4tzz.newrms.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CandidateProfileService extends AbstractService {
    private final CandidateRepository candidateRepository;
    private final CandidateMapper candidateMapper;
    private final FileService fileService;


    public CandidateDto updateInformation(UpdateCandidateInfoRequest request) {
        Long candidateId = getUserIdentity();
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
                () -> new UserNotFoundException("Candidate not found")
        );

        candidateMapper.updatePartial(request, candidate);
        candidate = candidateRepository.save(candidate);

        return candidateMapper.toCandidateDto(candidate);
    }


    public CandidateDto updateCv(MultipartFile cvFile) {
        Long candidateId = getUserIdentity();
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
                () -> new UserNotFoundException("Candidate not found")
        );

        String cvFileUrl = fileService.uploadFile(cvFile);
        candidate.setCvUrl(cvFileUrl);
        candidateRepository.save(candidate);

        return candidateMapper.toCandidateDto(candidate);
    }



    public CandidateDto updateAvatar(MultipartFile avatarFile) {
        Long candidateId = getUserIdentity();
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
                () -> new UserNotFoundException("Candidate not found")
        );

        String avatarImgUrl = fileService.uploadFile(avatarFile);
        candidate.setAvatarUrl(avatarImgUrl);
        candidateRepository.save(candidate);

        return candidateMapper.toCandidateDto(candidate);
    }


    public CandidateDto getProfile() {
        Long candidateId = getUserIdentity();
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
                () -> new UserNotFoundException("Candidate not found")
        );

        return candidateMapper.toCandidateDto(candidate);
    }
}

