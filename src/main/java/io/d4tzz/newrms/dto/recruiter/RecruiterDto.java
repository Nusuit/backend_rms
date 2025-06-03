package io.d4tzz.newrms.dto.recruiter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

// Đây là một DTO đơn giản cho Recruiter.
// Bạn có thể thêm các trường khác tùy theo nhu cầu hiển thị profile Recruiter.
@Getter
@Builder
public class RecruiterDto {
    @JsonProperty("recruiterId")
    private Long id;
    private String username; // Thường là email hoặc username đăng nhập
    private String name;
    private String description;
    private String profilePictureUrl;
    // Thêm các trường khác nếu cần
}
