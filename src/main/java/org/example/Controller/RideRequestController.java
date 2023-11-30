package org.example.Controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.Model.DTOs.CustomResponseDTO;
import org.example.Model.DTOs.RideRequestDTOs.ResponseRideRequestDTO;
import org.example.Service.RideRequestService;
import org.example.Service.RideService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Tag(name = "Ride Request Controller",
        description = "This REST controller provides services to manage ride request of passages to specific rides")
public class RideRequestController {

    private final RideRequestService rideRequestService;


    public RideRequestController(RideRequestService rideRequestService,
                                 RideService rideService  ){
        this.rideRequestService=rideRequestService;

    }

    @PostMapping(path="{userId}/rides/{rideId}/requests")
    public ResponseEntity<CustomResponseDTO> makeRideRequest(@PathVariable Long rideId, @PathVariable Long userID){
        ResponseRideRequestDTO response= rideRequestService.requestRide(rideId,userID);
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        customResponseDTO.setResponseObject(response);
        customResponseDTO.setResponseMessage("A request has been created for ride id "+rideId);
        return new ResponseEntity<>(customResponseDTO,HttpStatus.CREATED);
    }


    @GetMapping("{driverId}/rides/{rideId}/requests")
    public ResponseEntity<CustomResponseDTO> getRideRequestsForRide(@PathVariable Long rideId,@PathVariable Long driverId){
        List<ResponseRideRequestDTO> responseRideRequestDTOS=rideRequestService.getRideRequestsForRide(rideId,driverId);
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        customResponseDTO.setResponseObject(responseRideRequestDTOS);
        customResponseDTO.setResponseMessage(responseRideRequestDTOS.size()+" ride requests found");
        return new ResponseEntity<>(customResponseDTO,HttpStatus.OK);
    }

    @GetMapping("{userId}/requests")
    public ResponseEntity<CustomResponseDTO> getAllRequests(@PathVariable Long userId){
        List<ResponseRideRequestDTO> response=rideRequestService.getAllRequests(userId);
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(customResponseDTO,HttpStatus.OK);
    }

    @GetMapping("{userId}/requests")
    public ResponseEntity<CustomResponseDTO> getRequestsByStatus(@PathVariable Long userId,@RequestParam String status){
        List<ResponseRideRequestDTO> response=rideRequestService.getRequestsByStatus(userId,status);
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(customResponseDTO,HttpStatus.OK);
    }

    @PutMapping("/{driverId}/rides/{rideId}/requests/{requestId}/accept")
    public ResponseEntity<CustomResponseDTO> acceptRideRequest(
            @PathVariable Long driverId,
            @PathVariable Long rideId,
            @PathVariable Long requestId
    ){
        ResponseRideRequestDTO responseRideRequestDTO=rideRequestService.acceptRideRequest(driverId,rideId,requestId);
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        customResponseDTO.setResponseObject(responseRideRequestDTO);
        customResponseDTO.setResponseMessage("Ride request status updated.");

        return new ResponseEntity<>(customResponseDTO,HttpStatus.OK);
    }

    @PutMapping("/{driverId}/rides/{rideId}/requests/{requestId}/decline")
    public ResponseEntity<CustomResponseDTO> declineRideRequest(
            @PathVariable Long driverId,
            @PathVariable Long rideId,
            @PathVariable Long requestId
    ){
        ResponseRideRequestDTO responseRideRequestDTO=rideRequestService.declineRideRequest(driverId,rideId,requestId);
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        customResponseDTO.setResponseObject(responseRideRequestDTO);
        customResponseDTO.setResponseMessage("Ride request status updated.");

        return new ResponseEntity<>(customResponseDTO,HttpStatus.OK);
    }

    @PutMapping("/{userId}/rides/{rideId}/requests/{requestId}/cancel")
    public ResponseEntity<CustomResponseDTO> cancelRideRequest(
            @PathVariable Long userId,
            @PathVariable Long rideId,
            @PathVariable Long requestId
    ){
        ResponseRideRequestDTO responseRideRequestDTO=rideRequestService.cancelRideRequest(userId,rideId,requestId);
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        customResponseDTO.setResponseObject(responseRideRequestDTO);
        customResponseDTO.setResponseMessage("Ride request status updated.");

        return new ResponseEntity<>(customResponseDTO,HttpStatus.OK);
    }

    @DeleteMapping("{userId}/requests/{requestId}/delete")
    public ResponseEntity deleteRideRequest(@PathVariable Long requestId, @PathVariable Long userId){
        boolean deleted= rideRequestService.deleteRideRequest(requestId,userId);
        if(deleted){
            return new ResponseEntity("Request deleted successfully",HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

    }






}
