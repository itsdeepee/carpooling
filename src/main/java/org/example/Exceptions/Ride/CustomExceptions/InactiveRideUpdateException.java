package org.example.Exceptions.Ride.CustomExceptions;

public class InactiveRideUpdateException extends RuntimeException{

    public InactiveRideUpdateException(String message){
        super(message);
    }
}
