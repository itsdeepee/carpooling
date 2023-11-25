package org.example.Controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.Model.DTOs.CustomResponseDTO;
import org.example.Model.DTOs.RideDTO;
import org.example.Service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    //get available rides
    @GetMapping
    @Operation(
            summary = "Get a list of rides",
            description = "Retrieves all rides in the Carpooling application.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved"
                    )
            })
    public List<RideDTO> getAllRides() {
        return this.rideService.findAll();
    }


    @PostMapping
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
    public ResponseEntity<CustomResponseDTO> createNewRide(@Valid @RequestBody RideDTO rideDTO, BindingResult bindingResult) {
        //custom response object
        CustomResponseDTO customResponseDTO = new CustomResponseDTO();

        //validation errors
        if (bindingResult.hasErrors()) {

            List<String> errorMessages = bindingResult.getFieldErrors().stream().map(error -> error.getField() + ": " + error.getDefaultMessage()).toList();
            System.out.println("Binding result.getFieldErrorCount: " + bindingResult.getFieldErrorCount());
            customResponseDTO.setResponseObject(null);
            customResponseDTO.setResponseMessage("Wrong request body format");


            return new ResponseEntity<>(customResponseDTO, HttpStatus.BAD_REQUEST);
        }

        rideService.save(rideDTO);
        customResponseDTO.setResponseObject(rideDTO);
        customResponseDTO.setResponseMessage("Ride created successfully");
        return new ResponseEntity<>(customResponseDTO, HttpStatus.CREATED);


    }


}
