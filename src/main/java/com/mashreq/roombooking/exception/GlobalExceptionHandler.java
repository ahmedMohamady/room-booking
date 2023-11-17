package com.mashreq.roombooking.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(RoomBookingException.class)
    protected ResponseEntity<Object> handleRoomBookingException(RoomBookingException ex) {
        return ResponseEntity.status(ex.getErrorDetails().getStatus()).body(ex.getErrorDetails());
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        ErrorDetails error =
                new ErrorDetails(HttpStatus.BAD_REQUEST, "Validation failed", errors);
        return handleExceptionInternal(
                ex, error, headers, error.getStatus(), request);
    }


    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatusCode status, WebRequest request) {
        String errorMessage = ex.getParameterName() + " parameter is missing";

        ErrorDetails error =
                new ErrorDetails(HttpStatus.BAD_REQUEST, errorMessage, null);
        return handleExceptionInternal(
                ex, error, headers, error.getStatus(), request);
    }
}




