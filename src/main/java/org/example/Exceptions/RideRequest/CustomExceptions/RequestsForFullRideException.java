package org.example.Exceptions.RideRequest.CustomExceptions;

public class RequestsForFullRideException extends RuntimeException{

    public RequestsForFullRideException(String message){
        super(message);
    }
}
