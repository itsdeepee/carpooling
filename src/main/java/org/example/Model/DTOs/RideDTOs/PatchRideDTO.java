package org.example.Model.DTOs.RideDTOs;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

/**
 * DTO class representing the patch request for a ride entity.
 * Contains information about the ride's user ID, ride ID, available seats, cost, and additional details.
 * Provides validation constraints for ensuring data integrity in the patch request.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PatchRideDTO {

    @NotNull(message = "{userId.notNull}")
    @Positive(message = "{userId.positive}")
    private Long userId;

    @NotNull(message = "{rideId.notNull}")
    @Positive(message = "{rideId.positive}")
    private Long rideId;

    @PositiveOrZero(message="{availableSeats.positiveOrZero}")
    private int availableSeats;

    @PositiveOrZero(message = "{cost.positiveOrZero}")
    private double cost;

    @Size(max = 10, message = "{additionalDetails.size}")
    private List<String> additionalDetails;

}
