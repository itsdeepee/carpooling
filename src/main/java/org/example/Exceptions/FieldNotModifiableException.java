package org.example.Exceptions;

public class FieldNotModifiableException extends RuntimeException {
    public FieldNotModifiableException(String message) {
        super(message);
    }
}
