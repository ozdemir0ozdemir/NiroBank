package ozdemir0zdemir.authservice.api;

import feign.FeignException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ozdemir0ozdemir.common.response.Response;

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.class)
    ResponseEntity<?> handleFeignEx(FeignException ex) {
        if (ex.responseBody().isPresent()) {
            String response = new String(ex.responseBody().get().array());
            return ResponseEntity.status(ex.status()).body(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.failed("Request failed"));
    }
}
