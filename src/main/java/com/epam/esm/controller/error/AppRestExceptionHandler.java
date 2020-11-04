package com.epam.esm.controller.error;

import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.ItemNotFoundException;
import com.epam.esm.exception.TagNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppRestExceptionHandler {
    private static final int TNF_CODE = 1404;
    private static final int BR_CODE = 9400;

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleTagNotFoundException(ItemNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        errorResponse.setCode(TNF_CODE);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleAdd(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        errorResponse.setCode(BR_CODE);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
