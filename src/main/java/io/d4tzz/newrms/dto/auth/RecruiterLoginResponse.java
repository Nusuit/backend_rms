package io.d4tzz.newrms.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter; // Có thể xóa nếu không cần setter

@Getter
@Builder
public class RecruiterLoginResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private boolean isSuperRecruiter; // Thêm trường này để đánh dấu tài khoản đặc biệt
}

