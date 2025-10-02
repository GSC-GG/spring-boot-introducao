package br.ifsp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class NullPointerExceptionHandler {

    // O erro retorna um HttpStatus.NOT_FOUND (erro 404)
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFoundElement(NullPointerException e) {
        return e.getMessage();
    }
}