package org.example.myproject.error;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice()
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger(ErrorCode.class);

    private ErrorCode errorCode;

    // 비즈니스 로직을 쓸만한 곳이 어디일지 고민 필요. / 재사용성이 필요한 곳.

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException e) {

        Map<String, Object> body = new HashMap<>();

        body.put("code", e.getErrorCode());
        body.put("message", e.getMessage());


        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(body);
    }
}


