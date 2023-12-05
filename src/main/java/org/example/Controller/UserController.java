package org.example.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.Exceptions.User.UserCreationException;
import org.example.Model.DTOs.CustomResponseDTO;
import org.example.Model.DTOs.UserDTOs.CreateUserDTO;
import org.example.Model.DTOs.UserDTOs.ResponseUserDTO;
import org.example.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/users")
@Validated
@Tag(name = "User Controller",
        description = "REST Controller for managing users.")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

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
    @PostMapping
    public ResponseEntity<CustomResponseDTO> createNewUser(@Valid  @RequestBody CreateUserDTO createUserDTO) {
        ResponseUserDTO responseUserDTO= userService.createUser(createUserDTO);
        CustomResponseDTO customResponseDTO = new CustomResponseDTO();
        customResponseDTO.setResponseObject(responseUserDTO);
        customResponseDTO.setResponseMessage("User created successfully");
        return new ResponseEntity<>(customResponseDTO, HttpStatus.CREATED);

    }


    @GetMapping("/{id}")
    public ResponseEntity<CustomResponseDTO> getUserById(@PathVariable @Valid Long id) {
        ResponseUserDTO responseUserDTO=userService.findById(id);
        CustomResponseDTO customResponseDTO = new CustomResponseDTO();
        customResponseDTO.setResponseObject(responseUserDTO);
        customResponseDTO.setResponseMessage("User found with id "+ id);

        return new ResponseEntity<>(customResponseDTO, HttpStatus.OK);

    }







}
