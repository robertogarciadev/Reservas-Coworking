package org.example.exception;



import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Collectors;


@ControllerAdvice
public class GlobalExceptionHandler {

    // Maneja validaciones @Valid (errores de DTO)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {

        // Extrae los detalles del error
        String details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        // Crear objeto del error
        ErrorResponse error = ErrorResponse.builder()
                .timeStamp(LocalDate.now())
                .message(ex.getMessage())
                .details(details)
                .code(ex.getStatusCode().value()).build();

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(RangeIlegalException.class)
    public ResponseEntity<ErrorResponse> handlerRangeIlegalException(RangeIlegalException ex){
        // Crear objeto del error
        ErrorResponse error = ErrorResponse.builder()
                .timeStamp(LocalDate.now())
                .message(ex.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.badRequest().body(error);
    }

}

