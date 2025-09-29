package org.example.myproject.home.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private static final Logger logger = LogManager.getLogger(HomeController.class);

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("layoutBody", "/WEB-INF/jsp/home.jsp");

        return "layout/main-layout";
    }

}
