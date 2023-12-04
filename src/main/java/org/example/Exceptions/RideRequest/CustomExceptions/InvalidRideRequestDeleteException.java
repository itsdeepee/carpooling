package org.example.Exceptions.RideRequest.CustomExceptions;

public class InvalidRideRequestDeleteException extends RuntimeException{

    public InvalidRideRequestDeleteException(String message){
        super(message);
    }
}
