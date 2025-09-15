package org.workinkexceptiondemo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.workinkexceptiondemo.dto.GeneralResponse;
import org.workinkexceptiondemo.service.exception.AlreadyExistException;
import org.workinkexceptiondemo.service.exception.NotFoundException;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(DateTimeParseException.class)
    public GeneralResponse<String> handlerDateTimeParseException(DateTimeParseException e){
        return new GeneralResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),"Неверный формат даты");
    }

    @ExceptionHandler(NullPointerException.class)
    public GeneralResponse<String> handlerNullPointerException(NullPointerException e){
        return new GeneralResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),"Отсутствуют необходимые данные");
    }

    @ExceptionHandler(NotFoundException.class)
    public GeneralResponse<String> handlerNotFoundException(NotFoundException e){
        return new GeneralResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),"Отсутствуют необходимые данные");
    }

    @ExceptionHandler(AlreadyExistException.class)
    public GeneralResponse<String> handlerAlreadyExistException(AlreadyExistException e){
        return new GeneralResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(),"Отсутствуют необходимые данные");
    }



}
