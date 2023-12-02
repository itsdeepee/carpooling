package org.example.Model.DTOs.RideDTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Model.DTOs.DriverDTOs.SimplifiedDriverDTO;
import org.example.Model.DTOs.Location.LocationDTO;
import org.example.Model.DTOs.UserDTOs.ResponseUserDTO;
import org.example.Model.RideRequestStatus;


import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResponseRideDTO {
    @NotNull(message = "Departure location cannot be empty")
    private LocationDTO departureLocation;
    @NotNull(message = "Destination location cannot be empty")
    private LocationDTO destinationLocation;
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm")
    @NotNull(message = "Date and time cannot be empty")
    @Future(message = "Date and time should be in the future")
    @Schema(description = "Date of ride", type = "date", pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateAndTimeOfRide;
    private String status;
    @Min(value = 1, message = "Available seats cannot be less than 1.")
    private int availableSeats;
    private SimplifiedDriverDTO driver;
    private List<ResponseUserDTO> passengers;
    private double cost;
    private List<String> additionalDetails;
}
