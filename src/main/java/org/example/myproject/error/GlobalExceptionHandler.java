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
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.servlet.ModelAndView;
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
    public ModelAndView handleUnhandledException(Exception e, HttpServletRequest request) {

        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Throwable throwable = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        String reason = throwable.getMessage();

        int status = 500;

        ModelAndView mav = new ModelAndView("layout/main-layout");

        mav.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        mav.addObject("reason", e.getMessage());
        mav.addObject("layoutBody", "/WEB-INF/jsp/error/404.jsp");

        logger.error("=== request error log ===");
        logger.error("Request URI : {}", request.getRequestURI());
        logger.error("Reason : {}", reason);
        logger.error("status code : {}", statusCode);
        logger.error("==========");

//        if (statusCode)
//
//        if ( == 404){
//
//        }
//        else {
//
//        }


        // 여기 /error 즉, vieExceptionHandler를 구현하자.
        return mav;
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


