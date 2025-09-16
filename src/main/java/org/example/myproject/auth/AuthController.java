package org.example.myproject.auth;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

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
        System.out.printf("Authentication object is null");
        return "user/login";
    }

    @GetMapping("/sign-up")
    public String signUpForm() {

        return "user/sign-up";
    }


    @PostMapping("/sign-up/")
    public String signUpUser(
            AuthDTO authDTO,
            String checkPassword,
            RedirectAttributes redirectAttributes
    ) {
        if (!authDTO.getPassword().equals(checkPassword)) {

            logger.error("wrong password");

            redirectAttributes.addFlashAttribute("message", "패스워드가 일치하지 않습니다.");
            return redirectAttributes
        }

        boolean result = authService.signUpUser(authDTO);

        if (result) {
            return
        }
    }

}
