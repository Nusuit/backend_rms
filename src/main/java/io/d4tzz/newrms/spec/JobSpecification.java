package io.d4tzz.newrms.spec;

import io.d4tzz.newrms.entity.Job;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class JobSpecification {
    public static Specification<Job> hasRecruiterId(Long id) {
        if (id == null) {
            return conjunction();
        }
        return (root, query, cb) -> cb.equal(root.get("recruiter").get("id"), id);
    }

    public static Specification<Job> hasTitle(String title) {
        if (title == null || title.isEmpty()) {
            return conjunction();
        }
        return (root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    // Phương thức belongsToIndustry đã được xóa vì Job entity không có mối quan hệ trực tiếp với Industry
    // public static Specification<Job> belongsToIndustry(String industry) {
    //     if (industry == null || industry.isEmpty()) {
    //         return conjunction();
    //     }
    //     return (root, query, cb) -> cb.like(cb.lower(root.get("industry").get("name")), "%" + industry.toLowerCase() + "%");
    // }

    public static Specification<Job> deadlineFrom(LocalDate from) {
        if (from == null) {
            return conjunction();
        }
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("deadline"), from);
    }

    public static Specification<Job> deadlineTo(LocalDate to) {
        if (to == null) {
            return conjunction();
        }
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("deadline"), to);
    }

    public static Specification<Job> matchesSalaryRange(Long expectedMinSalary, Long expectedMaxSalary) {
        if (expectedMinSalary == null || expectedMaxSalary == null) {
            return conjunction();
        }

        return ((root, query, cb) ->
                cb.and(
                        cb.greaterThanOrEqualTo(root.get("maxSalary"), expectedMinSalary),
                        cb.lessThanOrEqualTo(root.get("minSalary"), expectedMaxSalary)
                )
        );
    }

    private static Specification<Job> conjunction() {
        return (root, query, cb) -> cb.conjunction();
    }
}

