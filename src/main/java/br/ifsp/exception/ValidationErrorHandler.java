package br.ifsp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.ifsp.dto.ErrorResponse;

@RestControllerAdvice
class ValidationErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> failedValidation(MethodArgumentNotValidException e) {
        ErrorResponse error = new ErrorResponse(e.getFieldError().getDefaultMessage());
        // Resposta especial com a mensagem de erro indicada (vide atributos com validação em model/Contact.java) e HttpStatus.BAD_REQUEST (erro 400)
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
}