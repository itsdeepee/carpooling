package org.example.Exceptions.RideRequest.CustomExceptions;

public class DuplicateRideRequestException extends RuntimeException {
    public DuplicateRideRequestException(String message) {
        super(message);
    }
}
