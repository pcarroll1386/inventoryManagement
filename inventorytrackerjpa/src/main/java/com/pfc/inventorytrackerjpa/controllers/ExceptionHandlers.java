package com.pfc.inventorytrackerjpa.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.pfc.inventorytrackerjpa.services.InvalidCategoryException;
import com.pfc.inventorytrackerjpa.services.InvalidDataException;
import com.pfc.inventorytrackerjpa.services.InvalidItemException;
import com.pfc.inventorytrackerjpa.services.InvalidLocationException;
import com.pfc.inventorytrackerjpa.services.InvalidRoleException;
import com.pfc.inventorytrackerjpa.services.InvalidUserException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(InvalidCategoryException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCategoryException(InvalidCategoryException ex, WebRequest request){
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(InvalidLocationException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidLocationException(InvalidLocationException ex, WebRequest request){
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(InvalidItemException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidItemException(InvalidItemException ex, WebRequest request){
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidRoleException(InvalidRoleException ex, WebRequest request){
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidUserException(InvalidUserException ex, WebRequest request){
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidDataException(InvalidDataException ex, WebRequest request){
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        Map<String, String> details = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            details.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return build(HttpStatus.BAD_REQUEST, "Validation failed.", request, details);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            WebRequest request) {
        return build(HttpStatus.BAD_REQUEST, "Malformed request body.", request, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            WebRequest request) {
        return build(HttpStatus.CONFLICT, "Data integrity violation.", request, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnhandledException(Exception ex, WebRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error.", request, null);
    }

    private ResponseEntity<ApiErrorResponse> build(
            HttpStatus status,
            String message,
            WebRequest request,
            Map<String, String> details) {

        String path = request instanceof ServletWebRequest
                ? ((ServletWebRequest) request).getRequest().getRequestURI()
                : "unknown";

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                details
        );

        return new ResponseEntity<>(response, status);
    }
}
