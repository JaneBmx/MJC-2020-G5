package com.epam.esm.exception;

public class RequestParamsNotValidException extends RuntimeException {
    public RequestParamsNotValidException(String message) {
        super(message);
    }
}
