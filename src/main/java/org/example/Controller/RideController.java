package org.example.Controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.Model.DTOs.CustomResponseDTO;
import org.example.Model.DTOs.RideDTOs.*;
import org.example.Service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Ride Controller", description = "This REST controller" +
        "provides services to manage rides in the Carpooling application")
@RequestMapping(path="/api/v1/rides")
public class RideController {

    private RideService rideService;

    @Autowired
    RideController(RideService rideService) {
        this.rideService = rideService;
    }
    @Operation(
            summary = "Retrieve all rides optionally filtered by start and end locations",
            description = "Fetches all rides or filters them based on start and end locations if provided."

    )
    @GetMapping("/all")
    public ResponseEntity<CustomResponseDTO> getAllRides(
            @Parameter(
                    name = "startLocation",
                    description = "Start location for filtering rides (optional)",
                    required = false
            )
            @RequestParam(value="startLocation",required = false) String startLocation,
            @Parameter(
                    name = "endLocation",
                    description = "End location for filtering rides (optional)",
                    required = false
            )
            @RequestParam(value="endLocation",required = false) String endLocation
            ){
        List<ResponseRideDTO> result=rideService.findAllRidesFiltered(startLocation,endLocation);
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        if(result.size()==0){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        customResponseDTO.setResponseObject(result);
        customResponseDTO.setResponseMessage(result.size()+" active rides found");
        return new ResponseEntity<>(customResponseDTO,HttpStatus.OK);
    }


    @Operation(
            summary="Get rides created by a driver",
            description = "Retrieve all rides created by a specific driver, optionally filtered by status."
    )
    @GetMapping("/{userId}")
    public ResponseEntity<CustomResponseDTO> getAllRidesCreatedByDriver(@PathVariable Long userId, @RequestParam(value="status",required = false) String status){
        List<ResponseRideDTO> result=rideService.getAllRidesCreatedByDriver(userId,status);
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        if(result.size()==0){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        customResponseDTO.setResponseObject(result);
        customResponseDTO.setResponseMessage("Successfully retrieved "+result.size()+" rides.");
        return new ResponseEntity<>(customResponseDTO,HttpStatus.OK);

    }




    @PostMapping
    @Operation(
            summary = "Create a new ride",
            description = "Creates a new ride in the Carpooling application."
            + " Provide the necessary details in the request body to create the ride."
    )
    public ResponseEntity<CustomResponseDTO> createNewRide(@Valid @RequestBody CreateRideDTO createRideDTO) {
        CustomResponseDTO customResponseDTO = new CustomResponseDTO();
        ResponseRideDTO responseRideDTO=rideService.createRide(createRideDTO);
        customResponseDTO.setResponseObject(responseRideDTO);
        customResponseDTO.setResponseMessage("Ride created successfully");
        return new ResponseEntity<>(customResponseDTO, HttpStatus.CREATED);

    }


    @PutMapping("/cancel")
    public ResponseEntity<CustomResponseDTO> cancelRide(@RequestBody @Valid CancelRideDTO cancelRideDTO){
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        ResponseRideDTO responseRideDTO=rideService.cancelRide(cancelRideDTO);
        customResponseDTO.setResponseObject(responseRideDTO);
        customResponseDTO.setResponseMessage("Ride successfully canceled.");
        return new ResponseEntity<>(customResponseDTO,HttpStatus.OK);
    }

    @Operation(
            summary="Partially update ride details",
            description = "Allows for updating specific details of a ride such as available seats, cost, or additional details."
    )
    @PatchMapping
    public ResponseEntity<CustomResponseDTO> partialUpdateRide(@RequestBody @Valid PatchRideDTO patchRideDTO){
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        ResponseRideDTO response=rideService.patchRide(patchRideDTO);
        customResponseDTO.setResponseObject(response);
        customResponseDTO.setResponseMessage("Ride updated successfully.");
        return new ResponseEntity<>(customResponseDTO, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<CustomResponseDTO> updateRide(@RequestBody @Valid UpdateRideDTO updateRideDTO){
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        ResponseRideDTO response=rideService.updateRide(updateRideDTO);
        customResponseDTO.setResponseObject(response);
        customResponseDTO.setResponseMessage("Updated ride with id "+updateRideDTO.getRideId());
        return new ResponseEntity<>(customResponseDTO, HttpStatus.OK);
    }



}
