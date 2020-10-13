package com.aredzo.mtracker.sso.controller;


import com.aredzo.mtracker.sso.dto.ErrorResponse;
import com.aredzo.mtracker.sso.exception.SsoServiceError;
import com.aredzo.mtracker.sso.exception.SsoServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionController {

    private static final Logger log = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(SsoServiceException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleServiceException(SsoServiceException e) {
        log.error("Exception thrown: " + e.toString());
        return ResponseEntity
                .status(e.getError().getCode())
                .body(new ErrorResponse(e.getError()));
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class,
            MissingRequestHeaderException.class,
            MethodArgumentTypeMismatchException.class,
            HttpRequestMethodNotSupportedException.class,
            MissingServletRequestParameterException.class
    })
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleBadRequestErrors(Exception exception, HttpServletRequest request) {
        log.warn(exception.toString());
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(SsoServiceError.BAD_REQUEST));
    }

}
