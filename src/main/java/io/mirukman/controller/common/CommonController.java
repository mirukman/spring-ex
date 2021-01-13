package io.mirukman.controller.common;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class CommonController {
    
    @GetMapping("/accessDenied")
    public void accessDenied(Authentication auth, Model model) {
        log.info("access Denied: " + auth);
        model.addAttribute("msg", "Access Denied");
    }

    @GetMapping("/customLogin")
    public void loginInput(String error, String logout, Model model) {

        if (error != null) {
            model.addAttribute("error", "Login Error Check your Account");
        }

        if (logout != null) {
            model.addAttribute("logout", "Logout!!");
        }
    }
}
