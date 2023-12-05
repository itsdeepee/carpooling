package org.example.Exceptions.Ride;


import org.example.Exceptions.Ride.CustomExceptions.InactiveRideUpdateException;
import org.example.Exceptions.Ride.CustomExceptions.UnavailableRideException;
import org.example.Exceptions.RideRequest.CustomExceptions.IllegalStatusException;
import org.example.Exceptions.RideRequest.CustomExceptions.RequestsForFullRideException;
import org.example.Model.DTOs.ErrorDetailDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RideExceptionHandler {
    /**
     * Handles InactiveRideUpdateException by providing a custom error response.
     *
     * @param ex The InactiveRideUpdateException thrown during ride updates.
     * @return ResponseEntity containing ErrorDetailDTO with custom error information.
     */
    @ExceptionHandler(InactiveRideUpdateException.class)
    public ResponseEntity<ErrorDetailDTO> handleInactiveRideUpdateException(InactiveRideUpdateException ex){
        return new ResponseEntity<>(
                getCustomErrorDTO(HttpStatus.BAD_REQUEST.value(),
                        "Invalid Ride Status Update Request",ex.getMessage()),
                null,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnavailableRideException.class)
    public ResponseEntity<ErrorDetailDTO> handleUnavailableRideException(UnavailableRideException ex){
        return new ResponseEntity<>(
                getCustomErrorDTO(HttpStatus.BAD_REQUEST.value(),
                        "Unavailable Ride",ex.getMessage()),
                null,HttpStatus.BAD_REQUEST);
    }


    /**
     * Creates a custom ErrorDetailDTO with the given status, title, and message.
     *
     * @param status  The HTTP status code.
     * @param title   The title of the error.
     * @param message The detailed error message.
     * @return ErrorDetailDTO containing the customized error details.
     */
    private ErrorDetailDTO getCustomErrorDTO(int status, String title, String message){
        ErrorDetailDTO errorDetailDTO=new ErrorDetailDTO();
        errorDetailDTO.setTimeStamp(LocalDateTime.now().toString());
        errorDetailDTO.setStatus(status);
        errorDetailDTO.setTitle(title);
        errorDetailDTO.setDetail(message);
        return errorDetailDTO;
    }
}
