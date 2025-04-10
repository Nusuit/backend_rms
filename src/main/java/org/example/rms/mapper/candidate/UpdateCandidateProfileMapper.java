package org.example.rms.mapper.candidate;

import org.example.rms.dto.candidate.UpdateCandidateProfileRequest;
import org.example.rms.dto.candidate.UpdateCandidateProfileResponse;
import org.example.rms.entity.Candidate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UpdateCandidateProfileMapper {
    Candidate toCandidate(UpdateCandidateProfileRequest request);

    UpdateCandidateProfileResponse toUpdateCandidateProfileResponse(Candidate candidate);
}
