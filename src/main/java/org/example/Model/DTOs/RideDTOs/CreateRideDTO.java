package org.example.Model.DTOs.RideDTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import org.example.Model.DTOs.Location.LocationDTO;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CreateRideDTO {

    @NotNull(message = "{userId.notNull}")
    private Long userId;

    @NotNull(message = "{departureLocation.notNull}")
    private LocationDTO departureLocation;

    @NotNull(message = "{destinationLocation.notNull}")
    private LocationDTO destinationLocation;

    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm")
    @Schema(description = "Date of ride", type = "date", pattern = "yyyy-MM-dd HH:mm")
    @NotNull(message ="{dateAndTimeOfRide.notNull}")
    @Future(message = "{dateAndTimeOfRide.future}")
    private LocalDateTime dateAndTimeOfRide;

    @Min(value = 1, message = "{availableSeats.min}")
    private int availableSeats;

    @PositiveOrZero(message = "{cost.positiveOrZero}")
    private double cost;

    @Size(max = 10, message = "{additionalDetails.size}")
    private List<String> additionalDetails;
}
