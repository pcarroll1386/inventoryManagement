package com.pfc.inventorytrackerjpa.services;

public class InvalidRoleException extends Exception {
    public InvalidRoleException(String message) {
        super(message);
    }

    public InvalidRoleException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
