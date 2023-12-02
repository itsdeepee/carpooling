package org.example.Controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.Model.DTOs.CustomResponseDTO;
import org.example.Model.DTOs.RideDTOs.CreateRideDTO;
import org.example.Model.DTOs.RideDTOs.PatchRideDTO;
import org.example.Model.DTOs.RideDTOs.ResponseRideDTO;
import org.example.Service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Ride Controller", description = "This REST controller" +
        "provides services to manage rides in the Carpooling application")
public class RideController {

    private RideService rideService;

    @Autowired
    RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @GetMapping("rides/active")
    public ResponseEntity<CustomResponseDTO> getAllActiveRides(
            @RequestParam(value="startLocation",required = false) String startLocation,
            @RequestParam(value="endLocation",required = false) String endLocation
            ){
        List<ResponseRideDTO> result=rideService.findActiveRidesFiltered(startLocation,endLocation);
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        if(result.size()==0){
            return new ResponseEntity<>(customResponseDTO,HttpStatus.NO_CONTENT);
        }
        customResponseDTO.setResponseObject(result);
        customResponseDTO.setResponseMessage(result.size()+" active rides found");
        return new ResponseEntity<>(customResponseDTO,HttpStatus.OK);
    }



    @GetMapping("{userId}/rides")
    public ResponseEntity<CustomResponseDTO> getCurrentRides(@PathVariable Long userId, @RequestParam(value="status",required = false) String status){
        List<ResponseRideDTO> result=rideService.getRidesForUser(userId,status);
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        if(result.size()==0){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        customResponseDTO.setResponseObject(result);
        customResponseDTO.setResponseMessage(result.size()+" rides created in the last 30 days.");
        return new ResponseEntity<>(customResponseDTO,HttpStatus.OK);

    }

    @GetMapping("{userId}/rides/history")
    public ResponseEntity<CustomResponseDTO> getRidesHistory(@PathVariable Long userId){
        List<ResponseRideDTO> result=rideService.getRidesHistory(userId);
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        if(result.size()==0){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        customResponseDTO.setResponseObject(result);
        customResponseDTO.setResponseMessage(result.size()+" rides created in the last 30 days.");
        return new ResponseEntity<>(customResponseDTO,HttpStatus.OK);

    }


    @PostMapping("{userId}/rides")
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


    @PutMapping("{userId}/rides/{rideId}/cancel")
    public ResponseEntity<CustomResponseDTO> cancelRide(@PathVariable Long userId, @PathVariable Long rideId){
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        ResponseRideDTO responseRideDTO=rideService.cancelRide(userId,rideId);
        customResponseDTO.setResponseObject(responseRideDTO);
        customResponseDTO.setResponseMessage("Ride successfully canceled.");
        return new ResponseEntity<>(customResponseDTO,HttpStatus.OK);
    }

    @PatchMapping("{userId}/rides/{rideId}")
    public ResponseEntity<CustomResponseDTO> partialUpdateRide(@RequestBody @Valid PatchRideDTO patchRideDTOhRideDTo, @PathVariable Long userId, @PathVariable Long rideId){
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        ResponseRideDTO response=rideService.patchRide(patchRideDTOhRideDTo, userId, rideId);
        customResponseDTO.setResponseObject(response);
        customResponseDTO.setResponseMessage("Updated ride with id "+rideId);
        return new ResponseEntity<>(customResponseDTO, HttpStatus.OK);
    }

    @PutMapping("{userId}/rides/{rideId}")
    public ResponseEntity<CustomResponseDTO> updateRide(@RequestBody @Valid CreateRideDTO createRideDTO, @PathVariable Long userId, @PathVariable Long rideId){
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        ResponseRideDTO response=rideService.updateRide(createRideDTO, userId, rideId);
        customResponseDTO.setResponseObject(response);
        customResponseDTO.setResponseMessage("Updated ride with id "+rideId);
        return new ResponseEntity<>(customResponseDTO, HttpStatus.OK);
    }


}
