package org.example.Service;

import jakarta.transaction.Transactional;
import org.example.Exceptions.Driver.DriverRegistrationException;
import org.example.Exceptions.User.UserNotFoundException;
import org.example.Model.DTOs.DriverDTOs.RegisterDriverDTO;
import org.example.Model.DTOs.UserDTOs.Role;
import org.example.Model.Entities.DriverEntity;
import org.example.Model.Entities.UserEntity;
import org.example.Repository.DriverRepository;
import org.example.Repository.UserRepository;
import org.example.Service.Mappers.DriverMapper;
import org.springframework.stereotype.Service;




@Service
public class DriverService {

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
 private DriverMapper driverMapper;
    public DriverService(UserRepository userRepository,DriverRepository driverRepository,DriverMapper driverMapper) {
        this.userRepository = userRepository;
           this.driverRepository = driverRepository;
           this.driverMapper=driverMapper;
    }



    @Transactional
    public boolean registerAsDriver(Long userId, RegisterDriverDTO registerDriverDTO) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("There is no account associated with this id"));


        //could be changed to checking the role on the userEntity
        //but while im still testing considering im deleting tables sometimes
        //i want to check if it exists in the "driver" table
        if(driverRepository.existsByUserEntity(userEntity)){
            throw new DriverRegistrationException("Already a driver");
        }


        userEntity.setRole(Role.DRIVER.toString());
        userRepository.save(userEntity);


        DriverEntity driverEntity=driverMapper.mapRegisterDriverDTOToDriverEntity(registerDriverDTO,userEntity);
        driverRepository.save(driverEntity);

       return true;
    }




}
