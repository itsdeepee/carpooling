package org.example.Controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;


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
