package ozdemir0ozdemir.nirobank.tokenservice.api;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ozdemir0ozdemir.common.response.Response;
import ozdemir0ozdemir.nirobank.tokenservice.exception.RefreshTokenExpiredException;
import ozdemir0ozdemir.nirobank.tokenservice.exception.RefreshTokenNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    ResponseEntity<Response<?>> handle(RefreshTokenNotFoundException ex) {
        Response<?> response = Response.failed(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    ResponseEntity<Response<?>> handle(RefreshTokenExpiredException ex) {
        Response<?> response = Response.failed(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Response<Map<?, ?>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errorMessages = new ArrayList<>();
        Map<String, List<String>> errors = new HashMap<>();
        errors.put("errors", errorMessages);

        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> errorMessages.add(error.getDefaultMessage()));

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Response.failed(errors, "Please provide valid parameters"));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Response<Map<?, ?>>> handle(ConstraintViolationException ex) {
        List<String> errorMessages = new ArrayList<>();
        Map<String, List<String>> errors = new HashMap<>();
        errors.put("errors", errorMessages);

        ex.getConstraintViolations()
                .forEach(error -> errorMessages.add(error.getMessage()));

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Response.failed(errors, "Please provide valid parameters"));
    }


}
