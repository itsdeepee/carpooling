package org.example.Exceptions.RideRequest;

import org.example.Exceptions.RideRequest.CustomExceptions.*;
import org.example.Model.DTOs.ErrorDetailDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RideRequestExceptionHandler {
    /**
     * Handles DuplicateRideRequestException when thrown within the controller.
     * Returns a ResponseEntity with an ErrorDetailDTO containing details about the conflict.
     *
     * @param ex The DuplicateRideRequestException caught.
     * @return ResponseEntity containing an ErrorDetailDTO describing the conflict.
     */
    @ExceptionHandler(DuplicateRideRequestException.class)
    public ResponseEntity<ErrorDetailDTO> handleDuplicateRideRequestException(DuplicateRideRequestException ex){
        return new ResponseEntity<>(
                getCustomErrorDTO(HttpStatus.CONFLICT.value(), "Duplicate ride request detected",ex.getMessage()),
                null,HttpStatus.CONFLICT);
    }

    /**
     * Handles RideRequestNotFoundException occurring within the controller.
     * Constructs a ResponseEntity with an ErrorDetailDTO indicating the inability to find a ride request.
     *
     * @param ex The RideRequestNotFoundException caught.
     * @return ResponseEntity containing an ErrorDetailDTO for the missing ride request scenario.
     */
    @ExceptionHandler(RideRequestNotFoundException.class)
    public ResponseEntity<ErrorDetailDTO> handleRideRequestNotFoundException(RideRequestNotFoundException ex){
        return new ResponseEntity<>(
                getCustomErrorDTO(HttpStatus.NOT_FOUND.value(),
                        "Ride request not found",ex.getMessage()),
                null,HttpStatus.NOT_FOUND);
    }
    /**
     * Handles InvalidRideRequestDeleteException occurring within the controller.
     * Constructs a ResponseEntity with an ErrorDetailDTO indicating the inability to delete the ride request.
     *
     * @param ex The InvalidRideRequestDeleteException caught.
     * @return ResponseEntity containing an ErrorDetailDTO for the failed ride request deletion scenario.
     */
    @ExceptionHandler(InvalidRideRequestDeleteException.class)
    public ResponseEntity<ErrorDetailDTO> handleInvalidRideRequestDeleteException(InvalidRideRequestDeleteException ex){
        return new ResponseEntity<>(
                getCustomErrorDTO(HttpStatus.BAD_REQUEST.value(), "Unable to cancel ride request",ex.getMessage()),
                null,HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles InvalidRideRequestAcceptException occurring within the controller.
     * Constructs a ResponseEntity with an ErrorDetailDTO indicating the inability to accept the ride request.
     *
     * @param ex The InvalidRideRequestAcceptException caught.
     * @return ResponseEntity containing an ErrorDetailDTO for the failed ride request acceptance scenario.
     */
    @ExceptionHandler(InvalidRideRequestAcceptException.class)
    public ResponseEntity<ErrorDetailDTO> handleInvalidRideRequestAcceptException(InvalidRideRequestAcceptException ex){
        return new ResponseEntity<>(
                getCustomErrorDTO(HttpStatus.BAD_REQUEST.value(), "Unable to accept ride request",ex.getMessage()),
                null,HttpStatus.BAD_REQUEST);
    }


    /**
     * Handles RequestsForFullRideException occurring within the controller.
     * Constructs a ResponseEntity with an ErrorDetailDTO indicating the inability to process a request for a fully booked ride.
     *
     * @param ex The RequestsForFullRideException caught.
     * @return ResponseEntity containing an ErrorDetailDTO for the full ride request scenario.
     */
    @ExceptionHandler(RequestsForFullRideException.class)
    public ResponseEntity<ErrorDetailDTO> handleRequestsForFullRideException(RequestsForFullRideException ex){
        return new ResponseEntity<>(
                getCustomErrorDTO(HttpStatus.BAD_REQUEST.value(), "Unable to process request for ride",ex.getMessage()),
                null,HttpStatus.BAD_REQUEST);
    }
    /**
     * Handles IllegalStatusException occurring within the controller.
     * Constructs a ResponseEntity with an ErrorDetailDTO for an invalid ride request status.
     *
     * @param ex The IllegalStatusException caught.
     * @return ResponseEntity containing an ErrorDetailDTO describing the invalid status error.
     */
    @ExceptionHandler(IllegalStatusException.class)
    public ResponseEntity<ErrorDetailDTO> handleIllegalStatusException(IllegalStatusException ex){
        return new ResponseEntity<>(
                getCustomErrorDTO(HttpStatus.BAD_REQUEST.value(),
                        "Invalid Ride Request Status",ex.getMessage()),
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
