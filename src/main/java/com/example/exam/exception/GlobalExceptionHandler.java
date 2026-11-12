package com.example.exam.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(HttpClientErrorException.class)
    public ProblemDetail handleNotFound(HttpClientErrorException.NotFound ex){
        log.warn("Recurso no encontrado en la Petstore: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Pet no encontrado");

    }

    @ExceptionHandler(RestClientException.class)
    public ProblemDetail handlePetstoreError(RestClientException ex){
        log.error("Error al comunicarse a la Petstore", ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY, "Error al comunicarse con el servicio");

    }

}
