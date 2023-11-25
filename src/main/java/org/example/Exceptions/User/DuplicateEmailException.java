package org.example.Exceptions.User;

public class DuplicateEmailException extends RuntimeException{
    public DuplicateEmailException(String message){
        super(message);
    }
}
