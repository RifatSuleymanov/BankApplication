package ru.suleyman.bank.exception.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.suleyman.bank.exception.model.ErrorResponse;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handlerRuntimeException(final RuntimeException e) {
        return new ErrorResponse("INTERNAL SERVER ERROR", e.getMessage());
    }

}