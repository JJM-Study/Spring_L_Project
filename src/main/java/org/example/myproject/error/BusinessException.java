package org.example.myproject.error;

import lombok.Getter;

import java.text.MessageFormat;

@Getter
public class BusinessException extends RuntimeException {

    private ErrorCode errorCode;


    // 오버로드 이용해서, 추가 기능 필요할 때 추가하는 방법 생각.
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, Object... params) {
        // ErrorCode의 메시지를 params로 포매팅하여 super(RuntimeException)에 전달
        super(MessageFormat.format(errorCode.getMessage(), params));
        this.errorCode = errorCode;
    }

}
