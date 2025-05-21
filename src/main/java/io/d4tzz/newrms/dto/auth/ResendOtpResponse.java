package io.d4tzz.newrms.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResendOtpResponse {
    private Long waitingTime;
}
