package io.d4tzz.newrms.dto.job;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class RecruiterJobDto extends JobDto {
    private long applicationQuantity;
}
