package org.example.Controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.Model.DTOs.CustomResponseDTO;
import org.example.Model.DTOs.RideRequestDTOs.CreateRideRequestDTO;
import org.example.Model.DTOs.RideRequestDTOs.ResponseRideRequestDTO;
import org.example.Model.DTOs.RideRequestDTOs.UpdateRideRequestDTO;
import org.example.Service.RideRequestService;
import org.example.Service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping(path="/api/v1/requests")
@Tag(name = "Ride Request Controller",
        description = "This REST controller provides services to manage ride request of passages to specific rides")
public class RideRequestController {

    private final RideRequestService rideRequestService;


    @Autowired
    public RideRequestController(RideRequestService rideRequestService,
                                 RideService rideService  ){
        this.rideRequestService=rideRequestService;

    }

    @PostMapping
    public ResponseEntity<CustomResponseDTO> createRideRequest(@RequestBody @Valid CreateRideRequestDTO createRideRequestDTO){
        ResponseRideRequestDTO response= rideRequestService.requestRide(createRideRequestDTO);
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        customResponseDTO.setResponseObject(response);
        customResponseDTO.setResponseMessage("Ride request has been successfully created");
        return new ResponseEntity<>(customResponseDTO,HttpStatus.CREATED);
    }


    //TODO: there are 2 get mapping that could be handled in one
    //received/sent
    @GetMapping("/received")
    public ResponseEntity<CustomResponseDTO> getRideRequestsReceivedForRideByDriver(@RequestBody @Valid CreateRideRequestDTO createRideRequestDTO, @RequestParam(value="status", required = false) String status){
        List<ResponseRideRequestDTO> responseRideRequestDTOS=rideRequestService.getRideRequestsReceivedForRideByDriver(createRideRequestDTO,status);
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        customResponseDTO.setResponseObject(responseRideRequestDTOS);
        customResponseDTO.setResponseMessage(responseRideRequestDTOS.size()+" ride requests found");
        return new ResponseEntity<>(customResponseDTO,HttpStatus.OK);
    }

    @GetMapping("/sent")
    public ResponseEntity<CustomResponseDTO> getRequests(@RequestBody @Valid CreateRideRequestDTO createRideRequestDTO,@RequestParam(value = "status",required = false)  String status){
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        List<ResponseRideRequestDTO> response=rideRequestService.getRideRequestsSentByUserWithOptionalFilter(createRideRequestDTO,status);
        customResponseDTO.setResponseObject(response);
        customResponseDTO.setResponseMessage(response.size()+" requests found");
        return new ResponseEntity<>(customResponseDTO,HttpStatus.OK);
    }

    @PutMapping("/accept")
    public ResponseEntity<CustomResponseDTO> acceptRideRequest(
           @RequestBody @Valid UpdateRideRequestDTO updateRideRequestDTO
    ){
        ResponseRideRequestDTO responseRideRequestDTO=rideRequestService.acceptRideRequest(updateRideRequestDTO);
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        customResponseDTO.setResponseObject(responseRideRequestDTO);
        customResponseDTO.setResponseMessage("Ride request status updated.");

        return new ResponseEntity<>(customResponseDTO,HttpStatus.OK);
    }

    @PutMapping("/decline")
    public ResponseEntity<CustomResponseDTO> declineRideRequest(
           @RequestBody @Valid UpdateRideRequestDTO updateRideRequestDTO
    ){
        ResponseRideRequestDTO responseRideRequestDTO=rideRequestService.declineRideRequest(updateRideRequestDTO);
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        customResponseDTO.setResponseObject(responseRideRequestDTO);
        customResponseDTO.setResponseMessage("Ride request status updated.");

        return new ResponseEntity<>(customResponseDTO,HttpStatus.OK);
    }

//TODO to be deleted

//    @PutMapping("/requests/cancel")
//    public ResponseEntity<CustomResponseDTO> cancelRideRequest(
//            @RequestBody @Valid UpdateRideRequestDTO updateRideRequestDTO
//            ){
//        ResponseRideRequestDTO responseRideRequestDTO=rideRequestService.cancelRideRequest(updateRideRequestDTO);
//        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
//        customResponseDTO.setResponseObject(responseRideRequestDTO);
//        customResponseDTO.setResponseMessage("Ride request status updated.");
//
//        return new ResponseEntity<>(customResponseDTO,HttpStatus.OK);
//    }
//

    @DeleteMapping("/delete")
    public ResponseEntity deleteRideRequest(@RequestBody @Valid UpdateRideRequestDTO updateRideRequestDTO){
      //TODO: needs refactoring
        boolean deleted= rideRequestService.deleteRideRequest(updateRideRequestDTO);
        if(deleted){
            return new ResponseEntity("Request deleted successfully",HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

    }






}
