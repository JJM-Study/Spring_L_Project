package org.example.myproject.login;

import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.security.Principal;

@Controller
public class LoginController {

    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(Principal principal) {

//        model.addAttribute("user", principal.getName());

//        if (principal.getName() != null) {
//            logger.info(principal.getName());
//        }
        return "login";
    }
}
