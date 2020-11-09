package com.epam.esm.exception;

public class RequestParamsNotValidException extends RuntimeException {

    public RequestParamsNotValidException() {
    }

    public RequestParamsNotValidException(String message) {
        super(message);
    }

    public RequestParamsNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestParamsNotValidException(Throwable cause) {
        super(cause);
    }

    public RequestParamsNotValidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
