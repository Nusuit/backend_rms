package io.d4tzz.newrms.dto.job;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CancelJobRequest {
    private String reason;
}
