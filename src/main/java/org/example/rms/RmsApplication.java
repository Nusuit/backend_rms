package org.example.rms;

import org.example.rms.entity.Candidate;
import org.example.rms.entity.Recruiter;
import org.example.rms.entity.User;
import org.example.rms.entity.UserStatus;
import org.example.rms.repo.CandidateRepository;
import org.example.rms.repo.RecruiterRepository;
import org.example.rms.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;


@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})

public class RmsApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(RmsApplication.class, args);
        RecruiterRepository recruiterRepository = ctx.getBean(RecruiterRepository.class);
        UserRepository userRepository = ctx.getBean(UserRepository.class);
        TransactionTemplate transactionTemplate = ctx.getBean(TransactionTemplate.class);
        CandidateRepository candidateRepository = ctx.getBean(CandidateRepository.class);


        transactionTemplate.executeWithoutResult(status -> {

        });
    }


}