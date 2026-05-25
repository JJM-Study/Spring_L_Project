package org.example.myproject.error;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
public class ExternalServiceExceptionHandler extends RuntimeException {
    private final ErrorCode errorCode;

    // super() 가 뭐 나오는지 확인 후 본래 하고자 한 로직을 하자.
    public ExternalServiceExceptionHandler(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;


    }

    public ExternalServiceExceptionHandler(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }


}
