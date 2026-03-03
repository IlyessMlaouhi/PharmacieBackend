package org.example.pharmaciebackend.Config;


import org.example.pharmaciebackend.Dtos.ErrorMessage;
import org.example.pharmaciebackend.Exceptions.EmployeeException;
import org.example.pharmaciebackend.Exceptions.ShiftException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmployeeException.class)
    public ResponseEntity<ErrorMessage> handleBusinessException(final EmployeeException ex) {
        return ResponseEntity.badRequest().body(new ErrorMessage("business error", ex.getMessage()));
    }

    @ExceptionHandler(ShiftException.class)
    public ResponseEntity<ErrorMessage> handleSongNotFoundException(final ShiftException ex) {
        return ResponseEntity.badRequest().body(new ErrorMessage("there aren't any songs", ex.getMessage()));
    }
}
