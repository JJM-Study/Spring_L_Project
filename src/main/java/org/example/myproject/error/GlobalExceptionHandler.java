package org.example.myproject.error;

import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.NonFinal;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String commonErrorHandler(Model model, HttpServletResponse response) {
        model.addAttribute("layoutBody", "/WEB-INF/jsp/error/common-error.jsp");
        model.addAttribute("status", response.getStatus());
        return "layout/main-layout";
    }

    @ExceptionHandler(NotFoundException.class)
    public String NoHandlerFoundException(Model model){
        model.addAttribute("layoutBody", "/WEB-INF/jsp/error/404.jsp");
       return "layout/main-layout";
    }
}
