package io.d4tzz.newrms.dto.job;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class CandidateJobDto extends JobDto {
    private boolean applicable;
}
