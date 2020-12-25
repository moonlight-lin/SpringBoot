package com.example.demo.exception;

public class DemoException extends RuntimeException {
    private final String code;
    private final String message;

    public DemoException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
