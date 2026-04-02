package com.pfc.inventorytrackerjpa.services;

public class InvalidCategoryException extends Exception{

    public InvalidCategoryException(String message){
        super(message);
    }

   public InvalidCategoryException(String message, Throwable cause){
        super(message, cause);
   }
}

