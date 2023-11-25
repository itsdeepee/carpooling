package org.example.Exceptions.User;


import org.example.Model.DTOs.ErrorDetailDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.time.LocalDateTime;


@ControllerAdvice
public class UserExceptionHandler {





    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex){
        ErrorDetailDTO errorDetailDTO=new ErrorDetailDTO();
        errorDetailDTO.setTimeStamp(LocalDateTime.now().toString());
        errorDetailDTO.setStatus(HttpStatus.NOT_FOUND.value());
        errorDetailDTO.setTitle("User not found");
        errorDetailDTO.setDetail(ex.getMessage());


        return new ResponseEntity<>(errorDetailDTO,null,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<?> handleDuplicateEmailException(DuplicateEmailException ex){
        ErrorDetailDTO errorDetail=new ErrorDetailDTO();
        errorDetail.setTimeStamp(LocalDateTime.now().toString());
        errorDetail.setStatus(HttpStatus.CONFLICT.value());
        errorDetail.setTitle("User already exists");
        errorDetail.setDetail(ex.getMessage());


        return new ResponseEntity<>(errorDetail,null,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<?> handleUserCreationException(UserCreationException ex){
        ErrorDetailDTO errorDetail=new ErrorDetailDTO();
        errorDetail.setTimeStamp(LocalDateTime.now().toString());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetail.setTitle("Failed to create user");
        errorDetail.setDetail(ex.getMessage());


        return new ResponseEntity<>(errorDetail,null,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex){

        ErrorDetailDTO errorDetail=new ErrorDetailDTO();
        errorDetail.setTimeStamp(LocalDateTime.now().toString());
        errorDetail.setTitle("Internal server error. Request could not be processed");
        errorDetail.setDetail("Ensure the data is correct or try again later.");
        errorDetail.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorDetail,null,HttpStatus.INTERNAL_SERVER_ERROR);

    }


}
