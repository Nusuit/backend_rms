package org.example.rms.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.rms.repo.JobRepository;
import org.example.rms.repo.RecruiterRepository;
import org.example.rms.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final RecruiterRepository recruiterRepository;


    @Autowired
    private JobRepository jobRepository;

    @GetMapping("/hello")
    public String hello() {
        return "Hello world";
    }

    @GetMapping("/oauth")
    public RedirectView oauth(HttpServletRequest request) {

        System.out.println(request.getSession().getId());

        request.getSession().setAttribute("Key", request.getRequestURI());

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://localhost:8080/oauth2/authorize/google");
        return redirectView;
    }

    @GetMapping("/session")
    public String session(HttpServletRequest request) {
        return request.getSession().getAttribute("Key").toString();
    }

    @GetMapping("/candidate")
    public String candidate(HttpServletRequest request) {
        return "You are a candidate";
    }

    @GetMapping("/recruiter")
    public String recruiter(@AuthenticationPrincipal UserPrincipal userDetail) {
        return String.valueOf(userDetail.getIdentity());
    }

}
