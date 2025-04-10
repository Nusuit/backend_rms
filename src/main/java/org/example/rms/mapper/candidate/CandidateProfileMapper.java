package org.example.rms.mapper.candidate;

import org.example.rms.dto.candidate.CandidateProfileResponse;
import org.example.rms.entity.Candidate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CandidateProfileMapper {
    CandidateProfileResponse toCandidateProfileResponse(Candidate candidate);
}
