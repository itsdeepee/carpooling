package org.example.Model.DTOs.DriverDTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SimplifiedDriverDTO {
    @NotNull
    private Long id;
    @NotNull
    private String fullName;
    @NotNull
    private String phoneNo;
    @NotNull
    @Email
    private String email;
    @NotBlank(message = "Driver license number is required")
    private String driverLicenseNumber;
    @NotBlank(message = "Vehicle type is required")
    private String vehicleType;


}
