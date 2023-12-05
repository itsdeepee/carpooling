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
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@ControllerAdvice
public class RestExceptionHandler {

    private static final Logger logger= (Logger) LoggerFactory.getLogger(RestExceptionHandler.class);
    /**
     * Handles MethodArgumentTypeMismatchException caused by a mismatch between the expected
     * parameter type and the actual value received, commonly occurring in URL path variables.
     *
     * @param typeMismatchEx The MethodArgumentTypeMismatchException caught.
     * @return ResponseEntity containing an ErrorDetailDTO describing the URL parameter value mismatch.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDetailDTO> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException typeMismatchEx){
        //EXPLANATION FOR ME: If a method expects an integer parameter but receives a string
        //that cannot be parsed into an integer from a URL path variable.
        logger.error("Method argument type mismatch exception occurred: {}",typeMismatchEx.getMessage());

        return new ResponseEntity<>(
                getCustomErrorDTO(HttpStatus.BAD_REQUEST.value(),
                        "URL parameter value mismatch: " + typeMismatchEx.getName(),
                        "The value provided for the URL parameter '" + typeMismatchEx.getName() +
                                "' is not of the expected type. Please provide a valid value."),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles HttpMessageNotReadableException caused by incorrect JSON format or incompatible data types
     * in the payload of an HTTP request.
     *
     * @param ex The HttpMessageNotReadableException caught.
     * @return ResponseEntity containing an ErrorDetailDTO describing the JSON parsing error.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetailDTO> handleJsonParseException(HttpMessageNotReadableException ex){
        //TODO: Add logging
        //EXPLANATION FOR ME: It occurs when the payload of an HTTP request cannot be
        // deserialized into an object due to incorrect JSON format or incompatible data types.
        return new ResponseEntity<>(
                getCustomErrorDTO(HttpStatus.CONFLICT.value(),
                        "Invalid request payload",
                        "The data provided in the request payload is either incorrectly formatted " +
                                "or contains incompatible data types. Please verify and correct the request."),
                HttpStatus.CONFLICT);

    }
    /**
     * Handles MethodArgumentNotValidException triggered by validation errors in request parameters or bodies.
     * Generates an ErrorDetailDTO containing validation error details for the client.
     *
     * @param methodArgNotValid The MethodArgumentNotValidException caught.
     * @return ResponseEntity containing an ErrorDetailDTO describing validation failure.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetailDTO> handleValidationExceptions(MethodArgumentNotValidException methodArgNotValid){
        //EXPLANATION FOR ME:If you have a controller method receiving a DTO and that DTO has validation constraints
        // on its fields, when a request is made with invalid data that violates these constraints
        // (like passing an empty string to a field annotated with @NotBlank), this exception will be thrown.

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
//    /**
//     * Handles DataIntegrityViolationException caused by database integrity constraint violations.
//     * Constructs an ErrorDetailDTO indicating a conflict due to data integrity issues.
//     *
//     * @param ex The DataIntegrityViolationException caught.
//     * @return ResponseEntity containing an ErrorDetailDTO describing the database integrity violation.
//     */
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<ErrorDetailDTO> handleDataIntegrityViolation(DataIntegrityViolationException ex){
//        return new ResponseEntity<>(
//                getCustomErrorDTO(HttpStatus.CONFLICT.value(),
//                        "Conflict: Database Integrity Violation",
//                        "The operation could not be completed due to data integrity constraints. " +
//                                "Ensure the data is correct or remove conflicting records."),
//                        HttpStatus.CONFLICT);
//
//
//    }

    /**
     * Handles NoHandlerFoundException when no handler is found for the requested endpoint.
     * Returns a ResponseEntity with an error message indicating that the path doesn't match any existing endpoint.
     *
     * @param ex The NoHandlerFoundException caught.
     * @return ResponseEntity containing an ErrorDetailDTO describing the endpoint not found.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDetailDTO> handleNotFoundException(NoHandlerFoundException ex){
        return new ResponseEntity<>(
                getCustomErrorDTO(HttpStatus.NOT_FOUND.value(), "Path does not match any existing endpoint",ex.getMessage()),
                null,HttpStatus.NOT_FOUND);
    }

    private ErrorDetailDTO getCustomErrorDTO(int status, String title, String message){
        ErrorDetailDTO errorDetailDTO=new ErrorDetailDTO();
        errorDetailDTO.setTimeStamp(LocalDateTime.now().toString());
        errorDetailDTO.setStatus(status);
        errorDetailDTO.setTitle(title);
        errorDetailDTO.setDetail(message);
        return errorDetailDTO;
    }


}
