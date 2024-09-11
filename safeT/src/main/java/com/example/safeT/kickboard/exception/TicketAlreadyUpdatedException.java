package com.example.safeT.kickboard.exception;

public class TicketAlreadyUpdatedException extends RuntimeException {
    public TicketAlreadyUpdatedException(String message) {
        super(message);
    }
}