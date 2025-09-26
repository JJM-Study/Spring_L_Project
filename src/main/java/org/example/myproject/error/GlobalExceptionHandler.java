package org.example.myproject.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.NonFinal;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.product.controller.ProductController;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler implements HandlerInterceptor {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    //https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-servlet/exceptionhandlers.html#mvc-ann-customer-servlet-container-error-page

    @ExceptionHandler(Exception.class)
    public String commonErrorHandler(Model model, HttpServletResponse response) {
        model.addAttribute("layoutBody", "/WEB-INF/jsp/error/common-error.jsp");
        model.addAttribute("status", response.getStatus());
        return "layout/main-layout";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle404(Model model, HttpServletResponse response){
        model.addAttribute("layoutBody", "/WEB-INF/jsp/error/404.jsp");
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
//        logger.info("404 status : " + response.getStatus());

       return "layout/main-layout";
    }
//
//
//    // https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-servlet/exceptionhandlers.html#mvc-ann-customer-servlet-container-error-page
//    @ExceptionHandler(Exception.class)
//    public String commonExceptionHandler(Exception exception, Model model, HttpServletResponse response){
//
//        int status = response.getStatus();
//        String reason = exception.getMessage();
//        model.addAttribute("status", status);
//        model.addAttribute("reason", reason);
//
//
//        if (status == 404) {
//            model.addAttribute("layoutBody", "/WEB-INF/jsp/error/error.jsp");
//        } else {
//            model.addAttribute("layoutBody", "/WEB-INF/jsp/error/common-error.jsp");
//        }
//
//        return "layout/main-layout";
//    }
}
