package org.example.Model.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.Model.Location;
import org.springframework.format.annotation.DateTimeFormat;

import javax.print.attribute.standard.DateTimeAtProcessing;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class RideDTO {

    @NotNull(message = "Departure location cannot be empty")
    private Location departureLocation;

    @NotNull(message = "Destination location cannot be empty")
    private Location destinationLocation;


    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm")
    @NotNull(message = "Date and time cannot be empty")
    @Future(message="Date and time should be in the future")
    @Schema(description = "Date of ride", type = "date", pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateAndTimeOfRide;

    @Min(value = 1,message = "Available seats cannot be less than 1.")
    private int availableSeats;


    private Long driverId;


    private Set<UserDTO> passengers; //passengers in the ride

    private double cost;
    private List<String> additionalDetails;

}
