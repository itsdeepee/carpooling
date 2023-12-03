package org.example.Exceptions.RideRequest;

public class DuplicateRideRequestException extends RuntimeException {
    public DuplicateRideRequestException(String message) {
        super(message);
    }
}
