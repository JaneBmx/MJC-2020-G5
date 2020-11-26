package com.epam.esm.exception;

public class ServiceException extends RuntimeException {
    public ServiceException(Throwable cause) {
        super(cause);
    }
}
