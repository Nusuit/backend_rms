package org.example.rms.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/test")
public class TestController {
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
    public String recruiter(HttpServletRequest request) {
        return "You are a recruiter";
    }
}
