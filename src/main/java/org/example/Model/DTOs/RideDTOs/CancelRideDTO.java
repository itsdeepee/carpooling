package org.example.Model.DTOs.RideDTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CancelRideDTO {

    @NotNull(message = "{userId.notNull}")
    @Positive(message = "{userId.positive}")
    private Long userId;

    @NotNull(message = "{rideId.notNull}")
    @Positive(message = "{rideId.positive}")
    private Long rideId;
}
