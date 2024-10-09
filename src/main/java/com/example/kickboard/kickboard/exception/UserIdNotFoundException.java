package com.example.kickboard.kickboard.exception;

public class UserIdNotFoundException extends RuntimeException{
    public UserIdNotFoundException(String message) {
        super(message);
    }
}
