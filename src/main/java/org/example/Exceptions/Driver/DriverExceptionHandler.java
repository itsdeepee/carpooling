package org.example.Exceptions.Driver;


import org.example.Exceptions.User.DuplicateEmailException;
import org.example.Exceptions.User.UserCreationException;
import org.example.Exceptions.User.UserNotFoundException;
import org.example.Model.DTOs.ErrorDetailDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;


@ControllerAdvice
public class DriverExceptionHandler {

    @ExceptionHandler(DriverRegistrationException.class)
    public ResponseEntity<?> handleDriverRegistrationException(DriverRegistrationException ex){
        ErrorDetailDTO errorDetailDTO=new ErrorDetailDTO();
        errorDetailDTO.setTimeStamp(LocalDateTime.now().toString());
        errorDetailDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetailDTO.setTitle("User cannot be registered as driver");
        errorDetailDTO.setDetail(ex.getMessage());

        return new ResponseEntity<>(errorDetailDTO,null,HttpStatus.BAD_REQUEST);
    }




}
