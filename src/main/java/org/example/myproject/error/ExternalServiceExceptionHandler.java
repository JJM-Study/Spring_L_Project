package org.example.myproject.error;


import org.springframework.http.HttpStatus;

public class ExternalServiceExceptionHandler extends RuntimeException {
    private String message;
    private HttpStatus status;

    // super() 가 뭐 나오는지 확인 후 본래 하고자 한 로직을 하자.
    public ExternalServiceExceptionHandler() {
        super();
    }


}
