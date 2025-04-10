package org.example.rms.dto.candidate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateProfilePictureResponse {
    String profilePictureUrl;
}
