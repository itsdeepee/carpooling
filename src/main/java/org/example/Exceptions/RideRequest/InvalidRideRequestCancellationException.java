package org.example.Exceptions.RideRequest;

public class InvalidRideRequestCancellationException extends RuntimeException{

    public InvalidRideRequestCancellationException(String message){
        super(message);
    }
}
