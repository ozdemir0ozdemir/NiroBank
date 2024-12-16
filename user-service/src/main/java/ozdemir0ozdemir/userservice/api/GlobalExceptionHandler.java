package ozdemir0ozdemir.userservice.api;

import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ozdemir0ozdemir.common.response.Response;
import ozdemir0ozdemir.userservice.exception.UserNotFoundException;
import ozdemir0ozdemir.userservice.exception.UsernameAlreadyExistsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    ResponseEntity<Response<Void>> handleUsernameAlreadyExistsException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Response.failed(ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<Response<Void>> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Response.failed(ex.getMessage()));
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

}
