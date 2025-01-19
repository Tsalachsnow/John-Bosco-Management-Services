package com.johnboscoltd.exceptions;

import com.johnboscoltd.exceptions.Error.GenericErrorResponse;
import com.johnboscoltd.exceptions.Error.UrlNotFoundError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GenericException.class)
    public ResponseEntity<?> handleCustomException(GenericException ex) {
        // Create a structured error response
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(new GenericErrorResponse(
                        ex.getResponseMessage(),
                        ex.getHttpStatus().value(),
                        ex.getResponseCode()
                ));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<UrlNotFoundError> handle404(NoHandlerFoundException ex) {
        UrlNotFoundError errorResponse = new UrlNotFoundError(
                HttpStatus.NOT_FOUND.value(),
                "Url Not Found",
                ex.getRequestURL()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}
