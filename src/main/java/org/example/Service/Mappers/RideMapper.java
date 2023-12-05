package org.example.Service.Mappers;

import org.example.Model.DTOs.RideDTOs.CreateRideDTO;
import org.example.Model.DTOs.RideDTOs.PatchRideDTO;
import org.example.Model.DTOs.RideDTOs.ResponseRideDTO;
import org.example.Model.Entities.DriverEntity;
import org.example.Model.Entities.RideEntity;
import org.example.Model.Entities.RideStatus;
import org.example.Model.RideRequestStatus;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class RideMapper {
    LocationMapper locationMapper;
    DriverMapper driverMapper;
    UserMapper userMapper;
    public RideMapper(LocationMapper locationMapper, DriverMapper driverMapper,UserMapper userMapper){
        this.locationMapper=locationMapper;
        this.driverMapper=driverMapper;
        this.userMapper=userMapper;

    }

    public RideEntity mapCreateRideDTOtoRideEntity(CreateRideDTO createRideDTO, DriverEntity driverEntity){
        RideEntity rideEntity=new RideEntity();
        rideEntity.setDepartureLocation(locationMapper.mapLocationDTOtoLocationEntity(createRideDTO.getDepartureLocation()));
        rideEntity.setDestinationLocation(locationMapper.mapLocationDTOtoLocationEntity(createRideDTO.getDestinationLocation()));
        rideEntity.setDateTimeOfRide(createRideDTO.getDateAndTimeOfRide());
        rideEntity.setDriver(driverEntity);
        rideEntity.setRideStatus(RideStatus.ACTIVE.name());
        rideEntity.setAvailableSeats(createRideDTO.getAvailableSeats());
        rideEntity.setAdditionalDetails(createRideDTO.getAdditionalDetails());
        rideEntity.setPassengers(new HashSet<>());

        return rideEntity;
    }


    public ResponseRideDTO mapCreateRideEntitytoRideDTO(RideEntity rideEntity){
        ResponseRideDTO responseRideDTO=new ResponseRideDTO();
        responseRideDTO.setRideId(rideEntity.getRideId());
        responseRideDTO.setDepartureLocation(locationMapper.mapLocationEntitytoLocationDTO(rideEntity.getDepartureLocation()));
        responseRideDTO.setDestinationLocation(locationMapper.mapLocationEntitytoLocationDTO(rideEntity.getDestinationLocation()));
        responseRideDTO.setDateAndTimeOfRide(rideEntity.getDateTimeOfRide());
        responseRideDTO.setStatus(rideEntity.getRideStatus());
        responseRideDTO.setDriver(driverMapper.mapDriverEntityToSimpliedDriverDTO(rideEntity.getDriver()));
        responseRideDTO.setAvailableSeats(rideEntity.getAvailableSeats());
        responseRideDTO.setCost(rideEntity.getCost());

        responseRideDTO.setAdditionalDetails(rideEntity.getAdditionalDetails());
       responseRideDTO.setPassengers(rideEntity.getPassengers().stream().map(
               userEntity -> userMapper.mapUserEntityToResponseUserDTO(userEntity)
       ).toList());
        return responseRideDTO;
    }


}
