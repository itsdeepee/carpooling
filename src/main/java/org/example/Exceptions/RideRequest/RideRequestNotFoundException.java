package org.example.Exceptions.RideRequest;

public class RideRequestNotFoundException extends RuntimeException{

    public RideRequestNotFoundException(String message){
        super(message);
    }
}
