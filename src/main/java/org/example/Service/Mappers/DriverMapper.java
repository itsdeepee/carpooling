package org.example.Service.Mappers;

import org.example.Model.DTOs.DriverDTOs.RegisterDriverDTO;
import org.example.Model.DTOs.UserDTOs.CreateUserDTO;
import org.example.Model.DTOs.UserDTOs.ResponseUserDTO;
import org.example.Model.DTOs.UserDTOs.Role;
import org.example.Model.DTOs.UserDTOs.UserDTO;
import org.example.Model.Entities.DriverEntity;
import org.example.Model.Entities.UserEntity;
import org.springframework.stereotype.Component;


@Component
public class DriverMapper {
    public DriverEntity mapRegisterDriverDTOToDriverEntity(RegisterDriverDTO registerDriverDTO,UserEntity userEntity){
        DriverEntity driverEntity=new DriverEntity();
        driverEntity.setUserEntity(userEntity);
        driverEntity.setVehicleType(registerDriverDTO.getVehicleType());
        driverEntity.setDriverLicenseNumber(registerDriverDTO.getDriverLicenseNumber());
        return driverEntity;
    }


    public RegisterDriverDTO mapDriverEntityToRegisterDriverDTO(DriverEntity driverEntity){
        RegisterDriverDTO registerDriverDTO=new RegisterDriverDTO();

        registerDriverDTO.setDriverLicenseNumber(driverEntity.getDriverLicenseNumber());
        registerDriverDTO.setVehicleType(driverEntity.getVehicleType());

        return registerDriverDTO;
    }
}
