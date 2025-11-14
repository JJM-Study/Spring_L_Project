package org.example.myproject.common.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.auth.service.CustomUserDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


public abstract class AbstractBaseController {

    private static final Logger logger = LogManager.getLogger(AbstractBaseController.class);

    private static final String MAIN_LAYOUT_PATH = "layout/main-layout";

    public String layout(String contentPath, Model model) {

        model.addAttribute("layoutBody", "/WEB-INF/jsp" + contentPath);
        logger.info("abstract layout : {}", contentPath);
        return MAIN_LAYOUT_PATH;

    }
}
