package org.example.Controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.Model.DTOs.CustomResponseDTO;
import org.example.Model.DTOs.RideRequestDTOs.ResponseRideRequestDTO;
import org.example.Service.RideRequestService;
import org.example.Service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@RestController
@Tag(name = "Ride Request Controller",
        description = "This REST controller provides services to manage ride request of passages to specific rides")
public class RideRequestController {

    private final RideRequestService rideRequestService;


    @Autowired
    public RideRequestController(RideRequestService rideRequestService,
                                 RideService rideService  ){
        this.rideRequestService=rideRequestService;

    }

    @PostMapping(path="{userId}/rides/{rideId}/requests")
    public ResponseEntity<CustomResponseDTO> makeRideRequest( @PathVariable Long userId,@PathVariable Long rideId){
        ResponseRideRequestDTO response= rideRequestService.requestRide(rideId,userId);
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        customResponseDTO.setResponseObject(response);
        customResponseDTO.setResponseMessage("A request has been created for ride id "+rideId);
        return new ResponseEntity<>(customResponseDTO,HttpStatus.CREATED);
    }


    @GetMapping("{driverId}/rides/{rideId}/requests")
    public ResponseEntity<CustomResponseDTO> getRideRequestsForRide(@PathVariable Long rideId,@PathVariable Long driverId, @RequestParam(value="status", required = false) String status){

        List<ResponseRideRequestDTO> responseRideRequestDTOS=rideRequestService.getRideRequestsForRide(rideId,driverId,status);

        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        customResponseDTO.setResponseObject(responseRideRequestDTOS);
        customResponseDTO.setResponseMessage(responseRideRequestDTOS.size()+" ride requests found");
        return new ResponseEntity<>(customResponseDTO,HttpStatus.OK);
    }



    @GetMapping("{userId}/requests")
    public ResponseEntity<CustomResponseDTO> getRequests(@PathVariable Long userId,@RequestParam(value = "status",required = false)  String status){
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        List<ResponseRideRequestDTO> response;

        if(Objects.isNull(status) || status.isBlank()){
            response=rideRequestService.getAllRequests(userId);


        }else{
            response=rideRequestService.getRequestsByStatus(userId,status);
        }

        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        customResponseDTO.setResponseObject(response);
        customResponseDTO.setResponseMessage(response.size()+" requests found");
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
