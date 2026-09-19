package com.prajjaval.urlshortener.exception;

public class ExpiredUrlException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ExpiredUrlException(String message) {
        super(message);
    }
}