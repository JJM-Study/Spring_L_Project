package org.example.myproject.auth;

import org.apache.logging.log4j.LogManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import org.apache.logging.log4j.Logger;

@Controller
public class AuthController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    /* Pricipal을 쓸 지, Authentication 쓸 지 ... */
    @GetMapping("/login")
    public String loginPage(Authentication authentication) {

//        model.addAttribute("user", principal.getName());

//        if (principal.getName() != null) {
//            logger.info(principal.getName());
//        }

        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/home";
        }

        return "user/login";
    }

    @GetMapping("/sign-up")
    public String signUpForm() {

        return "user/sign-up";
    }

}
