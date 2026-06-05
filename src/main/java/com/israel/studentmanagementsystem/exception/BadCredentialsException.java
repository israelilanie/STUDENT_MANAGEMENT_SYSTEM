package com.israel.studentmanagementsystem.exception;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException() {
        super("Invalid email or password");
    }
}
