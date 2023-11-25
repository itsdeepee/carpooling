package org.example.Exceptions.User;

public class UserCreationException extends RuntimeException {
    public UserCreationException(String message) {
        super(message);
    }
}