package org.example.rms.mapper.recruiter;

import org.example.rms.dto.recruiter.NoteApplicationRequest;
import org.example.rms.dto.recruiter.NoteApplicationResponse;
import org.example.rms.entity.Application;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface NoteApplicationMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateApplication(NoteApplicationRequest request, @MappingTarget Application application);

    @Mapping(source = "candidate.cvUrl", target = "cvUrl")
    NoteApplicationResponse toNoteApplicationResponse(Application application);
}
