package org.example.Exceptions.RideRequest;

import org.example.Model.DTOs.ErrorDetailDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RideRequestExceptionHandler {

    @ExceptionHandler(DuplicateRideRequestException.class)
    public ResponseEntity<ErrorDetailDTO> handleDuplicateRideRequestException(DuplicateRideRequestException ex){
        return new ResponseEntity<>(
                getCustomErrorDTO(HttpStatus.BAD_REQUEST.value(), "Unable to create request",ex.getMessage()),
                null,HttpStatus.BAD_REQUEST);
    }



    private ErrorDetailDTO getCustomErrorDTO(int status, String title, String message){
        ErrorDetailDTO errorDetailDTO=new ErrorDetailDTO();
        errorDetailDTO.setTimeStamp(LocalDateTime.now().toString());
        errorDetailDTO.setStatus(status);
        errorDetailDTO.setTitle(title);
        errorDetailDTO.setTitle(message);
        return errorDetailDTO;
    }
}
