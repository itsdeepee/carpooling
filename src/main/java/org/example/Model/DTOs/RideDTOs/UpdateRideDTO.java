package org.example.Model.DTOs.RideDTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UpdateRideDTO {
    @NotNull(message = "{rideId.notNull}")
    private Long rideId;

    @NotNull(message = "{userId.notNull}")
    private Long userId;

    private CreateRideDTO ride;
}
