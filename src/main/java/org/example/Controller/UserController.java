package org.example.Controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.Model.DTOs.CustomResponseDTO;
import org.example.Model.DTOs.UserDTO;
import org.example.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
@Tag(name = "User Controller",
        description = "REST Controller for managing users.")
public class UserController {

    private final UserService userService;


    @Autowired
    UserController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping
    public List<UserDTO> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    @Operation(
            summary = "Create a new user",
            description="Creates a new user with the properties passed in the request body",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User created successfully"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Email already in use"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Wrong request body format"
                    )
            }
    )
    public ResponseEntity<CustomResponseDTO> createNewUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {

        //custom response object
        CustomResponseDTO customResponseDTO = new CustomResponseDTO();


        //validation errors
        if (bindingResult.hasErrors()) {
            List<String> errorMessages=bindingResult.getFieldErrors().stream().map(error->error.getField()+": "+error.getDefaultMessage()).toList();
            customResponseDTO.setResponseObject(null);
            customResponseDTO.setResponseMessage("Wrong request body format");
            customResponseDTO.setErrorDetails(errorMessages);


            return new ResponseEntity<>(customResponseDTO, HttpStatus.BAD_REQUEST);
        }

        //When a user with the same email address already exists.
        Optional<UserDTO> optionalUserDTO = userService.findByEmail(userDTO.getEmail());
        if (optionalUserDTO.isPresent()) {
            customResponseDTO.setResponseMessage("Email already in use.");
            customResponseDTO.setResponseObject(null);
            customResponseDTO.setErrorDetails(null);
            return new ResponseEntity<>(customResponseDTO, HttpStatus.CONFLICT);
        }


        userService.save(userDTO);
        customResponseDTO.setResponseObject(userDTO);
        customResponseDTO.setResponseMessage("User created successfully");
        customResponseDTO.setErrorDetails(null);
        return new ResponseEntity<>(customResponseDTO, HttpStatus.CREATED);

    }


}
