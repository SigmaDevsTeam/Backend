package com.sigmadevs.testtask.security.exception;

public class ExpiredJwtException extends RuntimeException{
    public ExpiredJwtException() {
        super("Jwt expired");
    }

//    public ExpiredJwtException(String message) {
//        super(message);
//    }
}
