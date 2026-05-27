package org.example.myproject.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@Controller
public class ViewExceptionHandler implements ErrorController {

    private static final Logger logger = LogManager.getLogger(ViewExceptionHandler.class);

    //https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-servlet/exceptionhandlers.html#mvc-ann-customer-servlet-container-error-page

//    @ExceptionHandler(Exception.class)
//    public String commonErrorHandler(Model model, HttpServletResponse response) {
//        model.addAttribute("layoutBody", "/WEB-INF/jsp/error/common-error.jsp");
//        model.addAttribute("status", response.getStatus());
//        return "layout/main-layout";
//    }
//
//    @ExceptionHandler(NoHandlerFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public String handle404(Model model, HttpServletResponse response){
//        model.addAttribute("layoutBody", "/WEB-INF/jsp/error/404.jsp");
//        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
//        logger.info("404 status : " + response.getStatus());
//
//       return "layout/main-layout";
//    }


    // https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-servlet/exceptionhandlers.html#mvc-ann-customer-servlet-container-error-page
//    @ExceptionHandler(Exception.class)
//    public String commonExceptionHandler(Exception exception, Model model, HttpServletResponse response, HttpServletRequest request){
//        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//
//        logger.info("statusCode : " + statusCode);
//
//        int status = response.getStatus();
//        String reason = exception.getMessage();
//        model.addAttribute("status", status);
//        model.addAttribute("reason", reason);
//
//
//        if (status == 404) {
//            model.addAttribute("status", status);
//            model.addAttribute("layoutBody", "/WEB-INF/jsp/error/error.jsp");
//        } else {
//            model.addAttribute("layoutBody", "/WEB-INF/jsp/error/common-error.jsp");
//        }
//
//        return "layout/main-layout";
//    }


    @RequestMapping("/error")
    public String commonExceptionHandler(Model model, HttpServletResponse response, HttpServletRequest request){
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        Throwable throwable = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        String reason = (throwable) != null ? throwable.getMessage() : null;

        logger.info("view statusCode : " + statusCode);
//        logger.info("throwable :" + throwable);
        logger.info("reason view response error: " + reason);
        Object message = request.getAttribute("org.springframework.web.util.WebUtils.ERROR_MESSAGE_ATTRIBUTE");
        //404 에러 처리가 제대로 안 되므로 에러 처리 흐름을 이해하고 그에 따라 처리하도록 수정할 것.


//        int status = 500; // 이게 servletResponse 에도 같은 논리로 적용 되더라.
        int status = (response.getStatus() > 0) ? response.getStatus() : 500;
        logger.info("view Error Message: {}", message);

        if (statusCode != null) {

            try {

                status = (int) statusCode;

            } catch(Exception e) {
                e.printStackTrace();
            }
        }

            model.addAttribute("status", status);
            model.addAttribute("reason", reason);

            if (status == 404) {
//                model.addAttribute("status", status);
                model.addAttribute("layoutBody", "/WEB-INF/jsp/error/404.jsp");
            } else {
                model.addAttribute("layoutBody", "/WEB-INF/jsp/error/common-error.jsp");
            }

        return "layout/main-layout";
    }

}
