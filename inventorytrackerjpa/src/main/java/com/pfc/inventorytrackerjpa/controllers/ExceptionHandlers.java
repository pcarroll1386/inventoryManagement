package com.pfc.inventorytrackerjpa.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.pfc.inventorytrackerjpa.services.InvalidCategoryException;
import com.pfc.inventorytrackerjpa.services.InvalidDataException;
import com.pfc.inventorytrackerjpa.services.InvalidItemException;
import com.pfc.inventorytrackerjpa.services.InvalidLocationException;
import com.pfc.inventorytrackerjpa.services.InvalidRoleException;
import com.pfc.inventorytrackerjpa.services.InvalidUserException;

@ControllerAdvice
@RestController
public class ExceptionHandlers {

    @ExceptionHandler(InvalidCategoryException.class)
    public ResponseEntity<Error> handleInvalidCategoryException(InvalidCategoryException ex, WebRequest request){
        String message = ex.getMessage();
        Error err = new Error(message);
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidLocationException.class)
    public ResponseEntity<Error> handleInvalidLocationException(InvalidLocationException ex, WebRequest request){
        String message = ex.getMessage();
        Error err = new Error(message);
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidItemException.class)
    public ResponseEntity<Error> handleInvalidItemException(InvalidItemException ex, WebRequest request){
        String message = ex.getMessage();
        Error err = new Error(message);
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<Error> handleInvalidRoleException(InvalidRoleException ex, WebRequest request){
        String message = ex.getMessage();
        Error err = new Error(message);
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<Error> handleInvalidUserException(InvalidUserException ex, WebRequest request){
        String message = ex.getMessage();
        Error err = new Error(message);
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<Error> handleInvalidDataException(InvalidDataException ex, WebRequest request){
        String message = ex.getMessage();
        Error err = new Error(message);
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

}
