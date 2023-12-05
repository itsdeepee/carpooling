package org.example.Service;


import org.example.Exceptions.User.DuplicateEmailException;
import org.example.Exceptions.User.UserCreationException;
import org.example.Exceptions.User.UserNotFoundException;
import org.example.Model.DTOs.UserDTOs.*;

import org.example.Model.Entities.UserEntity;
import org.example.Repository.UserRepository;
import org.example.Service.Mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;

    }

    public ResponseUserDTO createUser(CreateUserDTO createUserDTO) {
        int age = calculateAge(createUserDTO.getBirthdate());
        if (age < 16) {
            throw new UserCreationException("User must be above 16 years old.");
        }
        boolean userAlreadyExists = existsByEmail(createUserDTO.getEmail());
        if (userAlreadyExists) {
            throw new DuplicateEmailException("Email already in use");
        }
        UserEntity userEntityToAddToDB = userMapper.mapCreateUserDTOToUserEntity(createUserDTO, Role.PASSENGER);
        UserEntity createdUser = userRepository.save(userEntityToAddToDB);
        return userMapper.mapUserEntityToResponseUserDTO(createdUser);
    }

    public ResponseUserDTO findById(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " does not exist"));

        return userMapper.mapUserEntityToResponseUserDTO(userEntity);
    }

    public List<ResponseUserDTO> findOnlyDriversByPartialName(String partialName) {
        List<UserEntity> usersEntitiesWithPartialName = userRepository.findByFullNameContainingIgnoreCaseAndRole(partialName, Role.DRIVER.name());
        System.out.println("Partial name: " + partialName);
        List<ResponseUserDTO> resultUserDTOList = usersEntitiesWithPartialName.stream()
                .map(userEntity -> userMapper.mapUserEntityToResponseUserDTO(userEntity))
                .toList();

        resultUserDTOList.forEach(System.out::println);

        return resultUserDTOList;
    }


    public boolean existsByEmail(String email) {

        if (Objects.isNull(email) || email.isBlank()) {
            return false;
        }
        return userRepository.existsByEmail(email);

    }


    private void handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        if (isDuplicateEmailViolation(ex)) {
            throw new DuplicateEmailException("Email already exists.");
        }
        throw ex;


    }

    private boolean isDuplicateEmailViolation(DataIntegrityViolationException ex) {
        return ex.getMessage().contains("duplicate key value violates unique constraint");
    }

    private int calculateAge(LocalDate birthdate) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthdate, currentDate);
        return period.getYears();
    }
}
