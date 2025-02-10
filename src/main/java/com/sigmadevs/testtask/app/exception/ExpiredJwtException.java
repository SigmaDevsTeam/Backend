package com.sigmadevs.testtask.app.exception;

public class ExpiredJwtException extends RuntimeException{
    public ExpiredJwtException() {
        super("Jwt expired");
    }

//    public ExpiredJwtException(String message) {
//        super(message);
//    }
}
