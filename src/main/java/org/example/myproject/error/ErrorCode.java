package org.example.myproject.error;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // MES 실무 당시 봤던 에러 코드 도입.

    // 주문
    PRODUCT_ORDER_NOT_FOUND("O001", "주문 상품이 비어 있습니다.",  HttpStatus.NOT_FOUND),


    // 인증
    REQUEST_LOGIN("A001", "로그인이 필요합니다. (커스텀 Business 예외)", HttpStatus.UNAUTHORIZED);

    private final String code;
    private final String message;
//    private final HttpStatus httpStatus;
    private final HttpStatus status;


}
