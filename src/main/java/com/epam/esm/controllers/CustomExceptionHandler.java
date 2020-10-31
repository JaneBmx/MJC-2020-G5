package com.epam.esm.controllers;

import com.epam.esm.error.response.ErrorResponse;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.TagNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {
    private static final int TNF_CODE = 1231231;
    private static final int GNF_CODE = 112233;
    private static final int BR_CODE = 234567;

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleTagNotFoundException(TagNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse(TNF_CODE, e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleGiftCertificateNotFoundException(GiftCertificateNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse(GNF_CODE, e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleAdd(Exception e) {
        return new ResponseEntity<>(new ErrorResponse(BR_CODE, e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }
}
