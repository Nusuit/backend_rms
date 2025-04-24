package org.example.rms.repo;

import org.example.rms.entity.InterviewStage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterviewStageRepository extends JpaRepository<InterviewStage, Long> {

}
