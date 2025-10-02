package org.example.myproject.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 비즈니스 로직을 쓸만한 곳이 어디일지 고민 필요. / 재사용성이 필요한 곳.

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, Object>> ProductNotFoundException(String string) {
        string

        return ResponseEntity.ok();
    }

    public enum ErrorCode {
        PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "주문 상품이 존재하지 않습니다.");

        private final HttpStatus status;
        private final String defaultMessage;




    }

}


