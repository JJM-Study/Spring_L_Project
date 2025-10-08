package org.example.myproject.error;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private ErrorCode errorCode;


    // 오버로드 이용해서, 추가 기능 필요할 때 추가하는 방법 생각.
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
