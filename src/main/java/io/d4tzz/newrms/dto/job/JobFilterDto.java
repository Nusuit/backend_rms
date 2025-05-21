package io.d4tzz.newrms.dto.job;

import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Builder
public class JobFilterDto {
    private String title;
    private String industry;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate deadlineFrom;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate deadlineTo;
    private Long minSalary;
    private Long maxSalary;
}
