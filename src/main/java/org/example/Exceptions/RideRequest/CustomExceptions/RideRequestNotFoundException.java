package org.example.Exceptions.RideRequest.CustomExceptions;

public class RideRequestNotFoundException extends RuntimeException{

    public RideRequestNotFoundException(String message){
        super(message);
    }
}
