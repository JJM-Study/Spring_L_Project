package org.example.myproject.error;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

//@RestControllerAdvice
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

//    private ErrorCode errorCode;

    // 비즈니스 로직을 쓸만한 곳이 어디일지 고민 필요. / 재사용성이 필요한 곳.

    // 2026/05/25 전역 에러 로그 감지용 추가.

    @ExceptionHandler(Exception.class)
    public ModelAndView handleUnhandledException(Exception e, HttpServletRequest request, HttpServletResponse response) {

//        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//        Throwable throwable = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        int status = 500;


        if (e instanceof org.springframework.web.server.ResponseStatusException rse) {
            status = rse.getStatusCode().value();
        }

        response.setStatus(status);

        String reason = (e != null) ? e.getMessage() : "Unknown Error";

//
//        int status = statusCode != null ? (int) statusCode : 500;



        logger.error("=== request error log ===");
        logger.error("Request URI : {}", request.getRequestURI());
        logger.error("Reason      : {}", reason);
        logger.error("Status Code : {}", status);
        logger.error("Exception   : ", e);
        logger.error("==========");

        String filePath;

        filePath = "/WEB-INF/jsp/error/common-error.jsp";

        ModelAndView mav = new ModelAndView("layout/main-layout");

        mav.addObject("layoutBody", filePath);
        mav.addObject("status", status);
        mav.addObject("reason", reason);


        return mav;
    }

    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    public String handle404(HttpServletRequest request, HttpServletResponse response) {request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 404);
        response.setStatus(404);

        return "forward:/error";
    }

    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException e) {

        ErrorCode errorCode = e.getErrorCode();

        Map<String, Object> body = new HashMap<>();

        body.put("code", errorCode.getCode());
        body.put("message", errorCode.getMessage());
        
//        body.put("code", e.getErrorCode());
//        body.put("message", e.getMessage());

                body.put("status", errorCode.getStatus().value());

        return ResponseEntity
                //.status(e.getErrorCode().getStatus())
                .status(errorCode.getStatus())
                .body(body);
    }

    @ResponseBody
    @ExceptionHandler(ExternalServiceExceptionHandler.class)
    public ResponseEntity<String> ExternalServiceException(ExternalServiceExceptionHandler e) {

        String code = e.getErrorCode().getCode();
        String message = e.getErrorCode().getMessage();
        HttpStatus status = e.getErrorCode().getStatus();

        logger.info("External Error : " + e.getMessage());

        if (e.getCause() != null) {
            logger.error("Root Cause: {}", e.getCause());
        }

        return ResponseEntity.status(status).body("message :" + message + "code :" + code + "status :" + status);
    }
}


