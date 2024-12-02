package ozdemir0ozdemir.userservice.bridge;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {
    private T object;
    private Instant respondAt;
    private String status;
    private String message;

    public static <T> Response<T> found(T object) {
        return new Response<>(object, Instant.now(), "succeeded", "Found");
    }

    public static <T> Response<T> notFound() {
        return new Response<>(null, Instant.now(), "failed", "Not Found");
    }
}
