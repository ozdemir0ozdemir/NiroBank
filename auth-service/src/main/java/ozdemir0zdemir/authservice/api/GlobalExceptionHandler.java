package ozdemir0zdemir.authservice.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ozdemir0ozdemir.common.response.Response;
import ozdemir0zdemir.authservice.exception.TokenClientException;
import ozdemir0zdemir.authservice.exception.UserClientException;

import java.io.IOException;

@RestControllerAdvice
@RequiredArgsConstructor
class GlobalExceptionHandler {
    private final ObjectMapper mapper;

    @ExceptionHandler(FeignException.class)
    ResponseEntity<?> handleFeignEx(FeignException ex) throws IOException {
        if (ex.responseBody().isPresent()) {
            Response<?> response = mapper.readValue(ex.responseBody().get().array(), new TypeReference<>() {});
            return ResponseEntity.status(ex.status()).body(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.failed("Request failed"));
    }

    @ExceptionHandler({TokenClientException.class, UserClientException.class})
    ResponseEntity<?> handleClientEx(RuntimeException ex) throws IOException {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.failed(ex.getMessage()));
    }
}
