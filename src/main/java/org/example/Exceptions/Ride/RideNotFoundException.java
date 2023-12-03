package org.example.Exceptions.Ride;

    public class RideNotFoundException extends RuntimeException{
        public RideNotFoundException(String message){
            super(message);
        }
    }

