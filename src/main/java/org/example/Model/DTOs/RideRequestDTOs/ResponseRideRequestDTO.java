package org.example.Model.DTOs.RideRequestDTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.example.Model.DTOs.UserDTOs.ResponseUserDTO;

@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseRideRequestDTO {
    private Long requestId;
    private Long rideId;
    private String status;
    private ResponseUserDTO passenger;


}
