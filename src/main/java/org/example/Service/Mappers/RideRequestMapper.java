package org.example.Service.Mappers;

import org.example.Model.DTOs.RideRequestDTOs.ResponseRideRequestDTO;
import org.example.Model.Entities.RideRequestEntity;
import org.example.Model.Entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class RideRequestMapper {

    private final UserMapper userMapper;

    public RideRequestMapper(UserMapper userMapper){
        this.userMapper=userMapper;
    }

    public ResponseRideRequestDTO mapRideRequestEntityToResponseRideRequestDTO(RideRequestEntity rideRequestEntity){
        ResponseRideRequestDTO rideRequestDTO=new ResponseRideRequestDTO();
        rideRequestDTO.setRequestId(rideRequestEntity.getRequestID());
        rideRequestDTO.setRideId(rideRequestEntity.getRide().getRideId());
        rideRequestDTO.setStatus(rideRequestEntity.getStatus().name());
        UserEntity passenger=rideRequestEntity.getPassenger();
        rideRequestDTO.setPassenger(userMapper.mapUserEntityToResponseUserDTO(passenger));
        return rideRequestDTO;

    }


}
