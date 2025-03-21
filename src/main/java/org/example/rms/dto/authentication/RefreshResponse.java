package org.example.rms.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshResponse {
    private String accessToken;
    private String refreshToken;
    private String type;

}
