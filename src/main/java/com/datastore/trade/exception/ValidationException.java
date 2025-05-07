package com.datastore.trade.exception;

public class ValidationException extends RuntimeException{

    public ValidationException(String message){
        super(message);
        System.out.println(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        System.out.println(cause);
    }

}
