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

    //
    STOCK_NOT_ENOUGH("S001", "상품의 재고가 없습니다.", HttpStatus.BAD_REQUEST),

    STOCK_NOT_ENOUGH_DETAIL("S002", "다음 상품들의 재고가 부족합니다: {0}", HttpStatus.BAD_REQUEST),

    // 인증
    REQUEST_LOGIN("A001", "로그인이 필요합니다. (커스텀 Business 예외)", HttpStatus.UNAUTHORIZED),

    UNAUTHORIZED_ACCESS("A002", "해당 리소스에 접근할 권한이 없습니다.", HttpStatus.FORBIDDEN);

    private final String code;
    private final String message;
//    private final HttpStatus httpStatus;
    private final HttpStatus status;


}
