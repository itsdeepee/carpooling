package org.example.Exceptions.Ride;


import org.example.Exceptions.RideRequest.CustomExceptions.RequestsForFullRideException;
import org.example.Model.DTOs.ErrorDetailDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RideExceptionHandler {




    private ErrorDetailDTO getCustomErrorDTO(int status, String title, String message){
        ErrorDetailDTO errorDetailDTO=new ErrorDetailDTO();
        errorDetailDTO.setTimeStamp(LocalDateTime.now().toString());
        errorDetailDTO.setStatus(status);
        errorDetailDTO.setTitle(title);
        errorDetailDTO.setTitle(message);
        return errorDetailDTO;
    }
}
