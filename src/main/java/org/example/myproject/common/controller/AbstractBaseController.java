package org.example.myproject.common.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.auth.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


public abstract class AbstractBaseController {

    private static final Logger logger = LogManager.getLogger(AbstractBaseController.class);

    private static final String MAIN_LAYOUT_PATH = "layout/main-layout";

//    @Autowired
//    private HttpServletRequest request;

    public String layout(String contentPath, Model model) {

        model.addAttribute("layoutBody", "/WEB-INF/jsp" + contentPath);
//        model.addAttribute("curUrl", request.getRequestURI());
        logger.info("abstract layout : {}", contentPath);
        return MAIN_LAYOUT_PATH;

    }
}
