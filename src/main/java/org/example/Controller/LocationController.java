package org.example.Controller;

import org.example.Model.DTOs.Location.LocationDTO;
import org.example.Service.LocationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }


    @GetMapping("/location")
    public List<LocationDTO> getCoordinates(@RequestParam String address) throws IOException {
       return  locationService.getCoordinatesByAddress(address);
    }






}
