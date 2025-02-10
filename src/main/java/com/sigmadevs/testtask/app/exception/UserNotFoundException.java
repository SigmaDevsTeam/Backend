package com.sigmadevs.testtask.app.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Object value, String parameter) {
        super("User with " + parameter + ": " + value.toString() + " not found");
    }
}
