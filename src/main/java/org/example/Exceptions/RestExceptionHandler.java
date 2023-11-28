package org.example.Exceptions;


import org.example.Model.DTOs.ErrorDetailDTO;
import org.example.Model.DTOs.ValidationErrorDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;



@ControllerAdvice
public class RestExceptionHandler {

    private static final Logger logger= (Logger) LoggerFactory.getLogger(RestExceptionHandler.class);
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException typeMismatchEx){

        logger.error("Method argument type mismatch exception occurred: {}",typeMismatchEx.getMessage());
        ErrorDetailDTO errorDetail=new ErrorDetailDTO();
        errorDetail.setTimeStamp(LocalDateTime.now().toString());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetail.setTitle("URL parameter value is invalid: "+typeMismatchEx.getName());
        errorDetail.setDetail(typeMismatchEx.getMostSpecificCause().getLocalizedMessage());

        return new ResponseEntity<>(errorDetail,null,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleJsonParseException(HttpMessageNotReadableException ex){
        // add logging

        ErrorDetailDTO errorDetail =new ErrorDetailDTO();
        errorDetail.setTimeStamp(LocalDateTime.now().toString());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetail.setTitle("Invalid JSON format or data. Please check your request.");
        errorDetail.setDetail(ex.getRootCause().getLocalizedMessage());

        return new ResponseEntity<>(errorDetail,null,HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException methodArgNotValid){
        ErrorDetailDTO errorDetailDTO=new ErrorDetailDTO();

        //Populate errorDetailDTO instance
        errorDetailDTO.setTimeStamp(LocalDateTime.now().toString());
        errorDetailDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetailDTO.setTitle("Validation failed");
        errorDetailDTO.setDetail("Input validation failed");

        //Create ValidationError instances
        List<FieldError> fieldErrors=methodArgNotValid.getBindingResult().getFieldErrors();
        for(FieldError fe: fieldErrors){
            List<ValidationErrorDTO> validationErrorDTOSList=errorDetailDTO.getErrors().get(fe.getField());
            if(validationErrorDTOSList==null){
                validationErrorDTOSList=new ArrayList<ValidationErrorDTO>();
                errorDetailDTO.getErrors().put(fe.getField(),validationErrorDTOSList);
            }

            ValidationErrorDTO validationErrorDTO=new ValidationErrorDTO();
            validationErrorDTO.setCode(fe.getCode());
            validationErrorDTO.setMessage(fe.getDefaultMessage());
            validationErrorDTOSList.add(validationErrorDTO);
        }
        return new ResponseEntity<>(errorDetailDTO,null,HttpStatus.BAD_REQUEST);

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
