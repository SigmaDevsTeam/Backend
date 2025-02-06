package com.sigmadevs.testtask.security.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Object value, String parameter) {
        super("User with " + parameter + ": " + value.toString() + " not found");
    }
}
