package org.example.Service.Mappers;

import org.example.Model.DTOs.Location.LocationDTO;
import org.example.Model.DTOs.RideDTOs.CreateRideDTO;
import org.example.Model.DTOs.RideDTOs.ResponseRideDTO;
import org.example.Model.Entities.DriverEntity;
import org.example.Model.Entities.LocationEntity;
import org.example.Model.Entities.RideEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class RideMapper {
    LocationMapper locationMapper;
    DriverMapper driverMapper;
    public RideMapper(LocationMapper locationMapper, DriverMapper driverMapper){
        this.locationMapper=locationMapper;
        this.driverMapper=driverMapper;

    }

    public RideEntity mapCreateRideDTOtoRideEntity(CreateRideDTO createRideDTO, DriverEntity driverEntity){
        RideEntity rideEntity=new RideEntity();
        rideEntity.setDepartureLocation(locationMapper.mapLocationDTOtoLocationEntity(createRideDTO.getDepartureLocation()));
        rideEntity.setDestinationLocation(locationMapper.mapLocationDTOtoLocationEntity(createRideDTO.getDestinationLocation()));
        rideEntity.setDateTimeOfRide(createRideDTO.getDateAndTimeOfRide());
        rideEntity.setDriver(driverEntity);
        rideEntity.setAvailableSeats(createRideDTO.getAvailableSeats());
        rideEntity.setAdditionalDetails(createRideDTO.getAdditionalDetails());
        rideEntity.setPassengers(new HashSet<>());

        return rideEntity;
    }

    public ResponseRideDTO mapCreateRideEntitytoRideDTO(RideEntity rideEntity){
        ResponseRideDTO responseRideDTO=new ResponseRideDTO();


        responseRideDTO.setDepartureLocation(locationMapper.mapLocationEntitytoLocationDTO(rideEntity.getDepartureLocation()));
        responseRideDTO.setDestinationLocation(locationMapper.mapLocationEntitytoLocationDTO(rideEntity.getDestinationLocation()));
        responseRideDTO.setDateAndTimeOfRide(rideEntity.getDateTimeOfRide());
        System.out.println(driverMapper.mapDriverEntityToSimpliedDriverDTO(rideEntity.getDriver()));
        responseRideDTO.setDriver(driverMapper.mapDriverEntityToSimpliedDriverDTO(rideEntity.getDriver()));
//
        responseRideDTO.setAvailableSeats(rideEntity.getAvailableSeats());
        responseRideDTO.setAdditionalDetails(rideEntity.getAdditionalDetails());
       // responseRideDTO.setPassengers(rideEntity.getPassengers());

        return responseRideDTO;
    }
}
