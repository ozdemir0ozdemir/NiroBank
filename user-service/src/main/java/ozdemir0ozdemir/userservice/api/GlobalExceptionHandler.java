package ozdemir0ozdemir.userservice.api;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ozdemir0ozdemir.userservice.bridge.Response;

import java.time.Instant;

@ControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // TODO: Explain why an error occurred to the user? is duplicate keys the only violation?
    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<Response<?>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new Response<>(null, Instant.now(), "failed", ex.getMessage()));
    }
}
