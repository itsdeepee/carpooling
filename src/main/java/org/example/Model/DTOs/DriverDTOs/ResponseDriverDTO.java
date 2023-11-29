package org.example.Model.DTOs.DriverDTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.example.Model.DTOs.UserDTOs.ResponseUserDTO;
import org.example.Model.Entities.LocationEntity;
import org.example.Model.Entities.RideEntity;

import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ResponseDriverDTO {
    private ResponseUserDTO responseUserDTO;
    @NotBlank(message = "Driver license number is required")
    private String driverLicenseNumber;
    @NotBlank(message = "Vehicle type is required")
    private String vehicleType;
    private Set<RideEntity> rides;
    private Set<LocationEntity> recentAddresses;
}
