package com.example.springsecuritydemo.exception;

public class NoSuchEntitiesException extends RuntimeException {
    public NoSuchEntitiesException() {
    }

    public NoSuchEntitiesException(String message) {
        super(message);
    }

    public NoSuchEntitiesException(String message, Throwable cause) {
        super(message, cause);
    }
}

