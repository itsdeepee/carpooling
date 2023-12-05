package org.example.Model.DTOs.RideDTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseRideDTO {

    @NotNull(message = "{rideId.notNull}")
    private Long rideId;

    @NotNull(message = "{departureLocation.notNull}")
    private LocationDTO departureLocation;

    @NotNull(message = "{destinationLocation.notNull}")
    private LocationDTO destinationLocation;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Schema(description = "Date of ride", type = "date", pattern = "yyyy-MM-dd HH:mm")
    @NotNull(message ="{dateAndTimeOfRide.notNull}")
    @Future(message = "{dateAndTimeOfRide.future}")
    private LocalDateTime dateAndTimeOfRide;


    private String status;

    @Min(value = 1, message = "{availableSeats.min}")
    private int availableSeats;

    private SimplifiedDriverDTO driver;

    private List<ResponseUserDTO> passengers;
    private double cost;
    private List<String> additionalDetails;
}
