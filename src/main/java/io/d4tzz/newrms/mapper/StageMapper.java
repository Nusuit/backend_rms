package io.d4tzz.newrms.mapper;

import io.d4tzz.newrms.dto.job.JobStageDto;
import io.d4tzz.newrms.entity.JobStage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StageMapper {
//    @Mapping(target = "jobStageId", source = "id")
//    @Mapping(target = "name", source = "stage.name")
//    @Mapping(target = "order", source = "order")
//    JobStageDto toStageDto(JobStage stage);
}
