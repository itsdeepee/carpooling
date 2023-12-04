package org.example.Exceptions.RideRequest.CustomExceptions;

public class IllegalStatusException extends RuntimeException{

    public IllegalStatusException(String message){
        super(message);
    }
}
