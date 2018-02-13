package com.example;

public class IllegalMeldModificationException extends RuntimeException {
    protected IllegalMeldModificationException() {
        super();
    }

    protected IllegalMeldModificationException(String errorMessage) {
        super(errorMessage);
    }
}
