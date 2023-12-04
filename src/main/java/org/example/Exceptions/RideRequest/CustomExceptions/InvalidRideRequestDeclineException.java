package org.example.Exceptions.RideRequest.CustomExceptions;

public class InvalidRideRequestDeclineException extends RuntimeException{

    public InvalidRideRequestDeclineException(String message){
        super(message);
    }
}
