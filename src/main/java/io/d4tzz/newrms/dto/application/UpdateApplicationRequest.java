package io.d4tzz.newrms.dto.application;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateApplicationRequest {
    private String coverLetter;
}
