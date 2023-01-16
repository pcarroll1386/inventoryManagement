package com.pfc.inventorytrackerjpa.controllers;

import com.pfc.inventorytrackerjpa.service.InvalidCategoryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@RestController
public class ExceptionHandlers {

    @ExceptionHandler(InvalidCategoryException.class)
    public ResponseEntity<Error> handleInvalidCategoryException(InvalidCategoryException ex, WebRequest request){
        String message = ex.getMessage();
        Error err = new Error(message);
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

}
