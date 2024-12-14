package ozdemir0ozdemir.nirobank.tokenservice.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ozdemir0ozdemir.common.response.Response;
import ozdemir0ozdemir.nirobank.tokenservice.exception.RefreshTokenExpiredException;
import ozdemir0ozdemir.nirobank.tokenservice.exception.RefreshTokenNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

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


}
