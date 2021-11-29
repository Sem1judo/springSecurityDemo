package com.example.springsecuritydemo.exception;

public class NoSuchEntityException extends RuntimeException {
    public NoSuchEntityException() {
    }
    public NoSuchEntityException(String message) {
        super(message);
    }

    public NoSuchEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
