package org.example.Model;

import jakarta.validation.constraints.NotNull;

/**
 * Passenger's request to join an available ride.
 */
public class RideRequest {

    private long rideRequestID;
    private Long rideID;
    private long passengerID;
    private RideRequestStatus status;
}
