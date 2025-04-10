package org.example.rms.mapper.candidate;

import org.example.rms.dto.candidate.ModifyApplicationRequest;
import org.example.rms.entity.Application;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ModifyApplicationMapper {
    void updateApplication(ModifyApplicationRequest request, @MappingTarget Application application);
}
