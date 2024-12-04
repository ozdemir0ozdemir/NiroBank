package ozdemir0ozdemir.nirobank.tokenservice.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ozdemir0ozdemir.nirobank.client.userclient.Response;
import ozdemir0ozdemir.nirobank.tokenservice.exception.TokenExpiredException;
import ozdemir0ozdemir.nirobank.tokenservice.exception.TokenGenerationException;
import ozdemir0ozdemir.nirobank.tokenservice.exception.TokenNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({TokenGenerationException.class, TokenNotFoundException.class, TokenExpiredException.class})
    ResponseEntity<Response<?>> handleTokenGenerationException(RuntimeException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Response.exception(ex.getMessage()));
    }

}
