package org.example.Service;


import org.example.Model.DTOs.UserDTO;
import org.example.Repository.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    List<UserDTO> userDTOS=new ArrayList<>();

    UserDTO userDTO1=new UserDTO(1L,"Adam","","Blue","adam.blue@gmail.com","0748754894", LocalDate.of(1997,5,16),"42d3d2d2");
    UserDTO userDTO2=new UserDTO(2L,"John","","Red","john.blue@gmail.com","0748754584", LocalDate.of(1995,5,16),"dsddada");
    UserDTO userDTO3=new UserDTO(3L,"Mer","","Smith","mer.smith@gmail.com","076987253", LocalDate.of(1987,5,25),"dsceacs");


    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;

        //to be deleted later
        userDTOS.add(userDTO1);
        userDTOS.add(userDTO2);
        userDTOS.add(userDTO3);

    }

    public List<UserDTO> getAllUsers(){
        return userDTOS;
    }

    public void save(UserDTO userDTO){

        userDTOS.add(userDTO);
        userDTO.setID((long) userDTOS.size());
    }

    public Optional<UserDTO> findByEmail(String email){
        if(email.isBlank()) return Optional.empty();

        return userDTOS.stream()
               .filter(user->user.getEmail().equals(email))
               .findAny();

    }
}
