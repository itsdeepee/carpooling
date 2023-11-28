package org.example.Service.Mappers;

import org.example.Model.DTOs.DriverDTOs.RegisterDriverDTO;
import org.example.Model.DTOs.DriverDTOs.ResponseDriverDTO;
import org.example.Model.DTOs.DriverDTOs.SimplifiedDriverDTO;
import org.example.Model.DTOs.UserDTOs.CreateUserDTO;
import org.example.Model.DTOs.UserDTOs.ResponseUserDTO;
import org.example.Model.DTOs.UserDTOs.Role;
import org.example.Model.DTOs.UserDTOs.UserDTO;
import org.example.Model.Entities.DriverEntity;
import org.example.Model.Entities.UserEntity;
import org.springframework.stereotype.Component;


@Component
public class DriverMapper {

    UserMapper userMapper;

    public DriverMapper(UserMapper userMapper){
        this.userMapper=userMapper;
    }
    public DriverEntity mapRegisterDriverDTOToDriverEntity(RegisterDriverDTO registerDriverDTO,UserEntity userEntity){
        DriverEntity driverEntity=new DriverEntity();
      //  driverEntity.setUserEntity(userEntity);

        driverEntity.setVehicleType(registerDriverDTO.getVehicleType());
        driverEntity.setDriverLicenseNumber(registerDriverDTO.getDriverLicenseNumber());
        return driverEntity;
    }


    public ResponseDriverDTO mapDriverEntityToResponseDriverDTO(DriverEntity driverEntity){
        ResponseDriverDTO responseDriverDTO=new ResponseDriverDTO();

        ResponseUserDTO responseUserDTO=new ResponseUserDTO();
        responseUserDTO=userMapper.mapUserEntityToResponseUserDTO(driverEntity.getUser());
        responseDriverDTO.setResponseUserDTO(responseUserDTO);
        responseDriverDTO.setDriverLicenseNumber(driverEntity.getDriverLicenseNumber());
        responseDriverDTO.setVehicleType(driverEntity.getVehicleType());
        responseDriverDTO.setRides(driverEntity.getRides());
        responseDriverDTO.setRecentAddresses(driverEntity.getRecentAddresses());

        return responseDriverDTO;
    }

    public SimplifiedDriverDTO mapDriverEntityToSimpliedDriverDTO(DriverEntity driverEntity){
        SimplifiedDriverDTO simplifiedDriverDTO=new SimplifiedDriverDTO();
        simplifiedDriverDTO.setId(driverEntity.getId());
        simplifiedDriverDTO.setFullName(driverEntity.getUser().getFullName());
        simplifiedDriverDTO.setEmail(driverEntity.getUser().getEmail());
        simplifiedDriverDTO.setPhoneNo(driverEntity.getUser().getPhoneNo());
        simplifiedDriverDTO.setDriverLicenseNumber(driverEntity.getDriverLicenseNumber());
        simplifiedDriverDTO.setVehicleType(driverEntity.getVehicleType());
        return simplifiedDriverDTO;
    }
}
