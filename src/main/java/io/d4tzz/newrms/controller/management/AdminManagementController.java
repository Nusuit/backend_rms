package io.d4tzz.newrms.controller.management;

import io.d4tzz.newrms.entity.RecruiterAuth;
import io.d4tzz.newrms.security.UsernamePasswordUserPrinciple;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/management")
public class AdminManagementController {
    @GetMapping("/dashboard")
    public String getAdminDashboardPage(@AuthenticationPrincipal UsernamePasswordUserPrinciple admin, Model model) {
        model.addAttribute("admin", admin);

        //Todo: get number of recruiter and candidate

        return "admin/dashboard";
    }

    @GetMapping("/recruiters")
    public String getRecruiterManagementPage(Model model) {

        model.addAttribute("newRecruiter", new RecruiterAuth());
        return "admin/recruiter_management";
    }

    @PostMapping("/recruiters")
    public String addRecruiter(@Valid @ModelAttribute("newRecruiter") RecruiterAuth recruiter, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/recruiter_management";
        }

        return "empty";
    }
}
