package org.example.Controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.Model.DTOs.CustomResponseDTO;
import org.example.Model.DTOs.RideDTO;
import org.example.Model.RideRequest;
import org.example.Model.RideRequestStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path="/requests")
@Tag(name = "Ride Request Controller",
        description = "This REST controller provides services to manage ride request of passages to specific rides")
public class RideRequestController {

//    @PostMapping
//    public ResponseEntity<CustomResponseDTO> requestRide(@RequestBody @Valid RideRequest rideRequest, BindingResult bindingResult) {
//        return null;
//    }
//
//    @GetMapping
//    public ResponseEntity<CustomResponseDTO> getAllRequests(){
//       return null;
//    }
}
