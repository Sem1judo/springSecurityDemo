package com.example.springsecuritydemo.exception;

public class InvalidOldPasswordException extends RuntimeException {
    public InvalidOldPasswordException() {
        super();
    }

    public InvalidOldPasswordException(String message) {
        super(message);
    }

    public InvalidOldPasswordException(String message, Throwable cause) {
        super(message, cause);
    }


}
