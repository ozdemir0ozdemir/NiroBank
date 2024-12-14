package ozdemir0ozdemir.userservice.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ozdemir0ozdemir.common.response.Response;
import ozdemir0ozdemir.userservice.exception.UserNotFoundException;
import ozdemir0ozdemir.userservice.exception.UsernameAlreadyExistsException;

@ControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

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
}
