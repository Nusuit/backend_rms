package io.d4tzz.newrms.mapper;

import io.d4tzz.newrms.dto.candidate.CandidateDto;
import io.d4tzz.newrms.dto.candidate.UpdateCandidateInfoRequest;
import io.d4tzz.newrms.entity.Candidate;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CandidateMapper {
    @org.mapstruct.Mapping(target = "email", source = "auth.email")
    CandidateDto toCandidateDto(Candidate candidate);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePartial(UpdateCandidateInfoRequest candidateInfo, @MappingTarget Candidate candidate);
}
