package org.example.Controller;

import org.example.Model.DTOs.CustomResponseDTO;
import org.example.Model.DTOs.DriverDTOs.RegisterDriverDTO;
import org.example.Model.DTOs.DriverDTOs.ResponseDriverDTO;
import org.example.Model.DTOs.RideRequestDTOs.ResponseRideRequestDTO;
import org.example.Service.DriverService;
import org.example.Service.RideRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/drivers")
@Validated
public class DriverController {
    private final DriverService driverService;
    private final RideRequestService rideRequestService;

    public DriverController(DriverService driverService,
                            RideRequestService rideRequestService) {
        this.driverService = driverService;
        this.rideRequestService=rideRequestService;
    }
    @PostMapping("/register/{userId}")
    public ResponseEntity<CustomResponseDTO> registerAsDriver(@PathVariable Long userId, @RequestBody RegisterDriverDTO registerDriverDTO) {
        ResponseDriverDTO responseDriverDTO=driverService.registerAsDriver(userId, registerDriverDTO);
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        customResponseDTO.setResponseMessage("Successfully registered user as driver");
        customResponseDTO.setResponseObject(responseDriverDTO);
        return new ResponseEntity<>(customResponseDTO, HttpStatus.OK);
    }

    @GetMapping("{driverId}/myRides/{rideId}/requests")
    public ResponseEntity<CustomResponseDTO> getRideRequestsForRide(@PathVariable Long rideId,@PathVariable Long driverId){
        List<ResponseRideRequestDTO> responseRideRequestDTOS=rideRequestService.getRideRequestsForRide(rideId,driverId);
        CustomResponseDTO customResponseDTO=new CustomResponseDTO();
        customResponseDTO.setResponseObject(responseRideRequestDTOS);
        customResponseDTO.setResponseMessage(responseRideRequestDTOS.size()+" ride requests found");
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


}
