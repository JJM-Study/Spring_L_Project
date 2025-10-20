package org.example.myproject.auth.controller;

import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.example.myproject.auth.dto.AuthDTO;
import org.example.myproject.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

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
    public String loginPage(Authentication authentication, Model model, HttpSession httpSession) {


        String errorMessage = (String) httpSession.getAttribute("errorMessage");
        logger.info("errorMessage" + errorMessage);
//        model.addAttribute("user", principal.getName());

//        if (principal.getName() != null) {
//            logger.info(principal.getName());
//        }
//        if (Model)


        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/home";
        }

        if(errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            httpSession.removeAttribute("errorMessage");
        }

        System.out.println("Authentication object is null");
        return "auth/login";
    }

    @PostMapping("/exist-id")
    @ResponseBody
    public Map<String, Object> existId(@RequestBody Map<String, String> userdata) {
        Map<String, Object> response = new HashMap<>();
         String username = userdata.get("username");
         String result = authService.existId(username);

         response.put("username", result);
         logger.info("username" + result);
        return response;
    }

    @GetMapping("/sign-up")
    public String signUpForm() {

        return "auth/sign-up";
    }



    @PostMapping("/sign-up")
    @ResponseBody
    public Map<String, Object> signUpUser(
            @RequestBody AuthDTO authDTO
    ) {


        logger.info("SIGN-UP CONNECTION TEST");

        /* 실무에서 쓰던 방식대로 json으로 success, message 를 나눠서 통신하겠음. 차후 전역적으로 이걸 처리할 수 있는 handler 구현 고민.*/
        Map<String, Object> result = new HashMap<>();

//        if (!singUpDTO.getPassword().equals(singUpDTO.getCheckPassword())) {
//
//            logger.error("wrong password");
//
//            result.put("success", false);
//            result.put("message", "패스워드가 일치하지 않습니다.");
//            return result;
//        }

        try {

            boolean signUpResult = authService.signUpUser(authDTO);

            if (!signUpResult) {
                logger.error("failed sigh-up");
                result.put("success", false);
                result.put("message", "회원 가입이 실패했습니다.");
                return result;
            }

            logger.error("sigh-up success");
            result.put("success", true);
            result.put("message", "회원 가입이 완료되었습니다.");
            return result;

        } catch(Exception e) {
            logger.info("회원가입 중 예외 발생 : " + e);
            result.put("success", false);
            result.put("message", "회원가입 중 예외가 발생했습니다.");
            return result;
        }
    }

}
