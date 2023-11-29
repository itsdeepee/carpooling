package org.example.Controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.Model.DTOs.CustomResponseDTO;
import org.example.Model.DTOs.RideRequestDTOs.CreateRideRequestDTO;
import org.example.Model.DTOs.RideRequestDTOs.ResponseRideRequestDTO;
import org.example.Model.Entities.RideRequestEntity;
import org.example.Service.RideRequestService;
import org.example.Service.RideService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@Tag(name = "Ride Request Controller",
        description = "This REST controller provides services to manage ride request of passages to specific rides")
public class RideRequestController {

    private final RideRequestService rideRequestService;
    private final RideService rideService;

    public RideRequestController(RideRequestService rideRequestService,
                                 RideService rideService  ){
        this.rideRequestService=rideRequestService;
        this.rideService=rideService;
    }

    @PostMapping(path="/rides/{rideId}/requests")
    public List<String> makeRideRequest(@PathVariable Long rideId, @RequestBody @Valid CreateRideRequestDTO createRideRequestDTO){
        rideRequestService.requestRide(rideId,createRideRequestDTO);
        return new ArrayList<>();
    }


}
