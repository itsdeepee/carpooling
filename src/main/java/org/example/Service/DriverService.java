package org.example.Service;

import jakarta.transaction.Transactional;
import org.example.Exceptions.Driver.DriverRegistrationException;
import org.example.Exceptions.User.UserNotFoundException;
import org.example.Model.DTOs.DriverDTOs.RegisterDriverDTO;
import org.example.Model.DTOs.DriverDTOs.ResponseDriverDTO;
import org.example.Model.DTOs.UserDTOs.Role;
import org.example.Model.Entities.DriverEntity;
import org.example.Model.Entities.UserEntity;
import org.example.Repository.DriverRepository;
import org.example.Repository.UserRepository;
import org.example.Service.Mappers.DriverMapper;
import org.example.Service.Mappers.RideRequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;



@Service
public class DriverService {

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    @Autowired
    public DriverService(UserRepository userRepository,
                         DriverRepository driverRepository,
                         DriverMapper driverMapper){
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
    }

    @Transactional
    public ResponseDriverDTO registerAsDriver(Long userId, RegisterDriverDTO registerDriverDTO) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("There is no user associated with this id"));
        if (driverRepository.existsByUser(userEntity)) {
            throw new DriverRegistrationException("Already a driver");
        }
        userEntity.setRole(Role.DRIVER.toString());
     //   userEntity = userRepository.save(userEntity);
        DriverEntity driverEntity = driverMapper.mapRegisterDriverDTOToDriverEntity(registerDriverDTO,userEntity);
        try{
            driverRepository.save(driverEntity);
        }catch (DataIntegrityViolationException ex){
            throw  new DriverRegistrationException("The driver license number is associated with another user account");
        }

        return driverMapper.mapDriverEntityToResponseDriverDTO(driverEntity);

    }


}
