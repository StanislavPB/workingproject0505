package org.workinkexceptiondemo.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.workinkexceptiondemo.dto.GeneralResponse;
import org.workinkexceptiondemo.service.exception.AlreadyExistException;
import org.workinkexceptiondemo.service.exception.NotFoundException;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handlerDateTimeParseException(DateTimeParseException e){
        return new ResponseEntity<>( e.getMessage() , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handlerNullPointerException(NullPointerException e){
        return new ResponseEntity<>( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handlerNotFoundException(NotFoundException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<String> handlerAlreadyExistException(AlreadyExistException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handlerConstraintViolationException(ConstraintViolationException e){
        StringBuilder responseMessage = new StringBuilder();

        e.getConstraintViolations().forEach(
                constraintViolation -> {
                    String currentField = constraintViolation.getPropertyPath().toString();
                    String currentMessage = constraintViolation.getMessage();
                    responseMessage.append("В поле: " + currentField + " : " + currentMessage);
                    responseMessage.append("\n");
                }
        );
        return responseMessage.toString();
    }

}
