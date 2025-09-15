package org.example.myproject.home;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.user.UserController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private static final Logger logger = LogManager.getLogger(HomeController.class);

    @GetMapping("/home")
    public String home() {
        return "home";
    }

}
