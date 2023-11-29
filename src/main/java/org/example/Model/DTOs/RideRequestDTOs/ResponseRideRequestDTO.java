package org.example.Model.DTOs.RideRequestDTOs;

import lombok.*;
import org.example.Model.DTOs.UserDTOs.ResponseUserDTO;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ResponseRideRequestDTO {
    private Long requestId;
    private Long rideId;
    private String status;
    private ResponseUserDTO passenger;


}
