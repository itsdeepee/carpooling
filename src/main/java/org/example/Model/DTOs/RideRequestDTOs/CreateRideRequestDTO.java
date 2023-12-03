package org.example.Model.DTOs.RideRequestDTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) representing details required to create a ride request.
 * Includes user ID, where userId must not be null, and ride ID for ride creation purposes.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateRideRequestDTO {
    @NotNull
    private Long userId;  // ID of the user associated with the ride request, must not be null
    private Long rideId;  // ID of the ride for which the request is made
}