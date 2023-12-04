package org.example.Exceptions.RideRequest.CustomExceptions;

public class InvalidRideRequestAcceptException extends RuntimeException{

    public InvalidRideRequestAcceptException(String message){
        super(message);
    }
}
