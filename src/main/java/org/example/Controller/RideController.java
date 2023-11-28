package org.example.Controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.Model.DTOs.CustomResponseDTO;
import org.example.Model.DTOs.RideDTOs.CreateRideDTO;
import org.example.Model.DTOs.RideDTOs.ResponseRideDTO;
import org.example.Service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/rides")
@Tag(name = "Ride Controller", description = "This REST controller" +
        "provides services to manage rides in the Carpooling application")
public class RideController {

    private RideService rideService;

    @Autowired
    RideController(RideService rideService) {
        this.rideService = rideService;
    }

//    //get available rides
//    @GetMapping
//    @Operation(
//            summary = "Get a list of rides",
//            description = "Retrieves all rides in the Carpooling application.",
//            responses = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "Successfully retrieved"
//                    )
//            })
//    public List<RideDTO> getAllRides() {
//        return this.rideService.findAll();
//    }


    @PostMapping("/{userId}/createRide")
    @Operation(summary = "Creates a new ride in the Carpooling application.",
            description = "Returns the new created ride.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Ride created successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Wrong request body format"
                    )
            })
    public ResponseEntity<CustomResponseDTO> createNewRide(@Valid @RequestBody CreateRideDTO createRideDTO, @PathVariable Long userId) {

        CustomResponseDTO customResponseDTO = new CustomResponseDTO();

        ResponseRideDTO responseRideDTO=rideService.createRide(createRideDTO,userId);
        customResponseDTO.setResponseObject(responseRideDTO);
        customResponseDTO.setResponseMessage("Ride created successfully");
        return new ResponseEntity<>(customResponseDTO, HttpStatus.CREATED);


    }


}
