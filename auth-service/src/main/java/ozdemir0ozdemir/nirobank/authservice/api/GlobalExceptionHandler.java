package ozdemir0ozdemir.nirobank.authservice.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ozdemir0ozdemir.nirobank.common.response.Response;
import ozdemir0ozdemir.nirobank.authservice.exception.TokenClientException;
import ozdemir0ozdemir.nirobank.authservice.exception.UserClientException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
