package org.example.rms.dto.authentication;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResendOtpResponse {
    private Long waitingTime;

    public ResendOtpResponse(Long waitingTime) {
        this.waitingTime = waitingTime;
        if (waitingTime == 0) this.waitingTime = null;
    }
}
