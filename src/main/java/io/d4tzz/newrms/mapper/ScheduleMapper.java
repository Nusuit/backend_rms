package io.d4tzz.newrms.mapper;

import io.d4tzz.newrms.dto.schedule.CreateScheduleRequest;
import io.d4tzz.newrms.dto.schedule.ScheduleDto;
import io.d4tzz.newrms.dto.schedule.UpdateScheduleRequest;
import io.d4tzz.newrms.entity.Schedule;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    Schedule toSchedule(CreateScheduleRequest createScheduleRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePartial(UpdateScheduleRequest updateScheduleRequest, @MappingTarget Schedule schedule);

    @Mapping(target = "jobId", source = "job.id")
    @Mapping(target = "jobStageId", source = "stage.id")
    ScheduleDto toScheduleDto(Schedule schedule);
}
