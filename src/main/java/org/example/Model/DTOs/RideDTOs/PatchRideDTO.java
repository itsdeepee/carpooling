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
import org.example.Model.DTOs.Location.LocationDTO;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PatchRideDTO {

    @Min(value = 1, message = "Available seats cannot be less than 1.")
    private int availableSeats;

    private double cost;
    private List<String> additionalDetails;

}
