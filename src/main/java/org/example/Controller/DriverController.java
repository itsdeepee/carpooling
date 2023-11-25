package org.example.Controller;

import org.example.Model.DTOs.CustomResponseDTO;
import org.example.Model.DTOs.DriverDTOs.RegisterDriverDTO;
import org.example.Service.DriverService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/drivers")
@Validated
public class DriverController {


    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping("/register/{userId}")
    public ResponseEntity<CustomResponseDTO> registerAsDriver(@PathVariable Long userId, @RequestBody RegisterDriverDTO registerDriverDTO) {
        //should change to return some object
        driverService.registerAsDriver(userId, registerDriverDTO);
       CustomResponseDTO customResponseDTO=new CustomResponseDTO();
       customResponseDTO.setResponseMessage("Successfully registered user as driver");

        return new ResponseEntity<>(customResponseDTO, HttpStatus.OK);
    }


}
