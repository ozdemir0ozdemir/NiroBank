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

    public static Response<User> userFound(User user) {
        return new Response<>(user, Instant.now(), "success", "User found");
    }

    public static Response<User> userNotFound() {
        return new Response<>(null, Instant.now(), "failed", "User not found");
    }
}
