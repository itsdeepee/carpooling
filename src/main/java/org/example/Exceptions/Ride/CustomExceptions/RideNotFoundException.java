package org.example.Exceptions.Ride.CustomExceptions;

    public class RideNotFoundException extends RuntimeException{
        public RideNotFoundException(String message){
            super(message);
        }
    }

