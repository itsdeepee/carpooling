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

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorDetailDTO> handleAuthorizationException(AuthorizationException ex){
        return new ResponseEntity<>(
                getCustomErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Unauthorized access",ex.getMessage()),
                null,HttpStatus.UNAUTHORIZED);
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

    private ErrorDetailDTO getCustomErrorDTO(int status, String title, String message){
        ErrorDetailDTO errorDetailDTO=new ErrorDetailDTO();
        errorDetailDTO.setTimeStamp(LocalDateTime.now().toString());
        errorDetailDTO.setStatus(status);
        errorDetailDTO.setTitle(title);
        errorDetailDTO.setTitle(message);
        return errorDetailDTO;
    }




}
