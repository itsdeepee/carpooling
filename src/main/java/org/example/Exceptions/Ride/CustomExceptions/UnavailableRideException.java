package org.example.Exceptions.Ride.CustomExceptions;

public class UnavailableRideException extends RuntimeException{

    public UnavailableRideException(String message){
        super(message);
    }
}
