package org.example.Model.DTOs.RideRequestDTOs;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateRideRequestDTO {
    private Long userId;  // ID of the user associated with the ride request, must not be null
    private Long requestId;
}
