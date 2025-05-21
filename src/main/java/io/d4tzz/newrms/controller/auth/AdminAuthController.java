package io.d4tzz.newrms.controller.auth;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/auth")
public class AdminAuthController {
    @GetMapping("/login")
    public String getAdminLoginPage() {
        return "admin/login";
    }

}
