package org.example.Service.Mappers;


import org.example.Model.DTOs.UserDTOs.CreateUserDTO;
import org.example.Model.DTOs.UserDTOs.ResponseUserDTO;
import org.example.Model.DTOs.UserDTOs.Role;
import org.example.Model.DTOs.UserDTOs.UserDTO;
import org.example.Model.Entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {






    public UserEntity mapCreateUserDTOToUserEntity(CreateUserDTO createUserDTO, Role role){
        UserEntity userEntity=new UserEntity();

        userEntity.setFirstName(createUserDTO.getFirstName());
        userEntity.setFullName(createUserDTO.getFirstName()+ " "+ createUserDTO.getLastName());
        userEntity.setLastName(createUserDTO.getLastName());
        userEntity.setEmail(createUserDTO.getEmail());
        userEntity.setPhoneNo(createUserDTO.getPhoneNo());
        userEntity.setBirthdate(createUserDTO.getBirthdate());
        userEntity.setPassword(createUserDTO.getPassword());

        userEntity.setRole(role.name());

        return userEntity;
    }



    public ResponseUserDTO mapUserEntityToResponseUserDTO(UserEntity userEntity){
        ResponseUserDTO responseUserDTO= new ResponseUserDTO();

        responseUserDTO.setID(userEntity.getUserId());
        responseUserDTO.setFirstName(userEntity.getFirstName());
        responseUserDTO.setLastName(userEntity.getLastName());
        responseUserDTO.setFullName(userEntity.getFullName());
        responseUserDTO.setEmail(userEntity.getEmail());
        responseUserDTO.setPhoneNo(userEntity.getPhoneNo());
        responseUserDTO.setBirthdate(userEntity.getBirthdate());
        responseUserDTO.setRole(userEntity.getRole());

        return responseUserDTO;
    }

    public UserDTO mapUserEntityToUserDTO(UserEntity userEntity){
        UserDTO userDTO= new UserDTO();

        //map user dto
        //properties
        //id
        userDTO.setID(userEntity.getUserId());
        //firstName
        userDTO.setFirstName(userEntity.getFirstName());
        //middleName

        //lastName
        userDTO.setLastName(userEntity.getLastName());
        //email
        userDTO.setEmail(userEntity.getEmail());
        //phoneNo
        userDTO.setPhoneNo(userEntity.getPhoneNo());
        //birthdate
        userDTO.setBirthdate(userEntity.getBirthdate());

        return userDTO;
    }

}
